package interprete;

import interprete.elementos.*;
import interprete.excepciones.*;
import interprete.util.LectorEvaluador;
import interprete.primitivas.FuncionAplicable;
import interprete.primitivas.PrimitivasRegistro;

import java.io.*;
import java.util.*;

/**
 * Clase principal del intérprete que gestiona la evaluación de expresiones
 */
public class Interprete {
    private Map<Simbolo, ElementoBase> ambiente;
    private PrimitivasRegistro registroPrimitivas;
    private LectorEvaluador lector;
    private PrintStream salida;

    /**
     * Constructor. Inicializa el intérprete con el ambiente global vacío
     */
    public Interprete() {
        this(System.out);
    }

    /**
     * Constructor con flujo de salida personalizable
     * @param flujoSalida Flujo donde se imprimirán los resultados
     */
    public Interprete(PrintStream flujoSalida) {
        this.ambiente = new HashMap<>();
        this.registroPrimitivas = new PrimitivasRegistro(this);
        this.lector = new LectorEvaluador();
        this.salida = flujoSalida;

        // Registrar todas las primitivas en el ambiente global
        registroPrimitivas.registrarTodasLasPrimitivas(ambiente);
    }

    /**
     * Método para convertir una cadena de texto en una estructura de ElementoBase
     */
    public ElementoBase leer(String expresion) {
        return lector.leer(expresion);
    }

    /**
     * Evalúa una expresión en el contexto del ambiente actual
     * @param expresion La expresión a evaluar
     * @return El resultado de la evaluación
     */
    public ElementoBase evaluar(ElementoBase expresion) {
        try {
            return evaluarInterno(expresion, ambiente);
        } catch (ArgumentoInvalidoExcepcion e) {
            System.err.println("Error: " + e.getMessage());
            return Simbolo.simbolo("ERROR");
        }
    }

    /**
     * Método interno para evaluación recursiva de expresiones
     */
    private ElementoBase evaluarInterno(ElementoBase expresion, Map<Simbolo, ElementoBase> ambienteActual)
            throws ArgumentoInvalidoExcepcion {

        // Si es un átomo
        if (expresion.esAtomico()) {
            // Si es un símbolo, buscar su valor en el ambiente
            if (expresion.esSimbolo()) {
                Simbolo simbolo = (Simbolo) expresion;

                // Casos especiales: nil, t
                if (simbolo == Simbolo.VACIO || simbolo == Simbolo.VERDADERO) {
                    return simbolo;
                }

                // Buscar en el ambiente
                ElementoBase valor = ambienteActual.get(simbolo);
                if (valor == null) {
                    throw new VariableNoDefinidaException("Variable no definida: " + simbolo);
                }
                return valor;
            }
            // Si es un literal (número, texto), devolver tal cual
            return expresion;
        }

        // Si no es una lista vacía, evaluar como una expresión compuesta
        if (expresion != Simbolo.VACIO) {
            ElementoBase operador = expresion.primero();
            ElementoBase args = expresion.resto();

            // Evaluar formas especiales
            if (operador.esSimbolo()) {
                Simbolo simbolo = (Simbolo) operador;

                // SI (condicional)
                if (simbolo == Simbolo.SI) {
                    return evaluarSi(args, ambienteActual);
                }

                // ASIGNAR (definición de variable)
                else if (simbolo == Simbolo.ASIGNAR) {
                    return evaluarAsignar(args, ambienteActual);
                }

                // DEF_FUNCION (definición de función)
                else if (simbolo == Simbolo.DEF_FUNCION) {
                    return evaluarDefinirFuncion(args, ambienteActual);
                }

                // CITAR (quote)
                else if (simbolo == Simbolo.CITAR) {
                    if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
                        throw new ArgumentoInvalidoExcepcion("QUOTE requiere exactamente un argumento");
                    }
                    return args.primero();
                }
            }

            // Para otras expresiones, evaluar operador y argumentos
            ElementoBase operadorEvaluado = evaluarInterno(operador, ambienteActual);

            // Verificar si el operador es aplicable como función
            if (!(operadorEvaluado instanceof FuncionAplicable)) {
                throw new ArgumentoInvalidoExcepcion("El operador no es una función: " + operadorEvaluado);
            }

            // Evaluar argumentos
            ElementoBase argumentosEvaluados = evaluarArgumentos(args, ambienteActual);

            // Aplicar función
            return ((FuncionAplicable) operadorEvaluado).aplicar(argumentosEvaluados, this);
        }

