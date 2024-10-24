package gt.edu.umg.mycalculator.Metodos.IntegralporPartes;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import android.util.Log;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LogicaPorPartes {
    private static final String TAG = "LogicaPorPartes";

    public static class ResultadoIntegracion {
        public final String integral;
        public final String pasos;
        public final boolean exito;
        public final String mensaje;

        private ResultadoIntegracion(String integral, String pasos, boolean exito, String mensaje) {
            this.integral = integral;
            this.pasos = pasos;
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public static ResultadoIntegracion error(String mensaje) {
            return new ResultadoIntegracion(null, null, false, mensaje);
        }

        public static ResultadoIntegracion exito(String integral, String pasos) {
            return new ResultadoIntegracion(integral, pasos, true, "");
        }
    }

    // Método principal para integrar
    public static ResultadoIntegracion integrar(String funcion, String variable) {
        try {
            // Primero intentamos identificar el patrón de la función
            TipoFuncion tipo = identificarTipoFuncion(funcion);
            StringBuilder pasos = new StringBuilder();
            String resultado;

            switch (tipo) {
                case EXPONENCIAL_X:
                    resultado = integrarExponencial(funcion, variable, pasos);
                    break;
                case X_EXPONENCIAL:
                    resultado = integrarXPorExponencial(funcion, variable, pasos);
                    break;
                case LOGARITMO:
                    resultado = integrarLogaritmo(funcion, variable, pasos);
                    break;
                case X_LOGARITMO:
                    resultado = integrarXPorLogaritmo(funcion, variable, pasos);
                    break;
                case TRIGONOMETRICA:
                    resultado = integrarTrigonometrica(funcion, variable, pasos);
                    break;
                case DESCONOCIDO:
                default:
                    return ResultadoIntegracion.error("Esta forma de integración no está soportada actualmente");
            }

            return ResultadoIntegracion.exito(resultado, pasos.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error al integrar: " + e.getMessage());
            return ResultadoIntegracion.error("Error en la integración: " + e.getMessage());
        }
    }

    private enum TipoFuncion {
        EXPONENCIAL_X,      // e^x
        X_EXPONENCIAL,      // x*e^x
        LOGARITMO,         // ln(x)
        X_LOGARITMO,       // x*ln(x)
        TRIGONOMETRICA,    // x*sin(x) o x*cos(x)
        DESCONOCIDO
    }

    private static TipoFuncion identificarTipoFuncion(String funcion) {
        funcion = funcion.replaceAll("\\s+", "").toLowerCase();

        if (funcion.matches("e\\^x")) {
            return TipoFuncion.EXPONENCIAL_X;
        } else if (funcion.matches("x\\*e\\^x")) {
            return TipoFuncion.X_EXPONENCIAL;
        } else if (funcion.matches("ln\\(x\\)")) {
            return TipoFuncion.LOGARITMO;
        } else if (funcion.matches("x\\*ln\\(x\\)")) {
            return TipoFuncion.X_LOGARITMO;
        } else if (funcion.matches("x\\*sin\\(x\\)") || funcion.matches("x\\*cos\\(x\\)")) {
            return TipoFuncion.TRIGONOMETRICA;
        }
        return TipoFuncion.DESCONOCIDO;
    }

    private static String integrarExponencial(String funcion, String variable, StringBuilder pasos) {
        pasos.append("1. La integral de e^x es e^x + C\n");
        return "e^x + C";
    }

    private static String integrarXPorExponencial(String funcion, String variable, StringBuilder pasos) {
        pasos.append("1. Usando integración por partes:\n");
        pasos.append("   u = x, dv = e^x dx\n");
        pasos.append("2. Entonces:\n");
        pasos.append("   du = dx\n");
        pasos.append("   v = e^x\n");
        pasos.append("3. La fórmula es: ∫x*e^x dx = x*e^x - ∫e^x dx\n");
        pasos.append("4. Resultado: x*e^x - e^x + C\n");
        return "x*e^x - e^x + C";
    }

    private static String integrarLogaritmo(String funcion, String variable, StringBuilder pasos) {
        pasos.append("1. La integral de ln(x) es x*ln(x) - x + C\n");
        return "x*ln(x) - x + C";
    }

    private static String integrarXPorLogaritmo(String funcion, String variable, StringBuilder pasos) {
        pasos.append("1. Usando integración por partes:\n");
        pasos.append("   u = x, dv = ln(x) dx\n");
        pasos.append("2. Entonces:\n");
        pasos.append("   du = dx\n");
        pasos.append("   v = x*ln(x) - x\n");
        pasos.append("3. La fórmula es: ∫x*ln(x) dx = (x^2*ln(x))/2 - x^2/4 + C\n");
        return "(x^2*ln(x))/2 - x^2/4 + C";
    }

    private static String integrarTrigonometrica(String funcion, String variable, StringBuilder pasos) {
        if (funcion.contains("sin")) {
            pasos.append("1. Para ∫x*sin(x) dx usando integración por partes:\n");
            pasos.append("2. Resultado: sin(x) - x*cos(x) + C\n");
            return "sin(x) - x*cos(x) + C";
        } else {
            pasos.append("1. Para ∫x*cos(x) dx usando integración por partes:\n");
            pasos.append("2. Resultado: cos(x) + x*sin(x) + C\n");
            return "cos(x) + x*sin(x) + C";
        }
    }

    public static double evaluarFuncion(String expresion, double x) {
        try {
            Expression e = new ExpressionBuilder(expresion)
                    .variable("x")
                    .build()
                    .setVariable("x", x);
            return e.evaluate();
        } catch (Exception e) {
            Log.e(TAG, "Error al evaluar la función: " + e.getMessage());
            return Double.NaN;
        }
    }
}








