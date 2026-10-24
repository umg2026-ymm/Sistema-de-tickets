package oficina.logica;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import oficina.dao.TurnoDao;
import oficina.estructuras.ColaTurnos;
import oficina.modelo.Cliente;
import oficina.modelo.EstadoTurno;
import oficina.modelo.Servicio;
import oficina.modelo.Turno;
import oficina.util.Fechas;

/** Generación, atención y cancelación de turnos. La BD se actualiza antes que la cola. */
public class TurnoService {
    private final TurnoDao turnoDao;
    private final ClienteService clienteService;
    private final ServicioService servicioService;
    private final ColaTurnos cola = new ColaTurnos();

    public TurnoService(TurnoDao turnoDao, ClienteService clienteService, ServicioService servicioService) {
        this.turnoDao = turnoDao;
        this.clienteService = clienteService;
        this.servicioService = servicioService;
    }

    /** Reconstruye la cola con los turnos PENDIENTES guardados, ordenados por número. */
    public void cargarCola() throws NegocioException {
        try {
            for (Turno turno : turnoDao.listarPorEstado(EstadoTurno.PENDIENTE)) {
                cola.encolar(turno);
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo reconstruir la cola: " + e.getMessage(), e);
        }
    }

    public Turno generar(int codigoCliente, int codigoServicio) throws NegocioException {
        Cliente cliente = clienteService.obtener(codigoCliente);
        if (!cliente.isActivo()) {
            throw new NegocioException("El cliente está inactivo y no puede recibir turnos.");
        }
        Servicio servicio = servicioService.obtener(codigoServicio);
        if (!servicio.isActivo()) {
            throw new NegocioException("El servicio está inactivo y no se pueden generar turnos para él.");
        }
        try {
            if (turnoDao.clienteTienePendiente(codigoCliente)) {
                throw new NegocioException("El cliente ya tiene un turno pendiente.");
            }
            LocalDateTime ahora = Fechas.ahora();
            int numero = turnoDao.insertar(codigoCliente, codigoServicio, ahora);
            Turno turno = new Turno(numero, codigoCliente, codigoServicio, ahora, EstadoTurno.PENDIENTE, null);
            cola.encolar(turno);
            return turno;
        } catch (SQLException e) {
            throw new NegocioException("No se pudo generar el turno: " + e.getMessage(), e);
        }
    }

    public Turno atenderSiguiente() throws NegocioException {
        Turno turno = cola.verPrimero();
        if (turno == null) {
            throw new NegocioException("No existen turnos pendientes.");
        }
        LocalDateTime ahora = Fechas.ahora();
        try {
            if (!turnoDao.cerrar(turno.getNumero(), EstadoTurno.ATENDIDO, ahora)) {
                throw new NegocioException("El turno " + turno.getNumero() + " ya no está pendiente en la base de datos.");
            }
        } catch (SQLException e) {
            throw new NegocioException("No se pudo atender el turno: " + e.getMessage(), e);
        }
        cola.desencolar();
        turno.marcarAtendido(ahora);
        return turno;
    }

    public void cancelar(int numero) throws NegocioException {
        try {
            Turno enBd = turnoDao.buscarPorNumero(numero);
            if (enBd == null) {
                throw new NegocioException("El turno " + numero + " no existe.");
            }
            if (enBd.getEstado() != EstadoTurno.PENDIENTE) {
                throw new NegocioException("El turno " + numero + " ya está " + enBd.getEstado()
                        + " y no se puede cancelar.");
            }
            LocalDateTime ahora = Fechas.ahora();
            if (!turnoDao.cerrar(numero, EstadoTurno.CANCELADO, ahora)) {
                throw new NegocioException("No se pudo cancelar el turno " + numero + ".");
            }
            Turno enCola = cola.buscar(numero);
            if (enCola != null) {
                enCola.marcarCancelado(ahora);
            }
            cola.cancelar(numero);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo cancelar el turno: " + e.getMessage(), e);
        }
    }

    /** Turnos pendientes en orden de atención (recorrido de la cola). */
    public List<Turno> listarPendientes() {
        return cola.recorrer();
    }
}
