package gt.edu.umg.mycalculator.Metodos.Trigonometricas;


import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LogicaTrigonometrica {
    private static final String TAG = "LogicaTrigonometrica";
    private static final int NUM_PUNTOS_GRAFICA = 200;

    public static class ResultadoIntegralTrig {
        public final String integral;
        public final String pasos;
        public final boolean exito;
        public final String mensaje;
        public final double[] puntosX;
        public final double[] puntosY;
        public final double limiteInferior;
        public final double limiteSuperior;

        private ResultadoIntegralTrig(String integral, String pasos, boolean exito, String mensaje,
                                      double[] puntosX, double[] puntosY,
                                      double limiteInferior, double limiteSuperior) {
            this.integral = integral;
            this.pasos = pasos;
            this.exito = exito;
            this.mensaje = mensaje;
            this.puntosX = puntosX;
            this.puntosY = puntosY;
            this.limiteInferior = limiteInferior;
            this.limiteSuperior = limiteSuperior;
        }

        public static ResultadoIntegralTrig error(String mensaje) {
            return new ResultadoIntegralTrig(null, null, false, mensaje,
                    null, null, 0, 0);
        }

        public static ResultadoIntegralTrig exito(String integral, String pasos,
                                                  double[] puntosX, double[] puntosY,
                                                  double limiteInferior, double limiteSuperior) {
            return new ResultadoIntegralTrig(integral, pasos, true, "",
                    puntosX, puntosY, limiteInferior, limiteSuperior);
        }
    }

    // Mapeo de integrales trigonométricas comunes
    private static final Map<Pattern, IntegralTrigInfo> INTEGRALES_TRIG = new HashMap<>();
    static {
        // sin(x)
        INTEGRALES_TRIG.put(
                Pattern.compile("sin\\(x\\)"),
                new IntegralTrigInfo("-cos(x)", "∫sin(x)dx = -cos(x) + C")
        );

        // cos(x)
        INTEGRALES_TRIG.put(
                Pattern.compile("cos\\(x\\)"),
                new IntegralTrigInfo("sin(x)", "∫cos(x)dx = sin(x) + C")
        );

        // sin^2(x)
        INTEGRALES_TRIG.put(
                Pattern.compile("sin\\(x\\)\\^2"),
                new IntegralTrigInfo("x/2 - sin(2*x)/4",
                        "∫sin²(x)dx = x/2 - sin(2x)/4 + C\n" +
                                "Usando la identidad: sin²(x) = (1 - cos(2x))/2")
        );

        // cos^2(x)
        INTEGRALES_TRIG.put(
                Pattern.compile("cos\\(x\\)\\^2"),
                new IntegralTrigInfo("x/2 + sin(2*x)/4",
                        "∫cos²(x)dx = x/2 + sin(2x)/4 + C\n" +
                                "Usando la identidad: cos²(x) = (1 + cos(2x))/2")
        );

        // sin(x)cos(x)
        INTEGRALES_TRIG.put(
                Pattern.compile("sin\\(x\\)\\*cos\\(x\\)"),
                new IntegralTrigInfo("-cos(2*x)/4",
                        "∫sin(x)cos(x)dx = -cos(2x)/4 + C\n" +
                                "Usando la identidad: sin(x)cos(x) = sin(2x)/2")
        );
    }

    private static class IntegralTrigInfo {
        final String resultado;
        final String explicacion;

        IntegralTrigInfo(String resultado, String explicacion) {
            this.resultado = resultado;
            this.explicacion = explicacion;
        }
    }

    public static ResultadoIntegralTrig resolverIntegral(String termino) {
        try {
            // Normalizar la expresión
            termino = termino.replace(" ", "").toLowerCase();

            // Buscar un patrón coincidente
            IntegralTrigInfo integralInfo = null;
            for (Map.Entry<Pattern, IntegralTrigInfo> entrada : INTEGRALES_TRIG.entrySet()) {
                if (entrada.getKey().matcher(termino).matches()) {
                    integralInfo = entrada.getValue();
                    break;
                }
            }

            if (integralInfo == null) {
                return ResultadoIntegralTrig.error("No se reconoce el patrón de integral trigonométrica");
            }

            // Calcular puntos para la gráfica
            double limiteInferior = -2 * Math.PI;
            double limiteSuperior = 2 * Math.PI;

            double[] puntosX = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosY = new double[NUM_PUNTOS_GRAFICA];

            double paso = (limiteSuperior - limiteInferior) / (NUM_PUNTOS_GRAFICA - 1);

            for (int i = 0; i < NUM_PUNTOS_GRAFICA; i++) {
                puntosX[i] = limiteInferior + i * paso;
                puntosY[i] = evaluarFuncion(termino, puntosX[i]);
            }

            return ResultadoIntegralTrig.exito(
                    integralInfo.resultado + " + C",
                    integralInfo.explicacion,
                    puntosX,
                    puntosY,
                    limiteInferior,
                    limiteSuperior
            );

        } catch (Exception e) {
            Log.e(TAG, "Error al resolver la integral trigonométrica: " + e.getMessage());
            return ResultadoIntegralTrig.error("Error en el cálculo: " + e.getMessage());
        }
    }

    private static double evaluarFuncion(String expresion, double x) {
        try {
            // Reemplazar funciones trigonométricas con sus equivalentes en exp4j
            expresion = expresion.replace("sin", "sin")
                    .replace("cos", "cos")
                    .replace("tan", "tan");

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

    public static boolean esFuncionTrigonometrica(String expresion) {
        expresion = expresion.toLowerCase();
        return expresion.contains("sin") ||
                expresion.contains("cos") ||
                expresion.contains("tan");
    }
}