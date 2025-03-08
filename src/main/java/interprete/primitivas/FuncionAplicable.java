package interprete.primitivas;

import interprete.Interprete;
import interprete.elementos.ElementoBase;
import interprete.excepciones.ArgumentoInvalidoExcepcion;

/**
 * Interfaz para elementos que pueden ser aplicados como funciones
 */
public interface FuncionAplicable {
    /**
     * Aplica la función a una lista de argumentos
     * @param argumentos Los argumentos para la función
     * @param interprete El intérprete que ejecuta la función
     * @return El resultado de aplicar la función
     */
    ElementoBase aplicar(ElementoBase argumentos, Interprete interprete) 
            throws ArgumentoInvalidoExcepcion;
}