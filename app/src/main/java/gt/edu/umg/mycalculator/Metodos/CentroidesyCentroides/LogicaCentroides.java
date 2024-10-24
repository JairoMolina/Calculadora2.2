package gt.edu.umg.mycalculator.Metodos.CentroidesyCentroides;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


import android.util.Log;

public class LogicaCentroides {
    private static final String TAG = "LogicaCentroides";
    private static final int NUM_INTERVALOS = 10000;

    public static class ResultadoCentroides {
        public final double area;
        public final double xBar;
        public final double yBar;
        public final String mensaje;
        public final boolean exito;

        private ResultadoCentroides(double area, double xBar, double yBar, String mensaje, boolean exito) {
            this.area = area;
            this.xBar = xBar;
            this.yBar = yBar;
            this.mensaje = mensaje;
            this.exito = exito;
        }

        public static ResultadoCentroides error(String mensaje) {
            return new ResultadoCentroides(0, 0, 0, mensaje, false);
        }

        public static ResultadoCentroides exito(double area, double xBar, double yBar) {
            return new ResultadoCentroides(area, xBar, yBar, "", true);
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

    private static double integrarSimpson(FuncionIntegrable funcion, double a, double b) {
        try {
            int n = NUM_INTERVALOS; // Número de subintervalos (debe ser par)
            double h = (b - a) / n;

            double suma = funcion.evaluar(a) + funcion.evaluar(b);

            // Suma términos pares
            for (int i = 2; i < n; i += 2) {
                suma += 2 * funcion.evaluar(a + i * h);
            }

            // Suma términos impares
            for (int i = 1; i < n; i += 2) {
                suma += 4 * funcion.evaluar(a + i * h);
            }

            return (h / 3) * suma;
        } catch (Exception e) {
            Log.e(TAG, "Error al calcular la integral: " + e.getMessage());
            return Double.NaN;
        }
    }

    @FunctionalInterface
    private interface FuncionIntegrable {
        double evaluar(double x);
    }

    public static ResultadoCentroides calcularCentroides(String funcionInput, double a, double b) {
        try {
            // Validar la función primero
            double testValue = evaluarFuncion(funcionInput, (a + b) / 2);
            if (Double.isNaN(testValue)) {
                return ResultadoCentroides.error("La función ingresada no es válida");
            }

            // Definir la función principal
            FuncionIntegrable funcion = x -> evaluarFuncion(funcionInput, x);

            // Calcular el área
            double area = integrarSimpson(funcion, a, b);

            if (!Double.isNaN(area) && area != 0) {
                // Calcular xBar (centroide en x)
                double xBar = integrarSimpson(
                        x -> x * funcion.evaluar(x),
                        a, b
                ) / area;

                // Calcular yBar (centroide en y)
                double yBar = area / (b - a);

                return ResultadoCentroides.exito(area, xBar, yBar);
            } else {
                return ResultadoCentroides.error("El área es 0 o no se pudo calcular correctamente");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo de centroides: " + e.getMessage());
            return ResultadoCentroides.error("Error en el cálculo: " + e.getMessage());
        }
    }
}