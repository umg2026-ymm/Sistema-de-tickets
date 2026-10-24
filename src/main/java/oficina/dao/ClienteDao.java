package oficina.dao;

import java.sql.SQLException;
import java.util.List;
import oficina.modelo.Cliente;

public interface ClienteDao {
    void insertar(Cliente cliente) throws SQLException;

    /** Actualiza nombre, teléfono y estado (el código no se modifica). */
    void actualizar(Cliente cliente) throws SQLException;

    List<Cliente> listarTodos() throws SQLException;
}
