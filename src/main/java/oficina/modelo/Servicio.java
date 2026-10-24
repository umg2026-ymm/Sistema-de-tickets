package oficina.modelo;

public class Servicio {
    private final int codigo;
    private String nombre;
    private boolean activo;

    public Servicio(int codigo, String nombre, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.activo = activo;
    }

    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("%-8d %-28s %s", codigo, nombre, activo ? "Activo" : "Inactivo");
    }
}
