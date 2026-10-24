package oficina.logica;

import java.sql.SQLException;
import java.util.List;
import oficina.dao.TurnoDao;
import oficina.modelo.EstadoTurno;
import oficina.modelo.ResumenServicio;
import oficina.modelo.Turno;

public class ReporteService {
    private final TurnoDao turnoDao;
    private final ClienteService clienteService;

    public ReporteService(TurnoDao turnoDao, ClienteService clienteService) {
        this.turnoDao = turnoDao;
        this.clienteService = clienteService;
    }

    public List<Turno> atendidos() throws NegocioException {
        return porEstado(EstadoTurno.ATENDIDO);
    }

    public List<Turno> cancelados() throws NegocioException {
        return porEstado(EstadoTurno.CANCELADO);
    }

    public List<Turno> historialCliente(int codigoCliente) throws NegocioException {
        clienteService.obtener(codigoCliente);
        try {
            return turnoDao.listarPorCliente(codigoCliente);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo consultar el historial: " + e.getMessage(), e);
        }
    }

    public List<ResumenServicio> resumenPorServicio() throws NegocioException {
        try {
            return turnoDao.resumenPorServicio();
        } catch (SQLException e) {
            throw new NegocioException("No se pudo generar el resumen: " + e.getMessage(), e);
        }
    }

    private List<Turno> porEstado(EstadoTurno estado) throws NegocioException {
        try {
            return turnoDao.listarPorEstado(estado);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo consultar los turnos: " + e.getMessage(), e);
        }
    }
}
