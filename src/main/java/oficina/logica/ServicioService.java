package oficina.logica;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oficina.dao.ServicioDao;
import oficina.dao.TurnoDao;
import oficina.modelo.Servicio;

public class ServicioService {
    private final ServicioDao servicioDao;
    private final TurnoDao turnoDao;

    public ServicioService(ServicioDao servicioDao, TurnoDao turnoDao) {
        this.servicioDao = servicioDao;
        this.turnoDao = turnoDao;
    }

    public void registrar(int codigo, String nombre) throws NegocioException {
        Validaciones.codigoPositivo(codigo);
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre");
        try {
            if (servicioDao.buscarPorCodigo(codigo) != null) {
                throw new NegocioException("Ya existe un servicio con el código " + codigo + ".");
            }
            servicioDao.insertar(new Servicio(codigo, nombreValido, true));
        } catch (SQLException e) {
            throw new NegocioException("No se pudo guardar el servicio: " + e.getMessage(), e);
        }
    }

    public List<Servicio> listar() throws NegocioException {
        try {
            return servicioDao.listarTodos();
        } catch (SQLException e) {
            throw new NegocioException("No se pudieron consultar los servicios: " + e.getMessage(), e);
        }
    }

    public Servicio obtener(int codigo) throws NegocioException {
        try {
            Servicio servicio = servicioDao.buscarPorCodigo(codigo);
            if (servicio == null) {
                throw new NegocioException("El servicio " + codigo + " no está registrado.");
            }
            return servicio;
        } catch (SQLException e) {
            throw new NegocioException("No se pudo consultar el servicio: " + e.getMessage(), e);
        }
    }

    public void actualizar(int codigo, String nombre) throws NegocioException {
        Servicio servicio = obtener(codigo);
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre");
        try {
            servicioDao.actualizar(new Servicio(codigo, nombreValido, servicio.isActivo()));
        } catch (SQLException e) {
            throw new NegocioException("No se pudo actualizar el servicio: " + e.getMessage(), e);
        }
    }

    public void cambiarEstado(int codigo, boolean activo) throws NegocioException {
        Servicio servicio = obtener(codigo);
        if (servicio.isActivo() == activo) {
            throw new NegocioException("El servicio ya está " + (activo ? "activo" : "inactivo") + ".");
        }
        try {
            if (!activo && turnoDao.contarPendientesDeServicio(codigo) > 0) {
                throw new NegocioException("No se puede desactivar: el servicio tiene turnos pendientes.");
            }
            servicioDao.actualizar(new Servicio(codigo, servicio.getNombre(), activo));
        } catch (SQLException e) {
            throw new NegocioException("No se pudo cambiar el estado: " + e.getMessage(), e);
        }
    }

    /** Mapa código -> nombre, útil para mostrar turnos sin consultar la BD fila por fila. */
    public Map<Integer, String> mapaNombres() throws NegocioException {
        Map<Integer, String> nombres = new HashMap<>();
        for (Servicio servicio : listar()) {
            nombres.put(servicio.getCodigo(), servicio.getNombre());
        }
        return nombres;
    }
}
