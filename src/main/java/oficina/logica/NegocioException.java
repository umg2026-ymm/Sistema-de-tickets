package oficina.logica;

/** Error de validación o de regla de negocio; su mensaje se muestra al usuario. */
public class NegocioException extends Exception {
    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
