package oficina.modelo;

import java.time.LocalDateTime;

public class Turno {
    private final int numero;
    private final int clienteCodigo;
    private final int servicioCodigo;
    private final LocalDateTime fechaGeneracion;
    private EstadoTurno estado;
    private LocalDateTime fechaCierre; // fecha de atención o cancelación

    public Turno(int numero, int clienteCodigo, int servicioCodigo, LocalDateTime fechaGeneracion,
                 EstadoTurno estado, LocalDateTime fechaCierre) {
        this.numero = numero;
        this.clienteCodigo = clienteCodigo;
        this.servicioCodigo = servicioCodigo;
        this.fechaGeneracion = fechaGeneracion;
        this.estado = estado;
        this.fechaCierre = fechaCierre;
    }

    public int getNumero() { return numero; }
    public int getClienteCodigo() { return clienteCodigo; }
    public int getServicioCodigo() { return servicioCodigo; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public EstadoTurno getEstado() { return estado; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }

    public void marcarAtendido(LocalDateTime fecha) {
        this.estado = EstadoTurno.ATENDIDO;
        this.fechaCierre = fecha;
    }

    public void marcarCancelado(LocalDateTime fecha) {
        this.estado = EstadoTurno.CANCELADO;
        this.fechaCierre = fecha;
    }
}
