package oficina.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oficina.modelo.Servicio;

public class ServicioDaoSqlite implements ServicioDao {

    @Override
    public void insertar(Servicio servicio) throws SQLException {
        String sql = "INSERT INTO servicios (codigo, nombre, activo) VALUES (?, ?, ?)";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, servicio.getCodigo());
            ps.setString(2, servicio.getNombre());
            ps.setInt(3, servicio.isActivo() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Servicio servicio) throws SQLException {
        String sql = "UPDATE servicios SET nombre = ?, activo = ? WHERE codigo = ?";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, servicio.getNombre());
            ps.setInt(2, servicio.isActivo() ? 1 : 0);
            ps.setInt(3, servicio.getCodigo());
            ps.executeUpdate();
        }
    }

    @Override
    public Servicio buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT codigo, nombre, activo FROM servicios WHERE codigo = ?";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Servicio(rs.getInt("codigo"), rs.getString("nombre"), rs.getInt("activo") == 1);
                }
            }
        }
        return null;
    }

    @Override
    public List<Servicio> listarTodos() throws SQLException {
        String sql = "SELECT codigo, nombre, activo FROM servicios ORDER BY codigo";
        List<Servicio> servicios = new ArrayList<>();
        try (Connection con = ConexionSqlite.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                servicios.add(new Servicio(rs.getInt("codigo"), rs.getString("nombre"), rs.getInt("activo") == 1));
            }
        }
        return servicios;
    }
}
