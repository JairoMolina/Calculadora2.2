package gt.edu.umg.mycalculator.Metodos.VolumenSolido;



import android.util.Log;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Arandelas {
    private static final String TAG = "LogicaArandelas";
    private static final int NUM_INTERVALOS = 10000;
    private static final int NUM_PUNTOS_GRAFICA = 200;

    public static class ResultadoArandelas {
        public final double volumen;
        public final double[] puntosXF;
        public final double[] puntosYF;
        public final double[] puntosXG;
        public final double[] puntosYG;
        public final double limiteInferior;
        public final double limiteSuperior;
        public final boolean exito;
        public final String mensaje;
        public final char eje;

        private ResultadoArandelas(double volumen,
                                   double[] puntosXF, double[] puntosYF,
                                   double[] puntosXG, double[] puntosYG,
                                   double limiteInferior, double limiteSuperior,
                                   boolean exito, String mensaje, char eje) {
            this.volumen = volumen;
            this.puntosXF = puntosXF;
            this.puntosYF = puntosYF;
            this.puntosXG = puntosXG;
            this.puntosYG = puntosYG;
            this.limiteInferior = limiteInferior;
            this.limiteSuperior = limiteSuperior;
            this.exito = exito;
            this.mensaje = mensaje;
            this.eje = eje;
        }

        public static ResultadoArandelas error(String mensaje) {
            return new ResultadoArandelas(0, null, null, null, null, 0, 0, false, mensaje, '\0');
        }

        public static ResultadoArandelas exito(double volumen,
                                               double[] puntosXF, double[] puntosYF,
                                               double[] puntosXG, double[] puntosYG,
                                               double limiteInferior, double limiteSuperior,
                                               char eje) {
            return new ResultadoArandelas(volumen, puntosXF, puntosYF, puntosXG, puntosYG,
                    limiteInferior, limiteSuperior, true, "", eje);
        }
    }

    public static ResultadoArandelas calcularVolumen(String funcionF, String funcionG,
                                                     double limInferior, double limSuperior,
                                                     char eje) {
        try {
            // Validar las funciones
            if (!validarFuncion(funcionF, eje, (limInferior + limSuperior) / 2) ||
                    !validarFuncion(funcionG, eje, (limInferior + limSuperior) / 2)) {
                return ResultadoArandelas.error("Una o ambas funciones no son válidas");
            }

            // Calcular el volumen usando el método de Simpson
            double volumen = calcularVolumenSimpson(funcionF, funcionG, limInferior, limSuperior, eje);

            // Generar puntos para graficar ambas funciones
            double[] puntosXF = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosYF = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosXG = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosYG = new double[NUM_PUNTOS_GRAFICA];

            generarPuntosGrafica(funcionF, funcionG, limInferior, limSuperior, eje,
                    puntosXF, puntosYF, puntosXG, puntosYG);

            return ResultadoArandelas.exito(
                    volumen,
                    puntosXF, puntosYF,
                    puntosXG, puntosYG,
                    limInferior, limSuperior,
                    eje
            );

        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo del volumen: " + e.getMessage());
            return ResultadoArandelas.error("Error en el cálculo: " + e.getMessage());
        }
    }

    private static boolean validarFuncion(String funcion, char eje, double valor) {
        try {
            ExpressionBuilder builder = new ExpressionBuilder(funcion)
                    .variable(String.valueOf(eje));
            Expression expr = builder.build();
            expr.setVariable(String.valueOf(eje), valor);
            double resultado = expr.evaluate();
            return !Double.isNaN(resultado) && !Double.isInfinite(resultado);
        } catch (Exception e) {
            return false;
        }
    }

    private static double calcularVolumenSimpson(String funcionF, String funcionG,
                                                 double a, double b, char eje) {
        double h = (b - a) / NUM_INTERVALOS;
        double suma = evaluarDiferenciaCuadrados(funcionF, funcionG, a, eje) +
                evaluarDiferenciaCuadrados(funcionF, funcionG, b, eje);

        // Suma términos pares
        for (int i = 2; i < NUM_INTERVALOS; i += 2) {
            double x = a + i * h;
            suma += 2 * evaluarDiferenciaCuadrados(funcionF, funcionG, x, eje);
        }

        // Suma términos impares
        for (int i = 1; i < NUM_INTERVALOS; i += 2) {
            double x = a + i * h;
            suma += 4 * evaluarDiferenciaCuadrados(funcionF, funcionG, x, eje);
        }

        return Math.PI * (h / 3) * suma;
    }

    private static double evaluarDiferenciaCuadrados(String funcionF, String funcionG,
                                                     double valor, char eje) {
        try {
            double f = evaluarFuncion(funcionF, valor, eje);
            double g = evaluarFuncion(funcionG, valor, eje);
            return (f * f) - (g * g);
        } catch (Exception e) {
            Log.e(TAG, "Error al evaluar diferencia de cuadrados: " + e.getMessage());
            return 0;
        }
    }

    private static double evaluarFuncion(String funcion, double valor, char eje) {
        Expression expr = new ExpressionBuilder(funcion)
                .variable(String.valueOf(eje))
                .build();
        expr.setVariable(String.valueOf(eje), valor);
        return expr.evaluate();
    }

    private static void generarPuntosGrafica(String funcionF, String funcionG,
                                             double limInferior, double limiteSuperior,
                                             char eje, double[] puntosXF, double[] puntosYF,
                                             double[] puntosXG, double[] puntosYG) {
        double paso = (limiteSuperior - limInferior) / (NUM_PUNTOS_GRAFICA - 1);

        for (int i = 0; i < NUM_PUNTOS_GRAFICA; i++) {
            double valor = limInferior + i * paso;

            if (eje == 'x') {
                puntosXF[i] = valor;
                puntosYF[i] = evaluarFuncion(funcionF, valor, eje);
                puntosXG[i] = valor;
                puntosYG[i] = evaluarFuncion(funcionG, valor, eje);
            } else {
                puntosYF[i] = valor;
                puntosXF[i] = evaluarFuncion(funcionF, valor, eje);
                puntosYG[i] = valor;
                puntosXG[i] = evaluarFuncion(funcionG, valor, eje);
            }
        }
    }
}