package oficina;

import oficina.dao.ClienteDao;
import oficina.dao.ClienteDaoSqlite;
import oficina.dao.InicializadorBD;
import oficina.dao.ServicioDao;
import oficina.dao.ServicioDaoSqlite;
import oficina.dao.TurnoDao;
import oficina.dao.TurnoDaoSqlite;
import oficina.logica.ClienteService;
import oficina.logica.ReporteService;
import oficina.logica.ServicioService;
import oficina.logica.TurnoService;
import oficina.vista.MenuPrincipal;

/** Solo arma las piezas y arranca el menú. */
public class Main {
    public static void main(String[] args) {
        try {
            InicializadorBD.inicializarSiHaceFalta();

            ClienteDao clienteDao = new ClienteDaoSqlite();
            ServicioDao servicioDao = new ServicioDaoSqlite();
            TurnoDao turnoDao = new TurnoDaoSqlite();

            ClienteService clienteService = new ClienteService(clienteDao, turnoDao);
            ServicioService servicioService = new ServicioService(servicioDao, turnoDao);
            TurnoService turnoService = new TurnoService(turnoDao, clienteService, servicioService);
            ReporteService reporteService = new ReporteService(turnoDao, clienteService);

            clienteService.cargarArbol();   // BST desde SQLite
            turnoService.cargarCola();      // cola desde los turnos PENDIENTES

            new MenuPrincipal(clienteService, servicioService, turnoService, reporteService).iniciar();
        } catch (Exception e) {
            System.err.println("No se pudo iniciar la aplicación: " + e.getMessage());
        }
    }
}
