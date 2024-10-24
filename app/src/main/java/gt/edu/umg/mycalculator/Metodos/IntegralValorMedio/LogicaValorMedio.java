package gt.edu.umg.mycalculator.Metodos.IntegralValorMedio;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import android.util.Log;

public class LogicaValorMedio {
    private static final String TAG = "LogicaValorMedio";
    private static final int NUM_INTERVALOS = 10000;
    private static final int NUM_PUNTOS_GRAFICA = 100;

    public static class ResultadoValorMedio {
        public final double valorMedio;
        public final double integral;
        public final boolean exito;
        public final String mensaje;
        public final double[] puntosX;
        public final double[] puntosY;
        public final double[] puntosPromedioX;
        public final double[] puntosPromedioY;

        private ResultadoValorMedio(double valorMedio, double integral, boolean exito, String mensaje,
                                    double[] puntosX, double[] puntosY,
                                    double[] puntosPromedioX, double[] puntosPromedioY) {
            this.valorMedio = valorMedio;
            this.integral = integral;
            this.exito = exito;
            this.mensaje = mensaje;
            this.puntosX = puntosX;
            this.puntosY = puntosY;
            this.puntosPromedioX = puntosPromedioX;
            this.puntosPromedioY = puntosPromedioY;
        }

        public static ResultadoValorMedio error(String mensaje) {
            return new ResultadoValorMedio(Double.NaN, Double.NaN, false, mensaje,
                    null, null, null, null);
        }

        public static ResultadoValorMedio exito(double valorMedio, double integral,
                                                double[] puntosX, double[] puntosY,
                                                double[] puntosPromedioX, double[] puntosPromedioY) {
            return new ResultadoValorMedio(valorMedio, integral, true, "",
                    puntosX, puntosY, puntosPromedioX, puntosPromedioY);
        }
    }

    private static double evaluarFuncion(String funcion, double x) {
        try {
            Expression expression = new ExpressionBuilder(funcion)
                    .variable("x")
                    .build()
                    .setVariable("x", x);
            return expression.evaluate();
        } catch (Exception e) {
            Log.e(TAG, "Error al evaluar la función: " + e.getMessage());
            return Double.NaN;
        }
    }

    public static ResultadoValorMedio calcularValorMedio(String funcion, double a, double b) {
        try {
            // Validar la función primero
            double testValue = evaluarFuncion(funcion, (a + b) / 2);
            if (Double.isNaN(testValue)) {
                return ResultadoValorMedio.error("La función ingresada no es válida");
            }

            // Calcular la integral usando el método de Simpson
            double integral = integrarSimpson(funcion, a, b);
            if (Double.isNaN(integral)) {
                return ResultadoValorMedio.error("Error al calcular la integral");
            }

            // Calcular el valor medio
            double valorMedio = integral / (b - a);

            // Generar puntos para graficar la función original
            double[] puntosX = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosY = new double[NUM_PUNTOS_GRAFICA];
            double paso = (b - a) / (NUM_PUNTOS_GRAFICA - 1);

            for (int i = 0; i < NUM_PUNTOS_GRAFICA; i++) {
                puntosX[i] = a + i * paso;
                puntosY[i] = evaluarFuncion(funcion, puntosX[i]);
            }

            // Generar puntos para la línea del valor medio
            double[] puntosPromedioX = {a, b};
            double[] puntosPromedioY = {valorMedio, valorMedio};

            return ResultadoValorMedio.exito(valorMedio, integral,
                    puntosX, puntosY, puntosPromedioX, puntosPromedioY);

        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo del valor medio: " + e.getMessage());
            return ResultadoValorMedio.error("Error en el cálculo: " + e.getMessage());
        }
    }

    private static double integrarSimpson(String funcion, double a, double b) {
        int n = NUM_INTERVALOS; // Debe ser par
        double h = (b - a) / n;

        double suma = evaluarFuncion(funcion, a) + evaluarFuncion(funcion, b);

        // Suma los términos pares
        for (int i = 2; i < n; i += 2) {
            double x = a + i * h;
            suma += 2 * evaluarFuncion(funcion, x);
        }

        // Suma los términos impares
        for (int i = 1; i < n; i += 2) {
            double x = a + i * h;
            suma += 4 * evaluarFuncion(funcion, x);
        }

        return (h / 3) * suma;
    }

    // Método auxiliar para formatear resultados
    public static String formatearResultado(ResultadoValorMedio resultado) {
        if (!resultado.exito) {
            return resultado.mensaje;
        }

        return String.format(
                "Valor medio: %.4f\n" +
                        "Integral: %.4f\n" +
                        "Interpretación: El valor promedio de la función en el intervalo es %.4f",
                resultado.valorMedio, resultado.integral, resultado.valorMedio
        );
    }
}



























