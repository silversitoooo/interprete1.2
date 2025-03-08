package interprete.elementos;

import java.io.PrintStream;
import java.util.HashMap;

public class Simbolo extends ElementoAtomico {
    private static HashMap<String, Simbolo> tablaSímbolos = new HashMap<>();
    public static final Simbolo VACIO = simbolo("VACIO");
    public static final Simbolo VERDADERO = simbolo("VERDADERO");
    public static final Simbolo PRIMERO = simbolo("PRIMERO");
    public static final Simbolo RESTO = simbolo("RESTO");
    public static final Simbolo ENLAZAR = simbolo("ENLAZAR");
    public static final Simbolo LISTA = simbolo("LISTA");
    public static final Simbolo SI = simbolo("SI");
    public static final Simbolo CONDICION = simbolo("CONDICION");
    public static final Simbolo IGUAL = simbolo("IGUAL");
    public static final Simbolo NULO = simbolo("NULO");
    public static final Simbolo ATOMICO = simbolo("ATOMICO");
    public static final Simbolo ES_SIMBOLO = simbolo("ES_SIMBOLO");
    public static final Simbolo ES_NUMERICO = simbolo("ES_NUMERICO");
    public static final Simbolo FUNCION = simbolo("FUNCION");
    public static final Simbolo MACRO = simbolo("MACRO");
    public static final Simbolo CLAUSULA = simbolo("CLAUSULA");
    public static final Simbolo CITAR = simbolo("CITAR");
    public static final Simbolo CONTRACOMILLA = simbolo("CONTRACOMILLA");
    public static final Simbolo COMA = simbolo("COMA");
    public static final Simbolo COMA_ARROBA = simbolo("COMA_ARROBA");
    public static final Simbolo EVALUAR = simbolo("EVALUAR");
    public static final Simbolo APLICAR = simbolo("APLICAR");
    public static final Simbolo Y = simbolo("Y");
    public static final Simbolo O = simbolo("O");
    public static final Simbolo NO = simbolo("NO");
    public static final Simbolo SUMAR = simbolo("+");
    public static final Simbolo RESTAR = simbolo("-");
    public static final Simbolo MULTIPLICAR = simbolo("*");
    public static final Simbolo DIVIDIR_ENTERO = simbolo("DIV_ENTERO");
    public static final Simbolo DIVIDIR = simbolo("/");
    public static final Simbolo MODULO = simbolo("MOD");
    public static final Simbolo INCREMENTAR = simbolo("INCR");
    public static final Simbolo DECREMENTAR = simbolo("DECR");
    public static final Simbolo ES_CERO = simbolo("ES_CERO");
    public static final Simbolo MAYOR_QUE = simbolo("MAYOR");
    public static final Simbolo MENOR_QUE = simbolo("MENOR");
    public static final Simbolo ES_LISTA = simbolo("ES_LISTA");
    public static final Simbolo EQUIVALENTE = simbolo("EQUIVALENTE");
    public static final Simbolo MENOR = simbolo("<");
    public static final Simbolo MAYOR = simbolo(">");
    public static final Simbolo IGUAL_NUM = simbolo("=");
    public static final Simbolo IMPRIMIR = simbolo("IMPRIMIR");
    public static final Simbolo IMPRIMIR_SIMPLE = simbolo("IMPRIMIR_SIMPLE");
    public static final Simbolo IMPRIMIR_SI = simbolo("IMPRIMIR_SI");
    public static final Simbolo IMPRIMIR_SALVO = simbolo("IMPRIMIR_SALVO");
    public static final Simbolo SELECCIONAR = simbolo("SELECCIONAR");
    public static final Simbolo PARAMETROS = simbolo("*PARAMETROS*");
    public static final Simbolo DEF_FUNCION = simbolo("DEF_FUNCION");
    public static final Simbolo DEF_MACRO = simbolo("DEF_MACRO");
    public static final Simbolo ASIGNAR = simbolo("ASIGNAR");
    public static final Simbolo INDEFINIDO = simbolo("INDEFINIDO");
    public static final Simbolo DEPURAR = simbolo("DEPURAR");
    public static final Simbolo FIN_DEPURAR = simbolo("FIN_DEPURAR");
    public static final Simbolo CARGAR = simbolo("CARGAR");
    public static final Simbolo ERROR = simbolo("ERROR");
    private String identificador;

    private Simbolo(String identificador) {
        this.identificador = identificador.toUpperCase();
    }

    public static Simbolo simbolo(String identificador) {
        return tablaSímbolos.computeIfAbsent(identificador.toUpperCase(), Simbolo::new);
    }

    @Override
    public boolean esSimbolo() {
        return true;
    }

    @Override
    public void imprimir(PrintStream flujoSalida) {
        flujoSalida.print(identificador);
    }

    @Override
    public String toString() {
        return identificador;
    }

    public String getNombre() {
        return identificador;
    }
}
