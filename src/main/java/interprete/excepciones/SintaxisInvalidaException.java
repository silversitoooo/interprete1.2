package interprete.excepciones;

/**
 * Excepción para errores en formas especiales (si, definir, etc.)
 * que tienen una sintaxis específica requerida.
 */
public class SintaxisInvalidaException extends ArgumentoInvalidoExcepcion {
    private final String forma;
    
    /**
     * Construye una nueva excepción de sintaxis para formas especiales
     * @param forma El nombre de la forma especial mal utilizada
     * @param mensaje Descripción del error de sintaxis
     */
    public SintaxisInvalidaException(String forma, String mensaje) {
        super(mensaje);
        this.forma = forma;
    }
    
    /**
     * @return El nombre de la forma especial que causó el error
     */
    public String getForma() {
        return forma;
    }
}