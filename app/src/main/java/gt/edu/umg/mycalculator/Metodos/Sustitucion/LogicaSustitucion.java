package gt.edu.umg.mycalculator.Metodos.Sustitucion;



import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import android.util.Log;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;

public class LogicaSustitucion {
    private static final String TAG = "LogicaSustitucion";

    public static class ResultadoSustitucion {
        public final String integral;
        public final String pasos;
        public final boolean exito;
        public final String mensaje;
        public final String sustitucion;
        public final String derivadaSustitucion;

        private ResultadoSustitucion(String integral, String pasos, String sustitucion,
                                     String derivadaSustitucion, boolean exito, String mensaje) {
            this.integral = integral;
            this.pasos = pasos;
            this.sustitucion = sustitucion;
            this.derivadaSustitucion = derivadaSustitucion;
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public static ResultadoSustitucion error(String mensaje) {
            return new ResultadoSustitucion(null, null, null, null, false, mensaje);
        }

        public static ResultadoSustitucion exito(String integral, String pasos,
                                                 String sustitucion, String derivadaSustitucion) {
            return new ResultadoSustitucion(integral, pasos, sustitucion,
                    derivadaSustitucion, true, "");
        }
    }

    // Mapeo de patrones comunes de sustitución
    private static final Map<Pattern, SustitucionInfo> PATRONES_SUSTITUCION = new HashMap<>();
    static {
        // sin(x)
        PATRONES_SUSTITUCION.put(
                Pattern.compile("sin\\(x\\)(\\^[0-9]+)?"),
                new SustitucionInfo(
                        "u = sin(x)",
                        "cos(x)",
                        x -> "u^" + x + " * cos(x)",
                        x -> "-sin(x)"
                )
        );

        // cos(x)
        PATRONES_SUSTITUCION.put(
                Pattern.compile("cos\\(x\\)(\\^[0-9]+)?"),
                new SustitucionInfo(
                        "u = cos(x)",
                        "-sin(x)",
                        x -> "u^" + x + " * (-sin(x))",
                        x -> "cos(x)"
                )
        );

        // e^x
        PATRONES_SUSTITUCION.put(
                Pattern.compile("e\\^x"),
                new SustitucionInfo(
                        "u = e^x",
                        "e^x",
                        x -> "u",
                        x -> "e^x"
                )
        );

        // ln(x)
        PATRONES_SUSTITUCION.put(
                Pattern.compile("ln\\(x\\)(\\^[0-9]+)?"),
                new SustitucionInfo(
                        "u = ln(x)",
                        "1/x",
                        x -> "u^" + x + " * (1/x)",
                        x -> "ln(x)"
                )
        );
    }

    private static class SustitucionInfo {
        final String sustitucion;
        final String derivada;
        final FuncionTransformacion transformacion;
        final FuncionTransformacion antiderivada;

        SustitucionInfo(String sustitucion, String derivada,
                        FuncionTransformacion transformacion,
                        FuncionTransformacion antiderivada) {
            this.sustitucion = sustitucion;
            this.derivada = derivada;
            this.transformacion = transformacion;
            this.antiderivada = antiderivada;
        }
    }

    @FunctionalInterface
    private interface FuncionTransformacion {
        String aplicar(String exponente);
    }

    public static ResultadoSustitucion calcularIntegral(String funcion) {
        try {
            StringBuilder pasos = new StringBuilder();

            // Identificar el patrón de sustitución
            SustitucionInfo sustitucionInfo = null;
            Matcher matcherEncontrado = null;
            String exponente = "";

            for (Map.Entry<Pattern, SustitucionInfo> entrada : PATRONES_SUSTITUCION.entrySet()) {
                Matcher matcher = entrada.getKey().matcher(funcion);
                if (matcher.find()) {
                    sustitucionInfo = entrada.getValue();
                    matcherEncontrado = matcher;
                    if (matcher.groupCount() > 0 && matcher.group(1) != null) {
                        exponente = matcher.group(1).substring(1);
                    } else {
                        exponente = "1";
                    }
                    break;
                }
            }

            if (sustitucionInfo == null) {
                return ResultadoSustitucion.error("No se encontró un patrón de sustitución válido");
            }

            pasos.append("1. Hacemos la sustitución: ").append(sustitucionInfo.sustitucion).append("\n");
            pasos.append("2. Entonces du = ").append(sustitucionInfo.derivada).append(" dx\n");

            // Transformar la integral
            String integralTransformada = sustitucionInfo.transformacion.aplicar(exponente);
            pasos.append("3. La integral se transforma en: ∫").append(integralTransformada).append(" dx\n");

            // Resolver la integral transformada
            String resultado = resolverIntegralTransformada(integralTransformada, exponente);

            // Sustituir de vuelta
            String resultadoFinal = sustituirDeVuelta(resultado, sustitucionInfo.antiderivada.aplicar(exponente));
            pasos.append("4. Resultado final: ").append(resultadoFinal).append("\n");

            return ResultadoSustitucion.exito(
                    resultadoFinal,
                    pasos.toString(),
                    sustitucionInfo.sustitucion,
                    sustitucionInfo.derivada
            );

        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo de la integral por sustitución: " + e.getMessage());
            return ResultadoSustitucion.error("Error en el cálculo: " + e.getMessage());
        }
    }

    private static String resolverIntegralTransformada(String integralTransformada, String exponente) {
        // Implementación simplificada para casos comunes
        int exp = Integer.parseInt(exponente);
        if (exp == 1) {
            return "u";
        } else {
            return "u^" + (exp + 1) + "/" + (exp + 1);
        }
    }

    private static String sustituirDeVuelta(String resultado, String sustitucion) {
        return resultado.replace("u", "(" + sustitucion + ")") + " + C";
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