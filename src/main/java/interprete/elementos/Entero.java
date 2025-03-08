package interprete.elementos;

import java.io.PrintStream;
import java.util.HashMap;

public class Entero extends ElementoAtomico {
    private static HashMap<Long, Entero> tablaEnteros = new HashMap<>();
    public static final Entero CERO = crear(0);
    public static final Entero UNO = crear(1);
    private long valor;

    private Entero(long valor) {
        this.valor = valor;
    }

    public static Entero crear(long valor) {
        return tablaEnteros.computeIfAbsent(valor, Entero::new);
    }

    @Override
    public boolean esNumerico() {
        return true;
    }

    @Override
    public void imprimir(PrintStream flujoSalida) {
        flujoSalida.print(valor);
    }

    public long valor() {
        return this.valor;
    }
}
