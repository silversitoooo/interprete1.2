package interprete.primitivas;

import interprete.Interprete;
import interprete.elementos.*;
import interprete.excepciones.*;

import java.io.PrintStream;
import java.util.*;

/**
 * Gestiona el registro y la implementación de las funciones primitivas
 */
public class PrimitivasRegistro {
    private Interprete interprete;
    
    public PrimitivasRegistro(Interprete interprete) {
        this.interprete = interprete;
    }
    
    /**
     * Registra todas las primitivas en el ambiente
     */
    public void registrarTodasLasPrimitivas(Map<Simbolo, ElementoBase> ambiente) {
        // Operaciones aritméticas
        registrarPrimitiva(ambiente, Simbolo.SUMAR, new Primitiva(this::sumar, Simbolo.SUMAR));
        registrarPrimitiva(ambiente, Simbolo.RESTAR, new Primitiva(this::restar, Simbolo.RESTAR));
        registrarPrimitiva(ambiente, Simbolo.MULTIPLICAR, new Primitiva(this::multiplicar, Simbolo.MULTIPLICAR));
        registrarPrimitiva(ambiente, Simbolo.DIVIDIR, new Primitiva(this::dividir, Simbolo.DIVIDIR));
        
        // Operaciones lógicas
        registrarPrimitiva(ambiente, Simbolo.Y, new Primitiva(this::y, Simbolo.Y));
        registrarPrimitiva(ambiente, Simbolo.O, new Primitiva(this::o, Simbolo.O));
        registrarPrimitiva(ambiente, Simbolo.NO, new Primitiva(this::no, Simbolo.NO));
        
        // Comparaciones
        registrarPrimitiva(ambiente, Simbolo.IGUAL_NUM, new Primitiva(this::igual, Simbolo.IGUAL_NUM));
        registrarPrimitiva(ambiente, Simbolo.MENOR, new Primitiva(this::menor, Simbolo.MENOR));
        registrarPrimitiva(ambiente, Simbolo.MAYOR, new Primitiva(this::mayor, Simbolo.MAYOR));
        
        // Operaciones de lista
        registrarPrimitiva(ambiente, Simbolo.PRIMERO, new Primitiva(this::primero, Simbolo.PRIMERO));
        registrarPrimitiva(ambiente, Simbolo.RESTO, new Primitiva(this::resto, Simbolo.RESTO));
        registrarPrimitiva(ambiente, Simbolo.ENLAZAR, new Primitiva(this::enlazar, Simbolo.ENLAZAR));
        registrarPrimitiva(ambiente, Simbolo.LISTA, new Primitiva(this::lista, Simbolo.LISTA));
        
        // Verificación de tipos
        registrarPrimitiva(ambiente, Simbolo.NULO, new Primitiva(this::esNulo, Simbolo.NULO));
        registrarPrimitiva(ambiente, Simbolo.ES_LISTA, new Primitiva(this::esLista, Simbolo.ES_LISTA));
        registrarPrimitiva(ambiente, Simbolo.ES_SIMBOLO, new Primitiva(this::esSimbolo, Simbolo.ES_SIMBOLO));
        registrarPrimitiva(ambiente, Simbolo.ES_NUMERICO, new Primitiva(this::esNumerico, Simbolo.ES_NUMERICO));
        
        // E/S
        registrarPrimitiva(ambiente, Simbolo.IMPRIMIR, new Primitiva(this::imprimir, Simbolo.IMPRIMIR));
    }
    
    /**
     * Registra una primitiva en el ambiente
     */
    private void registrarPrimitiva(Map<Simbolo, ElementoBase> ambiente, Simbolo simbolo, Primitiva funcion) {
        ambiente.put(simbolo, funcion);
    }
    
    /**
     * Implementaciones de las primitivas
     */
    private ElementoBase sumar(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        long resultado = 0;
        
        for (ElementoBase arg : args) {
            if (!arg.esNumerico()) {
                throw new TipoInvalidoException("Los argumentos de + deben ser números");
            }
            resultado += ((Entero) arg).valor();
        }
        
        return Entero.crear(resultado);
    }
    
    private ElementoBase restar(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("- requiere al menos un argumento");
        }
        
        if (!args.primero().esNumerico()) {
            throw new TipoInvalidoException("Los argumentos de - deben ser números");
        }
        
        long resultado = ((Entero) args.primero()).valor();
        
