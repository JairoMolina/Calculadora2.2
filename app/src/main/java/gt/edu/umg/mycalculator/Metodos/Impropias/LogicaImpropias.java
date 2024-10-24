package gt.edu.umg.mycalculator.Metodos.Impropias;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.function.BiFunction;


import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import android.util.Log;

public class LogicaImpropias {
    private static final String TAG = "LogicaImpropias";
    private static final int NUM_INTERVALOS = 10000000;
    private static final double LIMITE_INFINITO = 10000;

    public static final double INFINITO_POSITIVO = Double.POSITIVE_INFINITY;
    public static final double INFINITO_NEGATIVO = Double.NEGATIVE_INFINITY;

    public static class ResultadoIntegralImpropia {
        public final double valor;
        public final String mensaje;
        public final boolean exito;
        public final double[] puntosX;
        public final double[] puntosY;
        public final boolean esInfinito;

        private ResultadoIntegralImpropia(double valor, String mensaje, boolean exito,
                                          double[] puntosX, double[] puntosY, boolean esInfinito) {
            this.valor = valor;
            this.mensaje = mensaje;
            this.exito = exito;
            this.puntosX = puntosX;
            this.puntosY = puntosY;
            this.esInfinito = esInfinito;
        }

        public static ResultadoIntegralImpropia error(String mensaje) {
            return new ResultadoIntegralImpropia(0, mensaje, false, null, null, false);
        }

        public static ResultadoIntegralImpropia exito(double valor, double[] puntosX, double[] puntosY, boolean esInfinito) {
            return new ResultadoIntegralImpropia(valor, "", true, puntosX, puntosY, esInfinito);
        }
    }

    @FunctionalInterface
    public interface FuncionDosVariables {
        double evaluar(double x, double y);
    }

    private static FuncionDosVariables convertirFuncion(String funcionStr) {
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

    public static ResultadoIntegralImpropia integrar(String funcionStr, double a, double b, double c, double d, String eje) {
        try {
            FuncionDosVariables funcion = convertirFuncion(funcionStr);

            // Validar la función
            double testValue = funcion.evaluar(
                    Double.isFinite(a) ? a : 0,
                    Double.isFinite(c) ? c : 0
            );
            if (Double.isNaN(testValue)) {
                return ResultadoIntegralImpropia.error("La función ingresada no es válida");
            }

            boolean esInfinito = Double.isInfinite(a) || Double.isInfinite(b) ||
                    Double.isInfinite(c) || Double.isInfinite(d);

            double resultado;
            if (eje.equals("x")) {
                if (Double.isInfinite(a) || Double.isInfinite(b)) {
                    resultado = integrarInfinito(funcion, a, b, c, eje);
                } else {
                    resultado = integrarFinito(funcion, a, b, c, d, eje);
                }
            } else if (eje.equals("y")) {
                if (Double.isInfinite(c) || Double.isInfinite(d)) {
                    resultado = integrarInfinito(funcion, c, d, a, eje);
                } else {
                    resultado = integrarFinito(funcion, a, b, c, d, eje);
                }
            } else {
                return ResultadoIntegralImpropia.error("Eje debe ser 'x' o 'y'");
            }

            // Generar puntos para graficar
            double[] puntosX = new double[100];
            double[] puntosY = new double[100];
            generarPuntosGrafica(funcion, a, b, c, d, eje, puntosX, puntosY);

            return ResultadoIntegralImpropia.exito(resultado, puntosX, puntosY, esInfinito);

        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo de la integral impropia: " + e.getMessage());
            return ResultadoIntegralImpropia.error("Error en el cálculo: " + e.getMessage());
        }
    }

    private static double integrarFinito(FuncionDosVariables funcion, double a, double b, double c, double d, String eje) {
        double h = (eje.equals("x") ? (b - a) : (d - c)) / NUM_INTERVALOS;
        double suma = 0.0;

        if (eje.equals("x")) {
            for (int i = 0; i < NUM_INTERVALOS; i++) {
                double x1 = a + i * h;
                double x2 = a + (i + 1) * h;
                suma += (funcion.evaluar(x1, c) + funcion.evaluar(x2, c)) / 2 * h;
            }
        } else {
            for (int i = 0; i < NUM_INTERVALOS; i++) {
                double y1 = c + i * h;
                double y2 = c + (i + 1) * h;
                suma += (funcion.evaluar(a, y1) + funcion.evaluar(a, y2)) / 2 * h;
            }
        }

        return suma;
    }

    private static double integrarInfinito(FuncionDosVariables funcion, double lim1, double lim2, double constante, String eje) {
        double limiteInferior, limiteSuperior;

        if (lim1 == INFINITO_NEGATIVO && lim2 == INFINITO_POSITIVO) {
            limiteInferior = -LIMITE_INFINITO;
            limiteSuperior = LIMITE_INFINITO;
        } else if (lim1 == INFINITO_NEGATIVO) {
            limiteInferior = -LIMITE_INFINITO;
            limiteSuperior = lim2;
        } else if (lim2 == INFINITO_POSITIVO) {
            limiteInferior = lim1;
            limiteSuperior = LIMITE_INFINITO;
        } else {
            limiteInferior = lim1;
            limiteSuperior = lim2;
        }

        return integrarFinito(funcion,
                eje.equals("x") ? limiteInferior : constante,
                eje.equals("x") ? limiteSuperior : constante,
                eje.equals("y") ? limiteInferior : constante,
                eje.equals("y") ? limiteSuperior : constante,
                eje);
    }

    private static void generarPuntosGrafica(FuncionDosVariables funcion, double a, double b,
                                             double c, double d, String eje,
                                             double[] puntosX, double[] puntosY) {
        double inicioX = Double.isInfinite(a) ? -10 : a;
        double finX = Double.isInfinite(b) ? 10 : b;
        double inicioY = Double.isInfinite(c) ? -10 : c;
        double finY = Double.isInfinite(d) ? 10 : d;

        double paso = (eje.equals("x") ? (finX - inicioX) : (finY - inicioY)) / (puntosX.length - 1);

        for (int i = 0; i < puntosX.length; i++) {
            if (eje.equals("x")) {
                puntosX[i] = inicioX + i * paso;
                puntosY[i] = funcion.evaluar(puntosX[i], c);
            } else {
                puntosY[i] = inicioY + i * paso;
                puntosX[i] = funcion.evaluar(a, puntosY[i]);
            }
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
                case "inf":
                case "∞":
                    return INFINITO_POSITIVO;
                case "-inf":
                case "-∞":
                    return INFINITO_NEGATIVO;
                default:
                    return Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error al parsear entrada: " + input);
            throw new IllegalArgumentException("Entrada no válida: " + input);
        }
    }
}