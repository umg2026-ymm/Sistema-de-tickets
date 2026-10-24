package oficina.modelo;

/** Fila del resumen por servicio. */
public record ResumenServicio(int codigo, String nombre, int pendientes, int atendidos, int cancelados) {
    public int total() {
        return pendientes + atendidos + cancelados;
    }
}
