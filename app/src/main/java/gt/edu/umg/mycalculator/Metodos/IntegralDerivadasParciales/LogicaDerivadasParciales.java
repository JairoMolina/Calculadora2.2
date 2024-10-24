package gt.edu.umg.mycalculator.Metodos.IntegralDerivadasParciales;



import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import android.util.Log;

public class LogicaDerivadasParciales {
    private static final String TAG = "LogicaDerivadasParciales";
    private static final double H = 0.0001; // Paso para la diferencia finita

    public static class ResultadoDerivadas {
        public final String derivadaX;
        public final String derivadaY;
        public final boolean exito;
        public final String mensaje;
        public final double[] puntosEvaluacionX;
        public final double[] valoresDerivadaX;
        public final double[] puntosEvaluacionY;
        public final double[] valoresDerivadaY;

        private ResultadoDerivadas(String derivadaX, String derivadaY, boolean exito, String mensaje,
                                   double[] puntosEvaluacionX, double[] valoresDerivadaX,
                                   double[] puntosEvaluacionY, double[] valoresDerivadaY) {
            this.derivadaX = derivadaX;
            this.derivadaY = derivadaY;
            this.exito = exito;
            this.mensaje = mensaje;
            this.puntosEvaluacionX = puntosEvaluacionX;
            this.valoresDerivadaX = valoresDerivadaX;
            this.puntosEvaluacionY = puntosEvaluacionY;
            this.valoresDerivadaY = valoresDerivadaY;
        }

        public static ResultadoDerivadas error(String mensaje) {
            return new ResultadoDerivadas(null, null, false, mensaje, null, null, null, null);
        }

        public static ResultadoDerivadas exito(String derivadaX, String derivadaY,
                                               double[] puntosEvaluacionX, double[] valoresDerivadaX,
                                               double[] puntosEvaluacionY, double[] valoresDerivadaY) {
            return new ResultadoDerivadas(derivadaX, derivadaY, true, "",
                    puntosEvaluacionX, valoresDerivadaX,
                    puntosEvaluacionY, valoresDerivadaY);
        }
    }

    public static ResultadoDerivadas calcularDerivadasParciales(String expresion, double x0, double y0) {
        try {
            // Validar la función primero
            double testValue = evaluarFuncion(expresion, x0, y0);
            if (Double.isNaN(testValue)) {
                return ResultadoDerivadas.error("La función ingresada no es válida");
            }

            // Calcular derivada parcial respecto a x
            double derivadaX = (evaluarFuncion(expresion, x0 + H, y0) -
                    evaluarFuncion(expresion, x0 - H, y0)) / (2 * H);

            // Calcular derivada parcial respecto a y
            double derivadaY = (evaluarFuncion(expresion, x0, y0 + H) -
                    evaluarFuncion(expresion, x0, y0 - H)) / (2 * H);

            // Generar puntos para graficar las derivadas
            int numPuntos = 100;
            double[] puntosEvaluacionX = new double[numPuntos];
            double[] valoresDerivadaX = new double[numPuntos];
            double[] puntosEvaluacionY = new double[numPuntos];
            double[] valoresDerivadaY = new double[numPuntos];

            double rangoX = 2.0; // Rango de ±2 alrededor del punto de evaluación
            double rangoY = 2.0;

            // Generar puntos para la derivada respecto a x
            for (int i = 0; i < numPuntos; i++) {
                double x = x0 - rangoX + (2 * rangoX * i) / (numPuntos - 1);
                puntosEvaluacionX[i] = x;
                valoresDerivadaX[i] = (evaluarFuncion(expresion, x + H, y0) -
                        evaluarFuncion(expresion, x - H, y0)) / (2 * H);
            }

            // Generar puntos para la derivada respecto a y
            for (int i = 0; i < numPuntos; i++) {
                double y = y0 - rangoY + (2 * rangoY * i) / (numPuntos - 1);
                puntosEvaluacionY[i] = y;
                valoresDerivadaY[i] = (evaluarFuncion(expresion, x0, y + H) -
                        evaluarFuncion(expresion, x0, y - H)) / (2 * H);
            }

            return ResultadoDerivadas.exito(
                    String.format("∂f/∂x ≈ %.4f en (%.2f, %.2f)", derivadaX, x0, y0),
                    String.format("∂f/∂y ≈ %.4f en (%.2f, %.2f)", derivadaY, x0, y0),
                    puntosEvaluacionX, valoresDerivadaX,
                    puntosEvaluacionY, valoresDerivadaY
            );

        } catch (Exception e) {
            Log.e(TAG, "Error al calcular las derivadas parciales: " + e.getMessage());
            return ResultadoDerivadas.error("Error en el cálculo: " + e.getMessage());
        }
    }

    private static double evaluarFuncion(String expresion, double x, double y) {
        try {
            Expression e = new ExpressionBuilder(expresion)
                    .variables("x", "y")
                    .build()
                    .setVariable("x", x)
                    .setVariable("y", y);
            return e.evaluate();
        } catch (Exception e) {
            Log.e(TAG, "Error al evaluar la función: " + e.getMessage());
            return Double.NaN;
        }
    }

    public static String simplificarExpresionDerivada(double[] coeficientes, String[] terminos) {
        StringBuilder resultado = new StringBuilder();
        boolean primerTermino = true;

        for (int i = 0; i < coeficientes.length; i++) {
            if (Math.abs(coeficientes[i]) > 1e-10) { // Ignorar términos muy cercanos a cero
                if (!primerTermino && coeficientes[i] > 0) {
                    resultado.append(" + ");
                } else if (!primerTermino) {
                    resultado.append(" - ");
                } else if (coeficientes[i] < 0) {
                    resultado.append("-");
                }

                double coefAbs = Math.abs(coeficientes[i]);
                if (Math.abs(coefAbs - 1.0) > 1e-10 || terminos[i].isEmpty()) {
                    resultado.append(String.format("%.4f", coefAbs));
                }

                if (!terminos[i].isEmpty()) {
                    if (Math.abs(coefAbs - 1.0) > 1e-10) {
                        resultado.append("*");
                    }
                    resultado.append(terminos[i]);
                }

                primerTermino = false;
            }
        }

        return resultado.length() > 0 ? resultado.toString() : "0";
    }
}