package interprete.elementos;



import interprete.excepciones.ArgumentoInvalidoExcepcion;

import java.io.PrintStream;
import java.util.Iterator;




import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class ElementoBase implements Iterable<ElementoBase> {

    public abstract ElementoBase primero() throws ArgumentoInvalidoExcepcion;

    public abstract ElementoBase resto() throws ArgumentoInvalidoExcepcion;

    public boolean esAtomico() {
        return false;
    }

    public boolean esSimbolo() {
        return false;
    }

    public boolean esNumerico() {
        return false;
    }

    public boolean esTexto() {
        return false;
    }

    public abstract void imprimir(PrintStream flujoSalida);

    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        this.imprimir(ps);
        ps.close();
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    public Stream<ElementoBase> flujo() {
        return StreamSupport.stream(this.spliterator(), false);
    }
}
