package oficina.logica;

public final class Validaciones {
    private Validaciones() {
    }

    public static void codigoPositivo(int codigo) throws NegocioException {
        if (codigo <= 0) {
            throw new NegocioException("El código debe ser un número entero positivo.");
        }
    }

    public static String textoObligatorio(String valor, String campo) throws NegocioException {
        if (valor == null || valor.isBlank()) {
            throw new NegocioException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public static String telefono(String valor) throws NegocioException {
        if (valor == null || !valor.trim().matches("\\d{8}")) {
            throw new NegocioException("El teléfono debe tener exactamente 8 dígitos.");
        }
        return valor.trim();
    }
}
