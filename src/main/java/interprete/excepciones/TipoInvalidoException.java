package interprete.excepciones;

/**
 * Excepción lanzada cuando se intenta realizar una operación con un tipo de datos incorrecto.
 * Por ejemplo, realizar una operación aritmética con símbolos en lugar de números.
 */
public class TipoInvalidoException extends ArgumentoInvalidoExcepcion {
    /**
     * Construye una nueva excepción de tipo inválido con el mensaje especificado
     * @param mensaje Descripción del error de tipo
     */
    public TipoInvalidoException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Construye una nueva excepción con el mensaje y la causa subyacente
     * @param mensaje Descripción del error de tipo
     * @param causa Excepción que causó este error
     */
    public TipoInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}