        // Si solo hay un argumento, devuelve su negativo
        if (args.resto() == Simbolo.VACIO) {
            return Entero.crear(-resultado);
        }
        
        // Si hay más argumentos, resta cada uno
        ElementoBase resto = args.resto();
        for (ElementoBase arg : resto) {
            if (!arg.esNumerico()) {
                throw new TipoInvalidoException("Los argumentos de - deben ser números");
            }
            resultado -= ((Entero) arg).valor();
        }
        
        return Entero.crear(resultado);
    }
    
    private ElementoBase multiplicar(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        long resultado = 1;
        
        for (ElementoBase arg : args) {
            if (!arg.esNumerico()) {
                throw new TipoInvalidoException("Los argumentos de * deben ser números");
            }
            resultado *= ((Entero) arg).valor();
        }
        
        return Entero.crear(resultado);
    }
    
    private ElementoBase dividir(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("/ requiere al menos dos argumentos");
        }
        
        if (!args.primero().esNumerico() || !args.resto().primero().esNumerico()) {
            throw new TipoInvalidoException("Los argumentos de / deben ser números");
        }
        
        long numerador = ((Entero) args.primero()).valor();
        long denominador = ((Entero) args.resto().primero()).valor();
        
        if (denominador == 0) {
            throw new ArithmeticException("División por cero");
        }
        
        return Entero.crear(numerador / denominador);
    }
    
    private ElementoBase y(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        for (ElementoBase arg : args) {
            if (arg == Simbolo.VACIO || (arg.esNumerico() && ((Entero)arg).valor() == 0)) {
                return Entero.CERO;
            }
        }
        return Simbolo.VERDADERO;
    }
    
    private ElementoBase o(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        for (ElementoBase arg : args) {
            if (arg != Simbolo.VACIO && !(arg.esNumerico() && ((Entero)arg).valor() == 0)) {
                return Simbolo.VERDADERO;
            }
        }
        return Entero.CERO;
    }
    
    private ElementoBase no(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("NO requiere exactamente un argumento");
        }
        
        ElementoBase arg = args.primero();
        if (arg == Simbolo.VACIO || (arg.esNumerico() && ((Entero)arg).valor() == 0)) {
            return Simbolo.VERDADERO;
        } else {
            return Entero.CERO;
        }
    }
    
    private ElementoBase igual(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("= requiere al menos dos argumentos");
        }
        
        if (!args.primero().esNumerico() || !args.resto().primero().esNumerico()) {
            throw new TipoInvalidoException("Los argumentos de = deben ser números");
        }
        
        long valor = ((Entero) args.primero()).valor();
        ElementoBase resto = args.resto();
        
        for (ElementoBase arg : resto) {
            if (!arg.esNumerico() || ((Entero) arg).valor() != valor) {
                return Entero.CERO;
            }
        }
        
        return Simbolo.VERDADERO;
    }
    
    private ElementoBase menor(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("< requiere al menos dos argumentos");
        }
        
        if (!args.primero().esNumerico() || !args.resto().primero().esNumerico()) {
            throw new TipoInvalidoException("Los argumentos de < deben ser números");
        }
        
        long anterior = ((Entero) args.primero()).valor();
        ElementoBase actual = args.resto();
        
        while (actual != Simbolo.VACIO) {
            if (!actual.primero().esNumerico()) {
                throw new TipoInvalidoException("Los argumentos de < deben ser números");
            }
            
            long valorActual = ((Entero) actual.primero()).valor();
            if (anterior >= valorActual) {
                return Entero.CERO;
            }
            
            anterior = valorActual;
            actual = actual.resto();
        }
        
        return Simbolo.VERDADERO;
    }
    
    private ElementoBase mayor(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("> requiere al menos dos argumentos");
        }
        
        if (!args.primero().esNumerico() || !args.resto().primero().esNumerico()) {
            throw new TipoInvalidoException("Los argumentos de > deben ser números");
        }
        
        long anterior = ((Entero) args.primero()).valor();
        ElementoBase actual = args.resto();
        
        while (actual != Simbolo.VACIO) {
            if (!actual.primero().esNumerico()) {
                throw new TipoInvalidoException("Los argumentos de > deben ser números");
            }
            
            long valorActual = ((Entero) actual.primero()).valor();
            if (anterior <= valorActual) {
                return Entero.CERO;
            }
            
            anterior = valorActual;
            actual = actual.resto();
        }
        
        return Simbolo.VERDADERO;
    }
    
    private ElementoBase primero(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("PRIMERO requiere exactamente un argumento");
        }
        
        ElementoBase lista = args.primero();
        if (lista.esAtomico()) {
            throw new TipoInvalidoException("PRIMERO requiere una lista como argumento");
        }
        
        return lista.primero();
    }
    
    private ElementoBase resto(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("RESTO requiere exactamente un argumento");
        }
        
        ElementoBase lista = args.primero();
        if (lista.esAtomico()) {
            throw new TipoInvalidoException("RESTO requiere una lista como argumento");
        }
        
        return lista.resto();
    }
    
    private ElementoBase enlazar(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() == Simbolo.VACIO || args.resto().resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("ENLAZAR requiere exactamente dos argumentos");
        }
        
        ElementoBase primero = args.primero();
        ElementoBase segundo = args.resto().primero();
        
        return new Enlace(primero, segundo);
    }
    
    private ElementoBase lista(ElementoBase args) {
        ElementoBase resultado = Simbolo.VACIO;
        
        // Construir la lista desde atrás hacia adelante
        ElementoBase actual = args;
        Stack<ElementoBase> pila = new Stack<>();
        
        while (actual != Simbolo.VACIO) {
            try {
                pila.push(actual.primero());
                actual = actual.resto();
            } catch (ArgumentoInvalidoExcepcion e) {
                break;
            }
        }
        
        // Construir la lista en orden correcto
        while (!pila.empty()) {
            resultado = new Enlace(pila.pop(), resultado);
        }
        
        return resultado;
    }
    
    private ElementoBase esNulo(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("NULO? requiere exactamente un argumento");
        }
        
        return args.primero() == Simbolo.VACIO ? Simbolo.VERDADERO : Entero.CERO;
    }
    
    private ElementoBase esLista(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("LISTA? requiere exactamente un argumento");
        }
        
        return !args.primero().esAtomico() || args.primero() == Simbolo.VACIO 
               ? Simbolo.VERDADERO : Entero.CERO;
    }
    
    private ElementoBase esSimbolo(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("SIMBOLO? requiere exactamente un argumento");
        }
        
        return args.primero().esSimbolo() ? Simbolo.VERDADERO : Entero.CERO;
    }
    
    private ElementoBase esNumerico(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        if (args == Simbolo.VACIO || args.resto() != Simbolo.VACIO) {
            throw new ArgumentoInvalidoExcepcion("NUMERO? requiere exactamente un argumento");
        }
        
        return args.primero().esNumerico() ? Simbolo.VERDADERO : Entero.CERO;
    }
    
    private ElementoBase imprimir(ElementoBase args) throws ArgumentoInvalidoExcepcion {
        PrintStream salida = interprete.getSalida();
        
        for (ElementoBase arg : args) {
            arg.imprimir(salida);
            salida.print(" ");
        }
        salida.println();
        
        return args == Simbolo.VACIO ? Simbolo.VACIO : args.primero();
    }
    
    /**
     * Interfaz para implementaciones de funciones
     */
    @FunctionalInterface
    interface FuncionImplementacion {
        ElementoBase ejecutar(ElementoBase args) throws ArgumentoInvalidoExcepcion;
    }
    
    /**
     * Clase para funciones primitivas aplicables
     */
    public class Primitiva extends ElementoBase implements FuncionAplicable {
        private FuncionImplementacion funcion;
        private Simbolo nombre;
        
        public Primitiva(FuncionImplementacion funcion, Simbolo nombre) {
            this.funcion = funcion;
            this.nombre = nombre;
        }
        
        @Override
        public ElementoBase aplicar(ElementoBase argumentos, Interprete interprete) 
                throws ArgumentoInvalidoExcepcion {
            return funcion.ejecutar(argumentos);
        }
        
        @Override
        public ElementoBase primero() throws ArgumentoInvalidoExcepcion {
            throw new ArgumentoInvalidoExcepcion("No se puede obtener el primer elemento de una función primitiva");
        }
        
        @Override
        public ElementoBase resto() throws ArgumentoInvalidoExcepcion {
            throw new ArgumentoInvalidoExcepcion("No se puede obtener el resto de una función primitiva");
        }
        
        @Override
        public void imprimir(PrintStream flujoSalida) {
            flujoSalida.print("#<primitiva:");
            nombre.imprimir(flujoSalida);
            flujoSalida.print(">");
        }
        
        @Override
        public Iterator<ElementoBase> iterator() {
            return Collections.emptyIterator();
        }
    }
}