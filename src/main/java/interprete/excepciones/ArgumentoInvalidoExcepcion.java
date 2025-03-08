package interprete.excepciones;

/**
 * Excepción base para errores relacionados con argumentos inválidos
 * durante la evaluación de expresiones.
 */
public class ArgumentoInvalidoExcepcion extends Exception {
    /**
     * Construye una nueva excepción con el mensaje de error especificado
     * @param mensaje Descripción del error
     */
    public ArgumentoInvalidoExcepcion(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Construye una nueva excepción con el mensaje y la causa subyacente
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public ArgumentoInvalidoExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}