package oficina.dao;

import java.sql.SQLException;
import java.util.List;
import oficina.modelo.Servicio;

public interface ServicioDao {
    void insertar(Servicio servicio) throws SQLException;

    void actualizar(Servicio servicio) throws SQLException;

    /** Devuelve el servicio o null si no existe. */
    Servicio buscarPorCodigo(int codigo) throws SQLException;

    List<Servicio> listarTodos() throws SQLException;
}
