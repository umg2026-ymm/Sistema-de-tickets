package oficina.logica;

import java.sql.SQLException;
import java.util.List;
import oficina.dao.ClienteDao;
import oficina.dao.TurnoDao;
import oficina.estructuras.ArbolClientes;
import oficina.modelo.Cliente;

/** Reglas de clientes. Primero guarda en la BD y solo si funciona modifica el árbol. */
public class ClienteService {
    private final ClienteDao clienteDao;
    private final TurnoDao turnoDao;
    private final ArbolClientes arbol = new ArbolClientes();

    public ClienteService(ClienteDao clienteDao, TurnoDao turnoDao) {
        this.clienteDao = clienteDao;
        this.turnoDao = turnoDao;
    }

    /** Construye el BST con los clientes guardados en SQLite. */
    public void cargarArbol() throws NegocioException {
        try {
            for (Cliente cliente : clienteDao.listarTodos()) {
                arbol.insertar(cliente);
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudieron cargar los clientes: " + e.getMessage(), e);
        }
    }

    public void registrar(int codigo, String nombre, String telefono) throws NegocioException {
        Validaciones.codigoPositivo(codigo);
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre");
        String telefonoValido = Validaciones.telefono(telefono);
        if (arbol.buscar(codigo) != null) {
            throw new NegocioException("Ya existe un cliente con el código " + codigo + ".");
        }
        Cliente nuevo = new Cliente(codigo, nombreValido, telefonoValido, true);
        try {
            clienteDao.insertar(nuevo);
            arbol.insertar(nuevo);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo guardar el cliente: " + e.getMessage(), e);
        }
    }

    public List<Cliente> listar() {
        return arbol.inorden();
    }

    /** Devuelve el cliente o null si no existe. */
    public Cliente buscar(int codigo) {
        return arbol.buscar(codigo);
    }

    /** Devuelve el cliente o lanza error si el código no está registrado. */
    public Cliente obtener(int codigo) throws NegocioException {
        Cliente cliente = arbol.buscar(codigo);
        if (cliente == null) {
            throw new NegocioException("El cliente " + codigo + " no está registrado.");
        }
        return cliente;
    }

    public void actualizar(int codigo, String nombre, String telefono) throws NegocioException {
        Cliente cliente = obtener(codigo);
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre");
        String telefonoValido = Validaciones.telefono(telefono);
        try {
            clienteDao.actualizar(new Cliente(codigo, nombreValido, telefonoValido, cliente.isActivo()));
            cliente.setNombre(nombreValido);
            cliente.setTelefono(telefonoValido);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo actualizar el cliente: " + e.getMessage(), e);
        }
    }

    public void cambiarEstado(int codigo, boolean activo) throws NegocioException {
        Cliente cliente = obtener(codigo);
        if (cliente.isActivo() == activo) {
            throw new NegocioException("El cliente ya está " + (activo ? "activo" : "inactivo") + ".");
        }
        try {
            if (!activo && turnoDao.clienteTienePendiente(codigo)) {
                throw new NegocioException("No se puede desactivar: el cliente tiene un turno pendiente.");
            }
            clienteDao.actualizar(new Cliente(codigo, cliente.getNombre(), cliente.getTelefono(), activo));
            cliente.setActivo(activo);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo cambiar el estado: " + e.getMessage(), e);
        }
    }
}
