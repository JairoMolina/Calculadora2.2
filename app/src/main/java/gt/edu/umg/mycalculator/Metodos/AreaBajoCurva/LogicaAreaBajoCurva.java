package gt.edu.umg.mycalculator.Metodos.AreaBajoCurva;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
public class LogicaAreaBajoCurva {
    public static class ResultadoAreaBajoCurva {
        public final double area;
        public final String mensaje;
        public final boolean exito;
        public final double[] puntosX;
        public final double[] puntosY;

        private ResultadoAreaBajoCurva(double area, String mensaje, boolean exito,
                                       double[] puntosX, double[] puntosY) {
            this.area = area;
            this.mensaje = mensaje;
            this.exito = exito;
            this.puntosX = puntosX;
            this.puntosY = puntosY;
        }

        public static ResultadoAreaBajoCurva error(String mensaje) {
            return new ResultadoAreaBajoCurva(0, mensaje, false, null, null);
        }

        public static ResultadoAreaBajoCurva exito(double area, double[] puntosX, double[] puntosY) {
            return new ResultadoAreaBajoCurva(area, "", true, puntosX, puntosY);
        }
    }

    /**
     * Calcula el área bajo la curva usando el método de Simpson
     */
    public static ResultadoAreaBajoCurva calcularArea(String funcion, double limiteInferior, double limiteSuperior) {
        try {
            // Validar la función
            double testValue = evaluarFuncion(funcion, "x", (limiteInferior + limiteSuperior) / 2);
            if (Double.isNaN(testValue)) {
                return ResultadoAreaBajoCurva.error("La función ingresada no es válida");
            }

            // Número de subintervalos (debe ser par)
            int n = 1000;
            double h = (limiteSuperior - limiteInferior) / n;

            double suma = evaluarFuncion(funcion, "x", limiteInferior) +
                    evaluarFuncion(funcion, "x", limiteSuperior);

            // Suma los términos pares
            for (int i = 2; i < n; i += 2) {
                double x = limiteInferior + i * h;
                suma += 2 * evaluarFuncion(funcion, "x", x);
            }

            // Suma los términos impares
            for (int i = 1; i < n; i += 2) {
                double x = limiteInferior + i * h;
                suma += 4 * evaluarFuncion(funcion, "x", x);
            }

            double area = (h / 3) * suma;

            // Generar puntos para la gráfica
            int numPuntos = 100;
            double[] puntosX = new double[numPuntos];
            double[] puntosY = new double[numPuntos];

            double paso = (limiteSuperior - limiteInferior) / (numPuntos - 1);
            for (int i = 0; i < numPuntos; i++) {
                puntosX[i] = limiteInferior + i * paso;
                puntosY[i] = evaluarFuncion(funcion, "x", puntosX[i]);
            }

            return ResultadoAreaBajoCurva.exito(area, puntosX, puntosY);

        } catch (Exception e) {
            return ResultadoAreaBajoCurva.error("Error al calcular el área bajo la curva: " + e.getMessage());
        }
    }

    /**
     * Evalúa la función en un punto x dado usando exp4j
     */
    private static double evaluarFuncion(String funcion, String variable, double x) {
        try {
            Expression expression = new ExpressionBuilder(funcion)
                    .variable(variable)
                    .build();
            return expression.setVariable(variable, x).evaluate();
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}