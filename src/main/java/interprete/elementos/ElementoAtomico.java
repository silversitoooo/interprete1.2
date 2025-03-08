package interprete.elementos;

import interprete.excepciones.ArgumentoInvalidoExcepcion;

import java.util.Iterator;

public abstract class ElementoAtomico extends ElementoBase {

    @Override
    public boolean esAtomico() {
        return true;
    }

    @Override
    public ElementoBase primero() throws ArgumentoInvalidoExcepcion {
        throw new ArgumentoInvalidoExcepcion("No se puede obtener el primer elemento de un átomo: " + this.toString());
    }

    @Override
    public ElementoBase resto() throws ArgumentoInvalidoExcepcion {
        throw new ArgumentoInvalidoExcepcion("No se puede obtener el resto de un átomo: " + this.toString());
    }

    @Override
    public Iterator<ElementoBase> iterator() {
        return null;
    }
}
