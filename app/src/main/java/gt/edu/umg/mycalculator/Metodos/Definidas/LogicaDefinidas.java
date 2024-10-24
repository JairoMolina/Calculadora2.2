package gt.edu.umg.mycalculator.Metodos.Definidas;

import java.util.function.BiFunction;
import java.util.function.BiFunction;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import android.util.Log;

public class LogicaDefinidas {
    private static final String TAG = "LogicaDefinidas";
    private static final int NUM_INTERVALOS = 100000;

    public static class ResultadoIntegral {
        public final double valor;
        public final String mensaje;
        public final boolean exito;
        public final double[] puntosX;
        public final double[] puntosY;

        private ResultadoIntegral(double valor, String mensaje, boolean exito, double[] puntosX, double[] puntosY) {
            this.valor = valor;
            this.mensaje = mensaje;
            this.exito = exito;
            this.puntosX = puntosX;
            this.puntosY = puntosY;
        }

        public static ResultadoIntegral error(String mensaje) {
            return new ResultadoIntegral(0, mensaje, false, null, null);
        }

        public static ResultadoIntegral exito(double valor, double[] puntosX, double[] puntosY) {
            return new ResultadoIntegral(valor, "", true, puntosX, puntosY);
        }
    }

    @FunctionalInterface
    public interface FuncionDosVariables {
        double evaluar(double x, double y);
    }

    public static FuncionDosVariables convertirFuncion(String funcionStr) {
        return (x, y) -> {
            try {
                Expression e = new ExpressionBuilder(funcionStr)
                        .variables("x", "y", "π", "e")
                        .build()
                        .setVariable("x", x)
                        .setVariable("y", y)
                        .setVariable("π", Math.PI)
                        .setVariable("e", Math.E);
                return e.evaluate();
            } catch (Exception e) {
                Log.e(TAG, "Error al evaluar la función: " + e.getMessage());
                return Double.NaN;
            }
        };
    }

    public static ResultadoIntegral integrar(String funcionStr, double a, double b, double c, double d, String eje) {
        try {
            FuncionDosVariables funcion = convertirFuncion(funcionStr);

            // Validar la función
            double testValue = funcion.evaluar((a + b) / 2, (c + d) / 2);
            if (Double.isNaN(testValue)) {
                return ResultadoIntegral.error("La función ingresada no es válida");
            }

            double h = (eje.equals("x") ? (b - a) : (d - c)) / NUM_INTERVALOS;
            double suma = 0.0;

            // Arrays para almacenar puntos para graficar
            int numPuntosGrafica = 100;
            double[] puntosX = new double[numPuntosGrafica];
            double[] puntosY = new double[numPuntosGrafica];
            double paso = (eje.equals("x") ? (b - a) : (d - c)) / (numPuntosGrafica - 1);

            if (eje.equals("x")) {
                // Integrar con respecto a x
                for (int i = 0; i < NUM_INTERVALOS; i++) {
                    double x1 = a + i * h;
                    double x2 = a + (i + 1) * h;
                    suma += (funcion.evaluar(x1, c) + funcion.evaluar(x2, c)) / 2 * h;
                }

                // Generar puntos para graficar
                for (int i = 0; i < numPuntosGrafica; i++) {
                    puntosX[i] = a + i * paso;
                    puntosY[i] = funcion.evaluar(puntosX[i], c);
                }
            } else if (eje.equals("y")) {
                // Integrar con respecto a y
                for (int i = 0; i < NUM_INTERVALOS; i++) {
                    double y1 = c + i * h;
                    double y2 = c + (i + 1) * h;
                    suma += (funcion.evaluar(a, y1) + funcion.evaluar(a, y2)) / 2 * h;
                }

                // Generar puntos para graficar
                for (int i = 0; i < numPuntosGrafica; i++) {
                    puntosY[i] = c + i * paso;
                    puntosX[i] = funcion.evaluar(a, puntosY[i]);
                }
            } else {
                return ResultadoIntegral.error("Eje no válido. Debe ser 'x' o 'y'");
            }

            return ResultadoIntegral.exito(suma, puntosX, puntosY);

        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo de la integral: " + e.getMessage());
            return ResultadoIntegral.error("Error en el cálculo: " + e.getMessage());
        }
    }

    public static double parseInput(String input) {
        try {
            switch (input.toLowerCase()) {
                case "pi":
                case "π":
                    return Math.PI;
                case "e":
                    return Math.E;
                default:
                    return Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error al parsear entrada: " + e.getMessage());
            return Double.NaN;
        }
    }
}