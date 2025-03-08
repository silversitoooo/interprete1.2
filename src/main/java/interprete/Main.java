package interprete;

import interprete.elementos.ElementoBase;

/**
 * Clase principal para iniciar el interprete de Lisp
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Interprete de Lisp ===");
        System.out.println("Ingresa 'salir' para terminar la sesion");
        System.out.println();
        
        Interprete interprete = new Interprete();
        
        // Si hay argumentos en la linea de comandos, evaluarlos
        if (args.length > 0) {
            StringBuilder expresion = new StringBuilder();
            for (String arg : args) {
                expresion.append(arg).append(" ");
            }
            
            try {
                ElementoBase resultado = interprete.evaluar(interprete.leer(expresion.toString()));
                System.out.print("Resultado: ");
                resultado.imprimir(System.out);
                System.out.println();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            // Iniciar el modo interactivo (REPL)
            interprete.iniciarREPL();
        }
    }
}
