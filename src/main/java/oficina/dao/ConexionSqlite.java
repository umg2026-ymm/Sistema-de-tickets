package oficina.dao;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Crea conexiones a data/turnos.db (ruta relativa) con claves foráneas habilitadas. */
public final class ConexionSqlite {
    public static final Path RUTA_BD = Path.of("data", "turnos.db");

    private ConexionSqlite() {
    }

    public static Connection obtener() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:sqlite:" + RUTA_BD.toString().replace('\\', '/'));
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            con.close();
            throw e;
        }
        return con;
    }
}
