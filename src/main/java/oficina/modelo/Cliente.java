package oficina.modelo;

public class Cliente {
    private final int codigo;
    private String nombre;
    private String telefono;
    private boolean activo;

    public Cliente(int codigo, String nombre, String telefono, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.activo = activo;
    }

    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("%-8d %-25s %-10s %s", codigo, nombre, telefono, activo ? "Activo" : "Inactivo");
    }
}
