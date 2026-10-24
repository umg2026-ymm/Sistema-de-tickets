package oficina.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import oficina.modelo.EstadoTurno;
import oficina.modelo.ResumenServicio;
import oficina.modelo.Turno;
import oficina.util.Fechas;

public class TurnoDaoSqlite implements TurnoDao {

    private static final String COLUMNAS =
            "numero, cliente_codigo, servicio_codigo, fecha_generacion, estado, fecha_cierre";

    @Override
    public int insertar(int clienteCodigo, int servicioCodigo, LocalDateTime fechaGeneracion) throws SQLException {
        String sql = "INSERT INTO turnos (cliente_codigo, servicio_codigo, fecha_generacion, estado) "
                + "VALUES (?, ?, ?, 'PENDIENTE')";
        try (Connection con = ConexionSqlite.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clienteCodigo);
            ps.setInt(2, servicioCodigo);
            ps.setString(3, Fechas.aTexto(fechaGeneracion));
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
            }
            throw new SQLException("No se obtuvo el número del turno generado.");
        }
    }

    @Override
    public boolean cerrar(int numero, EstadoTurno nuevoEstado, LocalDateTime fecha) throws SQLException {
        String sql = "UPDATE turnos SET estado = ?, fecha_cierre = ? WHERE numero = ? AND estado = 'PENDIENTE'";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, Fechas.aTexto(fecha));
            ps.setInt(3, numero);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Turno buscarPorNumero(int numero) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM turnos WHERE numero = ?";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Turno> listarPorEstado(EstadoTurno estado) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM turnos WHERE estado = ? ORDER BY numero";
        List<Turno> turnos = new ArrayList<>();
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    turnos.add(mapear(rs));
                }
            }
        }
        return turnos;
    }

    @Override
    public List<Turno> listarPorCliente(int clienteCodigo) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM turnos WHERE cliente_codigo = ? ORDER BY numero";
        List<Turno> turnos = new ArrayList<>();
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteCodigo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    turnos.add(mapear(rs));
                }
            }
        }
        return turnos;
    }

    @Override
    public boolean clienteTienePendiente(int clienteCodigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM turnos WHERE cliente_codigo = ? AND estado = 'PENDIENTE'";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteCodigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public int contarPendientesDeServicio(int servicioCodigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM turnos WHERE servicio_codigo = ? AND estado = 'PENDIENTE'";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, servicioCodigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public List<ResumenServicio> resumenPorServicio() throws SQLException {
        // LEFT JOIN: los servicios sin turnos también aparecen, con ceros
        String sql = "SELECT s.codigo, s.nombre, "
                + "COALESCE(SUM(CASE WHEN t.estado = 'PENDIENTE' THEN 1 ELSE 0 END), 0) AS pendientes, "
                + "COALESCE(SUM(CASE WHEN t.estado = 'ATENDIDO'  THEN 1 ELSE 0 END), 0) AS atendidos, "
                + "COALESCE(SUM(CASE WHEN t.estado = 'CANCELADO' THEN 1 ELSE 0 END), 0) AS cancelados "
                + "FROM servicios s LEFT JOIN turnos t ON t.servicio_codigo = s.codigo "
                + "GROUP BY s.codigo, s.nombre ORDER BY s.codigo";
        List<ResumenServicio> resumen = new ArrayList<>();
        try (Connection con = ConexionSqlite.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resumen.add(new ResumenServicio(rs.getInt("codigo"), rs.getString("nombre"),
                        rs.getInt("pendientes"), rs.getInt("atendidos"), rs.getInt("cancelados")));
            }
        }
        return resumen;
    }

    private Turno mapear(ResultSet rs) throws SQLException {
        return new Turno(rs.getInt("numero"), rs.getInt("cliente_codigo"), rs.getInt("servicio_codigo"),
                Fechas.deTexto(rs.getString("fecha_generacion")),
                EstadoTurno.valueOf(rs.getString("estado")),
                Fechas.deTexto(rs.getString("fecha_cierre")));
    }
}
