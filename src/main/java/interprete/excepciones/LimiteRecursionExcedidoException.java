package interprete.excepciones;

/**
 * Excepción lanzada cuando se excede el límite de recursión en la evaluación de funciones.
 * Ayuda a prevenir desbordamientos de pila en caso de recursión infinita.
 */
public class LimiteRecursionExcedidoException extends ArgumentoInvalidoExcepcion {
    private final int profundidadAlcanzada;
    
    /**
     * Construye una nueva excepción de límite de recursión
     * @param mensaje Descripción del error
     * @param profundidad La profundidad de recursión alcanzada
     */
    public LimiteRecursionExcedidoException(String mensaje, int profundidad) {
        super(mensaje);
        this.profundidadAlcanzada = profundidad;
    }
    
    /**
     * @return La profundidad de recursión que se alcanzó antes de lanzar la excepción
     */
    public int getProfundidadAlcanzada() {
        return profundidadAlcanzada;
    }
}