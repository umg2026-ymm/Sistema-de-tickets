package oficina.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Conversión de fechas entre LocalDateTime y el texto guardado en SQLite. */
public final class Fechas {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Fechas() {
    }

    public static LocalDateTime ahora() {
        return LocalDateTime.now().withNano(0);
    }

    public static String aTexto(LocalDateTime fecha) {
        return fecha == null ? null : fecha.format(FORMATO);
    }

    public static LocalDateTime deTexto(String texto) {
        return texto == null ? null : LocalDateTime.parse(texto, FORMATO);
    }
}
