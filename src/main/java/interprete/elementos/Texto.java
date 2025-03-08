package interprete.elementos;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.function.Consumer;

public class Texto extends ElementoAtomico {
    private static HashMap<String, Texto> tablaTextos = new HashMap<>();
    private String contenido;

    private Texto(String contenido) {
        this.contenido = contenido;
    }

    public static Texto crear(String contenido) {
        return tablaTextos.computeIfAbsent(contenido, Texto::new);
    }

    @Override
    public boolean esTexto() {
        return true;
    }

    @Override
    public void imprimir(PrintStream flujoSalida) {
        flujoSalida.print("\"" + contenido + "\"");
    }

    public String valor() {
        return this.contenido;
    }

    @Override
    public void forEach(Consumer<? super ElementoBase> action) {
        super.forEach(action);
    }
}
