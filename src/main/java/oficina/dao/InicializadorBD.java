package oficina.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Si data/turnos.db no existe, la crea ejecutando sql/crear_y_cargar.sql.
 * Si ya existe NO la toca, así los datos nunca se borran al iniciar.
 */
public final class InicializadorBD {
    private static final Path SCRIPT = Path.of("sql", "crear_y_cargar.sql");

    private InicializadorBD() {
    }

    public static void inicializarSiHaceFalta() throws IOException, SQLException {
        if (Files.exists(ConexionSqlite.RUTA_BD)) {
            return;
        }
        Files.createDirectories(ConexionSqlite.RUTA_BD.getParent());
        List<String> lineas = Files.readAllLines(SCRIPT);
        StringBuilder sql = new StringBuilder();
        for (String linea : lineas) {
            if (!linea.trim().startsWith("--")) {
                sql.append(linea).append('\n');
            }
        }
        try (Connection con = ConexionSqlite.obtener(); Statement st = con.createStatement()) {
            for (String instruccion : sql.toString().split(";")) {
                if (!instruccion.isBlank()) {
                    st.execute(instruccion);
                }
            }
        }
        System.out.println("Base de datos creada desde " + SCRIPT + ".");
    }
}
