package interprete.elementos;

import interprete.excepciones.ArgumentoInvalidoExcepcion;

import java.io.PrintStream;
import java.util.Iterator;

import static interprete.elementos.Simbolo.VACIO;

public class Enlace extends ElementoBase {
    private ElementoBase cabeza;
    private ElementoBase cola;

    public Enlace(ElementoBase cabeza, ElementoBase cola) {
        this.cabeza = cabeza;
        this.cola = cola;
    }

    @Override
    public ElementoBase primero() {
        return cabeza;
    }

    @Override
    public ElementoBase resto() {
        return cola;
    }

    @Override
    public void imprimir(PrintStream flujoSalida) {
        flujoSalida.print("(");
        ElementoBase siguiente = this;
        do {
            ((Enlace)siguiente).primero().imprimir(flujoSalida);
            siguiente = ((Enlace)siguiente).resto();
            if (siguiente.esAtomico()) {
                if (siguiente != VACIO) {
                    flujoSalida.print(" . ");
                    siguiente.imprimir(flujoSalida);
                }
                flujoSalida.print(")");
            } else {
                flujoSalida.print(" ");
            }
        } while (!siguiente.esAtomico());
    }

    @Override
    public Iterator<ElementoBase> iterator() {
        return new IteradorLista(this);
    }

    class IteradorLista implements Iterator<ElementoBase> {
        private ElementoBase actual;

        IteradorLista(Enlace lista) {
            this.actual = lista;
        }

        @Override
        public boolean hasNext() {
            return (!actual.esAtomico());
        }

        @Override
        public ElementoBase next() {
            ElementoBase siguiente = null;
            try {
                siguiente = actual.primero();
                actual = actual.resto();
            } catch (ArgumentoInvalidoExcepcion e) {
                return null;
            }
            return siguiente;
        }
    }
}
