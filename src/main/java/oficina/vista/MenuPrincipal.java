package oficina.vista;

import java.util.List;
import java.util.Map;
import oficina.logica.ClienteService;
import oficina.logica.NegocioException;
import oficina.logica.ReporteService;
import oficina.logica.ServicioService;
import oficina.logica.TurnoService;
import oficina.modelo.Cliente;
import oficina.modelo.EstadoTurno;
import oficina.modelo.ResumenServicio;
import oficina.modelo.Servicio;
import oficina.modelo.Turno;
import oficina.util.Fechas;

public class MenuPrincipal {
    private static final String NOMBRE_OFICINA = "Oficina de Servicios";

    private final Consola consola = new Consola();
    private final ClienteService clienteService;
    private final ServicioService servicioService;
    private final TurnoService turnoService;
    private final ReporteService reporteService;

    public MenuPrincipal(ClienteService clienteService, ServicioService servicioService,
                         TurnoService turnoService, ReporteService reporteService) {
        this.clienteService = clienteService;
        this.servicioService = servicioService;
        this.turnoService = turnoService;
        this.reporteService = reporteService;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = consola.leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> menuClientes();
                    case 2 -> menuServicios();
                    case 3 -> generarTurno();
                    case 4 -> atenderSiguiente();
                    case 5 -> cancelarTurno();
                    case 6 -> mostrarPendientes();
                    case 7 -> mostrarTurnos("TURNOS ATENDIDOS", reporteService.atendidos());
                    case 8 -> mostrarTurnos("TURNOS CANCELADOS", reporteService.cancelados());
                    case 9 -> historialCliente();
                    case 10 -> resumenPorServicio();
                    case 0 -> {
                        salir = true;
                        System.out.println("Hasta pronto.");
                    }
                    default -> System.out.println("Opción inexistente. Elija un número del menú.");
                }
            } catch (NegocioException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== " + NOMBRE_OFICINA + " =====");
        System.out.println(" 1. Gestión de clientes");
        System.out.println(" 2. Gestión de servicios");
        System.out.println(" 3. Generar turno");
        System.out.println(" 4. Atender siguiente turno");
        System.out.println(" 5. Cancelar turno");
        System.out.println(" 6. Consultar turnos pendientes");
        System.out.println(" 7. Consultar turnos atendidos");
        System.out.println(" 8. Consultar turnos cancelados");
        System.out.println(" 9. Historial de un cliente");
        System.out.println("10. Resumen por servicio");
        System.out.println(" 0. Salir");
    }

    // ---------- clientes ----------
    private void menuClientes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Gestión de clientes ---");
            System.out.println("1. Registrar  2. Consultar todos  3. Buscar por código");
            System.out.println("4. Actualizar nombre y teléfono  5. Activar/desactivar  0. Volver");
            int opcion = consola.leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> {
                        int codigo = consola.leerEntero("Código: ");
                        String nombre = consola.leerTextoObligatorio("Nombre completo: ");
                        String telefono = consola.leerTextoObligatorio("Teléfono (8 dígitos): ");
                        clienteService.registrar(codigo, nombre, telefono);
                        System.out.println("Cliente registrado.");
                    }
                    case 2 -> {
                        System.out.printf("%-8s %-25s %-10s %s%n", "CÓDIGO", "NOMBRE", "TELÉFONO", "ESTADO");
                        clienteService.listar().forEach(System.out::println);
                    }
                    case 3 -> {
                        int codigo = consola.leerEntero("Código a buscar: ");
                        Cliente cliente = clienteService.buscar(codigo);
                        System.out.println(cliente == null ? "No existe un cliente con ese código." : cliente);
                    }
                    case 4 -> {
                        int codigo = consola.leerEntero("Código del cliente: ");
                        String nombre = consola.leerTextoObligatorio("Nuevo nombre: ");
                        String telefono = consola.leerTextoObligatorio("Nuevo teléfono (8 dígitos): ");
                        clienteService.actualizar(codigo, nombre, telefono);
                        System.out.println("Cliente actualizado.");
                    }
                    case 5 -> {
                        int codigo = consola.leerEntero("Código del cliente: ");
                        String respuesta = consola.leerTexto("¿Activar (A) o desactivar (D)? ").toUpperCase();
                        if (respuesta.equals("A") || respuesta.equals("D")) {
                            clienteService.cambiarEstado(codigo, respuesta.equals("A"));
                            System.out.println("Estado actualizado.");
                        } else {
                            System.out.println("Respuesta inválida. Escriba A o D.");
                        }
                    }
                    case 0 -> volver = true;
                    default -> System.out.println("Opción inexistente.");
                }
            } catch (NegocioException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ---------- servicios ----------
    private void menuServicios() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Gestión de servicios ---");
            System.out.println("1. Registrar  2. Listar  3. Actualizar nombre  4. Activar/desactivar  0. Volver");
            int opcion = consola.leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> {
                        int codigo = consola.leerEntero("Código: ");
                        String nombre = consola.leerTextoObligatorio("Nombre: ");
                        servicioService.registrar(codigo, nombre);
                        System.out.println("Servicio registrado.");
                    }
                    case 2 -> {
                        System.out.printf("%-8s %-28s %s%n", "CÓDIGO", "NOMBRE", "ESTADO");
                        for (Servicio servicio : servicioService.listar()) {
                            System.out.println(servicio);
                        }
                    }
                    case 3 -> {
                        int codigo = consola.leerEntero("Código del servicio: ");
                        String nombre = consola.leerTextoObligatorio("Nuevo nombre: ");
                        servicioService.actualizar(codigo, nombre);
                        System.out.println("Servicio actualizado.");
                    }
                    case 4 -> {
                        int codigo = consola.leerEntero("Código del servicio: ");
                        String respuesta = consola.leerTexto("¿Activar (A) o desactivar (D)? ").toUpperCase();
                        if (respuesta.equals("A") || respuesta.equals("D")) {
                            servicioService.cambiarEstado(codigo, respuesta.equals("A"));
                            System.out.println("Estado actualizado.");
                        } else {
                            System.out.println("Respuesta inválida. Escriba A o D.");
                        }
                    }
                    case 0 -> volver = true;
                    default -> System.out.println("Opción inexistente.");
                }
            } catch (NegocioException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ---------- turnos ----------
    private void generarTurno() throws NegocioException {
        int codigoCliente = consola.leerEntero("Código del cliente: ");
        int codigoServicio = consola.leerEntero("Código del servicio: ");
        Turno turno = turnoService.generar(codigoCliente, codigoServicio);
        System.out.println("Turno generado: número " + turno.getNumero() + ".");
    }

    private void atenderSiguiente() throws NegocioException {
        Turno turno = turnoService.atenderSiguiente();
        Map<Integer, String> servicios = servicioService.mapaNombres();
        System.out.println("Atendiendo turno " + turno.getNumero());
        System.out.println("  Cliente : " + nombreCliente(turno.getClienteCodigo()));
        System.out.println("  Servicio: " + servicios.get(turno.getServicioCodigo()));
        System.out.println("  Atendido: " + Fechas.aTexto(turno.getFechaCierre()));
    }

    private void cancelarTurno() throws NegocioException {
        int numero = consola.leerEntero("Número del turno a cancelar: ");
        turnoService.cancelar(numero);
        System.out.println("Turno " + numero + " cancelado.");
    }

    private void mostrarPendientes() throws NegocioException {
        mostrarTurnos("TURNOS PENDIENTES (en orden de atención)", turnoService.listarPendientes());
    }

    private void historialCliente() throws NegocioException {
        int codigo = consola.leerEntero("Código del cliente: ");
        List<Turno> turnos = reporteService.historialCliente(codigo);
        mostrarTurnos("HISTORIAL DEL CLIENTE " + codigo, turnos);
    }

    private void mostrarTurnos(String titulo, List<Turno> turnos) throws NegocioException {
        System.out.println("\n--- " + titulo + " ---");
        if (turnos.isEmpty()) {
            System.out.println("(sin turnos)");
            return;
        }
        Map<Integer, String> servicios = servicioService.mapaNombres();
        for (Turno turno : turnos) {
            String cierre = "";
            if (turno.getEstado() == EstadoTurno.ATENDIDO) {
                cierre = " | Atención: " + Fechas.aTexto(turno.getFechaCierre());
            } else if (turno.getEstado() == EstadoTurno.CANCELADO) {
                cierre = " | Cancelación: " + Fechas.aTexto(turno.getFechaCierre());
            }
            System.out.printf("Turno %d | %s | %s | Generado: %s | %s%s%n",
                    turno.getNumero(),
                    nombreCliente(turno.getClienteCodigo()),
                    servicios.get(turno.getServicioCodigo()),
                    Fechas.aTexto(turno.getFechaGeneracion()),
                    turno.getEstado(), cierre);
        }
    }

    private void resumenPorServicio() throws NegocioException {
        System.out.println("\n--- RESUMEN POR SERVICIO ---");
        System.out.printf("%-26s %10s %10s %11s %6s%n", "Servicio", "Pendientes", "Atendidos", "Cancelados", "Total");
        int pendientes = 0;
        int atendidos = 0;
        int cancelados = 0;
        for (ResumenServicio fila : reporteService.resumenPorServicio()) {
            System.out.printf("%-26s %10d %10d %11d %6d%n", fila.nombre(), fila.pendientes(),
                    fila.atendidos(), fila.cancelados(), fila.total());
            pendientes += fila.pendientes();
            atendidos += fila.atendidos();
            cancelados += fila.cancelados();
        }
        System.out.printf("%-26s %10d %10d %11d %6d%n", "Total", pendientes, atendidos, cancelados,
                pendientes + atendidos + cancelados);
    }

    private String nombreCliente(int codigo) {
        Cliente cliente = clienteService.buscar(codigo);
        return codigo + " - " + (cliente == null ? "?" : cliente.getNombre());
    }
}
