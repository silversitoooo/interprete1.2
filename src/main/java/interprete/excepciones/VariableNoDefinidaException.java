package interprete.excepciones;

/**
 * Excepción lanzada cuando se intenta acceder a una variable que no ha sido definida
 * en el ambiente actual de evaluación.
 */
public class VariableNoDefinidaException extends ArgumentoInvalidoExcepcion {
    /**
     * Construye una nueva excepción para variables no definidas
     * @param mensaje Descripción del error, típicamente el nombre de la variable no definida
     */
    public VariableNoDefinidaException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Construye una nueva excepción con el mensaje y la causa subyacente
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public VariableNoDefinidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}