        // Lista vacía
        return expresion;
    }

    /**
     * Evalúa una expresión condicional (SI)
     */
    private ElementoBase evaluarSi(ElementoBase args, Map<Simbolo, ElementoBase> ambienteActual)
            throws ArgumentoInvalidoExcepcion {

        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("SI requiere al menos dos argumentos");
        }

        ElementoBase condicion = args.primero();
        ElementoBase ramaVerdadera = args.resto().primero();
        ElementoBase ramaFalsa = args.resto().resto() != Simbolo.VACIO ?
                args.resto().resto().primero() : Simbolo.VACIO;

        ElementoBase resultadoCondicion = evaluarInterno(condicion, ambienteActual);

        // En Lisp, todo excepto nil se considera verdadero
        if (resultadoCondicion != Simbolo.VACIO &&
                !(resultadoCondicion.esNumerico() && ((Entero)resultadoCondicion).valor() == 0)) {
            return evaluarInterno(ramaVerdadera, ambienteActual);
        } else {
            return evaluarInterno(ramaFalsa, ambienteActual);
        }
    }

    /**
     * Evalúa una asignación de variable (ASIGNAR)
     */
    private ElementoBase evaluarAsignar(ElementoBase args, Map<Simbolo, ElementoBase> ambienteActual)
            throws ArgumentoInvalidoExcepcion {

        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO || args.resto().resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("ASIGNAR requiere exactamente dos argumentos");
        }

        if (!args.primero().esSimbolo()) {
            throw new TipoInvalidoException("El primer argumento de ASIGNAR debe ser un símbolo");
        }

        Simbolo nombre = (Simbolo) args.primero();
        ElementoBase valor = evaluarInterno(args.resto().primero(), ambienteActual);

        ambienteActual.put(nombre, valor);
        return valor;
    }

    /**
     * Evalúa una definición de función (DEF_FUNCION)
     */
    private ElementoBase evaluarDefinirFuncion(ElementoBase args, Map<Simbolo, ElementoBase> ambienteActual)
            throws ArgumentoInvalidoExcepcion {

        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO || args.resto().resto() == Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("DEF_FUNCION requiere al menos tres argumentos");
        }

        if (!args.primero().esSimbolo()) {
            throw new TipoInvalidoException("El nombre de la función debe ser un símbolo");
        }

        Simbolo nombreFuncion = (Simbolo) args.primero();
        ElementoBase parametros = args.resto().primero();
        ElementoBase cuerpo = args.resto().resto();

        FuncionUsuario funcion = new FuncionUsuario(nombreFuncion, parametros, cuerpo, ambienteActual);
        ambienteActual.put(nombreFuncion, funcion);

        return nombreFuncion;
    }

    /**
     * Evalúa todos los argumentos de una lista
     */
    private ElementoBase evaluarArgumentos(ElementoBase args, Map<Simbolo, ElementoBase> ambienteActual)
            throws ArgumentoInvalidoExcepcion {

        if (args == Simbolo.VACIO) {
            return Simbolo.VACIO;
        }

        ElementoBase primero = evaluarInterno(args.primero(), ambienteActual);
        ElementoBase resto = evaluarArgumentos(args.resto(), ambienteActual);

        return new Enlace(primero, resto);
    }

    /**
     * Obtiene el flujo de salida del intérprete
     */
    public PrintStream getSalida() {
        return salida;
    }

    /**
     * Inicia el modo interactivo del intérprete (Read-Eval-Print Loop)
     */
    public void iniciarREPL() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String linea = scanner.nextLine().trim();

            if (linea.equalsIgnoreCase("salir")) {
                break;
            }

            if (!linea.isEmpty()) {
                try {
                    ElementoBase expresion = lector.leer(linea);
                    ElementoBase resultado = evaluar(expresion);

                    if (resultado != null) {
                        resultado.imprimir(getSalida());
                        getSalida().println();
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        System.out.println("Sesión finalizada.");
    }

    /**
     * Clase interna para representar funciones definidas por el usuario
     */
    private class FuncionUsuario extends ElementoBase implements FuncionAplicable {
        private Simbolo nombre;
        private ElementoBase parametros;
        private ElementoBase cuerpo;
        private Map<Simbolo, ElementoBase> ambientePadre;

        public FuncionUsuario(Simbolo nombre, ElementoBase parametros, ElementoBase cuerpo,
                              Map<Simbolo, ElementoBase> ambientePadre) {
            this.nombre = nombre;
            this.parametros = parametros;
            this.cuerpo = cuerpo;
            this.ambientePadre = ambientePadre;
        }

        @Override
        public ElementoBase aplicar(ElementoBase argumentos, Interprete interprete)
                throws ArgumentoInvalidoExcepcion {

            // Crear un nuevo ambiente extendiendo el ambiente léxico
            Map<Simbolo, ElementoBase> ambienteLocal = new HashMap<>(ambientePadre);

            // Vincular argumentos a parámetros
            ElementoBase parametroActual = parametros;
            ElementoBase argumentoActual = argumentos;

            while (parametroActual != Simbolo.VACIO && argumentoActual != Simbolo.VACIO) {
                if (!parametroActual.primero().esSimbolo()) {
                    throw new TipoInvalidoException("Los parámetros deben ser símbolos");
                }

                Simbolo parametro = (Simbolo) parametroActual.primero();
                ElementoBase argumento = argumentoActual.primero();

                ambienteLocal.put(parametro, argumento);

                parametroActual = parametroActual.resto();
                argumentoActual = argumentoActual.resto();
            }

            // Verificar que no haya más parámetros que argumentos
            if (parametroActual != Simbolo.VACIO) {
                throw new ArgumentoInvalidoExcepcion("Faltan argumentos para la función");
            }

            // Evaluar cada expresión del cuerpo
            ElementoBase resultado = Simbolo.VACIO;
            ElementoBase expresionActual = cuerpo;

            while (expresionActual != Simbolo.VACIO) {
                resultado = interprete.evaluarInterno(expresionActual.primero(), ambienteLocal);
                expresionActual = expresionActual.resto();
            }

            return resultado;
        }

        @Override
        public ElementoBase primero() throws ArgumentoInvalidoExcepcion {
            return null;
        }

        @Override
        public ElementoBase resto() throws ArgumentoInvalidoExcepcion {
            return null;
        }

        @Override
        public void imprimir(PrintStream flujoSalida) {

        }

        @Override
        public Iterator<ElementoBase> iterator() {
            return null;
        }
    }
}