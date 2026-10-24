package oficina.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oficina.modelo.Cliente;

public class ClienteDaoSqlite implements ClienteDao {

    @Override
    public void insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (codigo, nombre, telefono, activo) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cliente.getCodigo());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTelefono());
            ps.setInt(4, cliente.isActivo() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, activo = ? WHERE codigo = ?";
        try (Connection con = ConexionSqlite.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setInt(3, cliente.isActivo() ? 1 : 0);
            ps.setInt(4, cliente.getCodigo());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT codigo, nombre, telefono, activo FROM clientes ORDER BY codigo";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection con = ConexionSqlite.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(rs.getInt("codigo"), rs.getString("nombre"),
                        rs.getString("telefono"), rs.getInt("activo") == 1));
            }
        }
        return clientes;
    }
}
