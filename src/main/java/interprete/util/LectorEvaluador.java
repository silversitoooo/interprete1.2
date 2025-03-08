package interprete.util;

import interprete.elementos.ElementoBase;
import interprete.elementos.Enlace;
import interprete.elementos.Entero;
import interprete.elementos.Simbolo;
import interprete.elementos.Texto;
import interprete.excepciones.ParseException;

/**
 * Analiza y evalúa expresiones textuales.
 * Convierte cadenas de texto en estructuras de ElementoBase.
 */
public class LectorEvaluador {
    
    /**
     * Parsea una cadena de texto en un elemento del lenguaje
     * @param texto La expresión en texto plano a parsear
     * @return La estructura de ElementoBase correspondiente
     * @throws ParseException si hay un error de sintaxis en la expresión
     */
    public ElementoBase leer(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return Simbolo.VACIO;
        }
        
        Lector lector = new Lector(texto);
        return lector.leerExpresion();
    }
    
    /**
     * Clase interna para el análisis léxico y sintáctico
     */
    private class Lector {
        private String texto;
        private int posicion;
        
        public Lector(String texto) {
            this.texto = texto;
            this.posicion = 0;
        }
        
        /**
         * Lee una expresión completa desde la posición actual
         */
        public ElementoBase leerExpresion() {
            saltarEspaciosEnBlanco();
            
            if (posicion >= texto.length()) {
                return Simbolo.VACIO;
            }
            
            char c = texto.charAt(posicion);
            
            if (c == '(') {
                return leerLista();
            } else if (c == ')') {
                posicion++;
                throw new ParseException("Paréntesis de cierre inesperado");
            } else if (c == '\'') {
                posicion++;
                ElementoBase citado = leerExpresion();
                return new Enlace(Simbolo.CITAR, new Enlace(citado, Simbolo.VACIO));
            } else if (c == '"') {
                return leerTexto();
            } else if (Character.isDigit(c) || (c == '-' && posicion + 1 < texto.length() && Character.isDigit(texto.charAt(posicion + 1)))) {
                return leerNumero();
            } else {
                return leerSimbolo();
            }
        }
        
        /**
         * Lee una lista (expresión entre paréntesis)
         */
        private ElementoBase leerLista() {
            posicion++; // Saltar '('
            saltarEspaciosEnBlanco();
            
            if (posicion >= texto.length()) {
                throw new ParseException("Lista no cerrada al final de la expresión");
            }
            
            if (texto.charAt(posicion) == ')') {
                posicion++; // Saltar ')'
                return Simbolo.VACIO;
            }
            
            ElementoBase primero = leerExpresion();
            saltarEspaciosEnBlanco();
            
            if (posicion >= texto.length()) {
                throw new ParseException("Lista no cerrada al final de la expresión");
            }
            
            // Soporte para notación de punto (pares explícitos)
            if (texto.charAt(posicion) == '.') {
                posicion++; // Saltar '.'
                saltarEspaciosEnBlanco();
                ElementoBase resto = leerExpresion();
                saltarEspaciosEnBlanco();
                
                if (posicion >= texto.length() || texto.charAt(posicion) != ')') {
                    throw new ParseException("Se esperaba ')' después del punto en una lista");
                }
                
                posicion++; // Saltar ')'
                return new Enlace(primero, resto);
            } else {
                // Lista normal
                ElementoBase resto = leerLista();
                return new Enlace(primero, resto);
            }
        }
        
        /**
         * Lee una cadena de texto (entre comillas dobles)
         */
        private ElementoBase leerTexto() {
            posicion++; // Saltar '"'
            StringBuilder sb = new StringBuilder();
            
            while (posicion < texto.length() && texto.charAt(posicion) != '"') {
                char c = texto.charAt(posicion++);
                if (c == '\\' && posicion < texto.length()) {
                    char escapado = texto.charAt(posicion++);
                    switch (escapado) {
                        case 'n': sb.append('\n'); break;
                        case 't': sb.append('\t'); break;
                        case 'r': sb.append('\r'); break;
                        case '"': sb.append('"'); break;
                        case '\\': sb.append('\\'); break;
                        default: sb.append(escapado); break;
                    }
                } else {
                    sb.append(c);
                }
            }
            
            if (posicion >= texto.length() || texto.charAt(posicion) != '"') {
                throw new ParseException("Cadena de texto no cerrada");
            }
            
            posicion++; // Saltar '"'
            return Texto.crear(sb.toString());
        }
        
        /**
         * Lee un número entero
         */
        private ElementoBase leerNumero() {
            int inicio = posicion;
            
            if (texto.charAt(posicion) == '-') {
                posicion++;
            }
            
            while (posicion < texto.length() && Character.isDigit(texto.charAt(posicion))) {
                posicion++;
            }
            
            String numero = texto.substring(inicio, posicion);
            try {
                return Entero.crear(Long.parseLong(numero));
            } catch (NumberFormatException e) {
                throw new ParseException("Número inválido: " + numero);
            }
        }
        
        /**
         * Lee un símbolo (identificador)
         */
        private ElementoBase leerSimbolo() {
            int inicio = posicion;
            
            while (posicion < texto.length() && !Character.isWhitespace(texto.charAt(posicion)) && 
                   texto.charAt(posicion) != '(' && texto.charAt(posicion) != ')' && 
                   texto.charAt(posicion) != '\'' && texto.charAt(posicion) != '"') {
                posicion++;
            }
            
            String simbolo = texto.substring(inicio, posicion).toUpperCase();
            
            // Manejo de constantes especiales
            if (simbolo.equals("NIL")) {
                return Simbolo.VACIO;
            } else if (simbolo.equals("T")) {
                return Simbolo.VERDADERO;
            } else {
                return Simbolo.simbolo(simbolo);
            }
        }
        
        /**
         * Avanza la posición hasta el primer carácter no-blanco
         */
        private void saltarEspaciosEnBlanco() {
            while (posicion < texto.length()) {
                char c = texto.charAt(posicion);
                if (Character.isWhitespace(c)) {
                    posicion++;
                } else if (c == ';') {  // Comentarios de línea
                    while (posicion < texto.length() && texto.charAt(posicion) != '\n') {
                        posicion++;
                    }
                    if (posicion < texto.length()) {
                        posicion++; // Saltar el '\n'
                    }
                } else {
                    break;
                }
            }
        }
    }
}