package gt.edu.umg.mycalculator.Metodos.VolumenSolido;


import java.util.Scanner;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import android.util.Log;

public class Cascarones {
    private static final String TAG = "LogicaCascarones";
    private static final int NUM_INTERVALOS = 10000;
    private static final int NUM_PUNTOS_GRAFICA = 200;

    public static class ResultadoCascarones {
        public final double volumen;
        public final double[] puntosX;
        public final double[] puntosY;
        public final double limiteInferior;
        public final double limiteSuperior;
        public final boolean exito;
        public final String mensaje;
        public final char ejeRotacion;
        public final char variable;

        private ResultadoCascarones(double volumen, double[] puntosX, double[] puntosY,
                                    double limiteInferior, double limiteSuperior,
                                    boolean exito, String mensaje,
                                    char ejeRotacion, char variable) {
            this.volumen = volumen;
            this.puntosX = puntosX;
            this.puntosY = puntosY;
            this.limiteInferior = limiteInferior;
            this.limiteSuperior = limiteSuperior;
            this.exito = exito;
            this.mensaje = mensaje;
            this.ejeRotacion = ejeRotacion;
            this.variable = variable;
        }

        public static ResultadoCascarones error(String mensaje) {
            return new ResultadoCascarones(0, null, null, 0, 0,
                    false, mensaje, '\0', '\0');
        }

        public static ResultadoCascarones exito(double volumen,
                                                double[] puntosX, double[] puntosY,
                                                double limiteInferior, double limiteSuperior,
                                                char ejeRotacion, char variable) {
            return new ResultadoCascarones(volumen, puntosX, puntosY,
                    limiteInferior, limiteSuperior,
                    true, "", ejeRotacion, variable);
        }
    }

    public static ResultadoCascarones calcularVolumen(String funcion,
                                                      double limInferior, double limiteSuperior,
                                                      char ejeRotacion) {
        try {
            char variable = (ejeRotacion == 'x') ? 'y' : 'x';

            // Validar la función
            if (!validarFuncion(funcion, variable, (limInferior + limiteSuperior) / 2)) {
                return ResultadoCascarones.error("La función ingresada no es válida");
            }

            // Calcular el volumen usando el método de Simpson
            double volumen = calcularVolumenSimpson(funcion, limInferior, limiteSuperior, variable);

            // Generar puntos para graficar
            double[] puntosX = new double[NUM_PUNTOS_GRAFICA];
            double[] puntosY = new double[NUM_PUNTOS_GRAFICA];
            generarPuntosGrafica(funcion, limInferior, limiteSuperior, variable, puntosX, puntosY);

            return ResultadoCascarones.exito(
                    volumen,
                    puntosX, puntosY,
                    limInferior, limiteSuperior,
                    ejeRotacion, variable
            );

        } catch (Exception e) {
            Log.e(TAG, "Error en el cálculo del volumen: " + e.getMessage());
            return ResultadoCascarones.error("Error en el cálculo: " + e.getMessage());
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

    private static double calcularVolumenSimpson(String funcion, double a, double b, char variable) {
        double h = (b - a) / NUM_INTERVALOS;
        double suma = 0;

        // Primer término
        suma += evaluarFuncionCascaron(funcion, a, variable);

        // Términos intermedios pares
        for (int i = 2; i < NUM_INTERVALOS; i += 2) {
            double x = a + i * h;
            suma += 2 * evaluarFuncionCascaron(funcion, x, variable);
        }

        // Términos intermedios impares
        for (int i = 1; i < NUM_INTERVALOS; i += 2) {
            double x = a + i * h;
            suma += 4 * evaluarFuncionCascaron(funcion, x, variable);
        }

        // Último término
        suma += evaluarFuncionCascaron(funcion, b, variable);

        return 2 * Math.PI * (h / 3) * suma;
    }

    private static double evaluarFuncionCascaron(String funcion, double valor, char variable) {
        try {
            Expression expr = new ExpressionBuilder(funcion)
                    .variable(String.valueOf(variable))
                    .build()
                    .setVariable(String.valueOf(variable), valor);
            double resultado = expr.evaluate();
            return valor * resultado;
        } catch (Exception e) {
            Log.e(TAG, "Error al evaluar función: " + e.getMessage());
            return 0;
        }
    }

    private static void generarPuntosGrafica(String funcion, double limInferior, double limiteSuperior,
                                             char variable, double[] puntosX, double[] puntosY) {
        double paso = (limiteSuperior - limInferior) / (NUM_PUNTOS_GRAFICA - 1);

        for (int i = 0; i < NUM_PUNTOS_GRAFICA; i++) {
            double valor = limInferior + i * paso;
            if (variable == 'x') {
                puntosX[i] = valor;
                puntosY[i] = evaluarFuncion(funcion, valor, variable);
            } else {
                puntosY[i] = valor;
                puntosX[i] = evaluarFuncion(funcion, valor, variable);
            }
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
}