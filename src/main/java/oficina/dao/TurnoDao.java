package oficina.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import oficina.modelo.EstadoTurno;
import oficina.modelo.ResumenServicio;
import oficina.modelo.Turno;

public interface TurnoDao {
    /** Inserta un turno PENDIENTE y devuelve el número generado automáticamente. */
    int insertar(int clienteCodigo, int servicioCodigo, LocalDateTime fechaGeneracion) throws SQLException;

    /** Cambia un turno PENDIENTE a ATENDIDO/CANCELADO. Devuelve false si no se pudo (no estaba pendiente). */
    boolean cerrar(int numero, EstadoTurno nuevoEstado, LocalDateTime fecha) throws SQLException;

    Turno buscarPorNumero(int numero) throws SQLException;

    List<Turno> listarPorEstado(EstadoTurno estado) throws SQLException;

    List<Turno> listarPorCliente(int clienteCodigo) throws SQLException;

    boolean clienteTienePendiente(int clienteCodigo) throws SQLException;

    int contarPendientesDeServicio(int servicioCodigo) throws SQLException;

    List<ResumenServicio> resumenPorServicio() throws SQLException;
}
