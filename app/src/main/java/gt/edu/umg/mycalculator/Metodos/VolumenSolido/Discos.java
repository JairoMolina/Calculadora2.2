package gt.edu.umg.mycalculator.Metodos.VolumenSolido;


import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import android.util.Log;

public class Discos {
    private static final String TAG = "LogicaDiscos";
    private static final int NUM_INTERVALOS = 10000;
    private static final int NUM_PUNTOS_GRAFICA = 200;

    public static class ResultadoDiscos {
        public final double volumen;
        public final double[] puntosXF;
        public final double[] puntosYF;
        public final double[] puntosXG;
        public final double[] puntosYG;
        public final double limiteInferior;
        public final double limiteSuperior;
        public final boolean exito;
        public final String mensaje;
        public final char ejeRotacion;

        private ResultadoDiscos(double volumen,
                                double[] puntosXF, double[] puntosYF,
                                double[] puntosXG, double[] puntosYG,
                                double limiteInferior, double limiteSuperior,
                                boolean exito, String mensaje, char ejeRotacion) {
            this.volumen = volumen;
            this.puntosXF = puntosXF;
            this.puntosYF = puntosYF;
            this.puntosXG = puntosXG;
            this.puntosYG = puntosYG;
            this.limiteInferior = limiteInferior;
            this.limiteSuperior = limiteSuperior;
            this.exito = exito;
            this.mensaje = mensaje;
            this.ejeRotacion = ejeRotacion;
        }

        public static ResultadoDiscos error(String mensaje) {
            return new ResultadoDiscos(0, null, null, null, null,
                    0, 0, false, mensaje, '\0');
        }

        public static ResultadoDiscos exito(double volumen,
                                            double[] puntosXF, double[] puntosYF,
                                            double[] puntosXG, double[] puntosYG,
                                            double limiteInferior, double limiteSuperior,
                                            char ejeRotacion) {
            return new ResultadoDiscos(volumen, puntosXF, puntosYF, puntosXG, puntosYG,
                    limiteInferior, limiteSuperior, true, "", ejeRotacion);
        }
    }

    public static ResultadoDiscos calcularVolumen(String funcionF, String funcionG,
                                                  double limInferior, double limiteSuperior,
                                                  char ejeRotacion) {
        try {
            // Validar las funciones
            if (!validarFuncion(funcionF, ejeRotacion, (limInferior + limiteSuperior) / 2) ||
                    !validarFuncion(funcionG, ejeRotacion, (limInferior + limiteSuperior) / 2)) {
                return ResultadoDiscos.error("Una o ambas funciones no son válidas");
            }

            // Calcular el volumen usando el método de Simpson
            double volumen = calcularVolumenSimpson(funcionF, funcionG,
                    limInferior, limiteSuperior, ejeRotacion);

            // Generar puntos para graficar
            double[] puntosXF = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosYF = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosXG = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosYG = new double[NUM_PUNTOS_GRAFICA];

            generarPuntosGrafica(funcionF, funcionG, limInferior, limiteSuperior,
                    ejeRotacion, puntosXF, puntosYF, puntosXG, puntosYG);

            return ResultadoDiscos.exito(
                    volumen,
                    puntosXF, puntosYF,
                    puntosXG, puntosYG,
                    limInferior, limiteSuperior,
                    ejeRotacion
            );

        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo del volumen: " + e.getMessage());
            return ResultadoDiscos.error("Error en el cálculo: " + e.getMessage());
        }
    }

    private static boolean validarFuncion(String funcion, char variable, double valor) {
        try {
            Expression expr = new ExpressionBuilder(funcion)
                    .variable(String.valueOf(variable))
                    .build()
                    .setVariable(String.valueOf(variable), valor);
            double resultado = expr.evaluate();
            return !Double.isNaN(resultado) && !Double.isInfinite(resultado);
        } catch (Exception e) {
            return false;
        }
    }

    private static double calcularVolumenSimpson(String funcionF, String funcionG,
                                                 double a, double b, char variable) {
        double h = (b - a) / NUM_INTERVALOS;
        double suma = evaluarDiferenciaDiscos(funcionF, funcionG, a, variable) +
                evaluarDiferenciaDiscos(funcionF, funcionG, b, variable);

        // Términos pares
        for (int i = 2; i < NUM_INTERVALOS; i += 2) {
            double x = a + i * h;
            suma += 2 * evaluarDiferenciaDiscos(funcionF, funcionG, x, variable);
        }

        // Términos impares
        for (int i = 1; i < NUM_INTERVALOS; i += 2) {
            double x = a + i * h;
            suma += 4 * evaluarDiferenciaDiscos(funcionF, funcionG, x, variable);
        }

        return Math.PI * (h / 3) * suma;
    }

    private static double evaluarDiferenciaDiscos(String funcionF, String funcionG,
                                                  double valor, char variable) {
        try {
            double f = evaluarFuncion(funcionF, valor, variable);
            double g = evaluarFuncion(funcionG, valor, variable);
            return (f * f) - (g * g);
        } catch (Exception e) {
            Log.e(TAG, "Error al evaluar diferencia: " + e.getMessage());
            return 0;
        }
    }

    private static double evaluarFuncion(String funcion, double valor, char variable) {
        try {
            Expression expr = new ExpressionBuilder(funcion)
                    .variable(String.valueOf(variable))
                    .build()
                    .setVariable(String.valueOf(variable), valor);
            return expr.evaluate();
        } catch (Exception e) {
            Log.e(TAG, "Error al evaluar función: " + e.getMessage());
            return Double.NaN;
        }
    }

    private static void generarPuntosGrafica(String funcionF, String funcionG,
                                             double limInferior, double limiteSuperior,
                                             char variable, double[] puntosXF, double[] puntosYF,
                                             double[] puntosXG, double[] puntosYG) {
        double paso = (limiteSuperior - limInferior) / (NUM_PUNTOS_GRAFICA - 1);

        for (int i = 0; i < NUM_PUNTOS_GRAFICA; i++) {
            double valor = limInferior + i * paso;
            if (variable == 'x') {
                puntosXF[i] = valor;
                puntosYF[i] = evaluarFuncion(funcionF, valor, variable);
                puntosXG[i] = valor;
                puntosYG[i] = evaluarFuncion(funcionG, valor, variable);
            } else {
                puntosYF[i] = valor;
                puntosXF[i] = evaluarFuncion(funcionF, valor, variable);
                puntosYG[i] = valor;
            }
        }
    }
}
