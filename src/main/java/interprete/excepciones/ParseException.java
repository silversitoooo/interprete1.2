package interprete.excepciones;

/**
 * Excepción lanzada cuando hay un error en el análisis sintáctico
 */
public class ParseException extends RuntimeException {
    public ParseException(String mensaje) {
        super(mensaje);
    }
}