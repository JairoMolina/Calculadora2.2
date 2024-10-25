package gt.edu.umg.mycalculator;

import android.util.Log;

import android.util.Log;

public class IntegralCalculator {

    /**
     * Calcula el área bajo la curva usando el método del trapecio compuesto
     */
    public static double calcularAreaBajoCurva(String funcion, double a, double b, int n) {
        if (a >= b) throw new IllegalArgumentException("El límite inferior debe ser menor que el superior");

        double h = (b - a) / n;  // Tamaño del paso
        double suma = 0.0;

        // Log para depuración
        Log.d("Calculadora", "Calculando integral de " + funcion + " desde " + a + " hasta " + b);
        Log.d("Calculadora", "Tamaño de paso h = " + h);

        // Aplicar regla del trapecio
        for (int i = 0; i <= n; i++) {
            double x = a + i * h;
            CalculadoraActivity.Calculadora.setValorX(x);

            // Factor es 1.0 para los extremos, 2.0 para puntos intermedios
            double factor = (i == 0 || i == n) ? 1.0 : 2.0;

            double fx = CalculadoraActivity.Calculadora.evaluar(funcion);
            suma += factor * fx;

            // Log para depuración
            Log.d("Calculadora", "x = " + x + ", f(x) = " + fx);
        }

        double resultado = (h / 2.0) * suma;
        Log.d("Calculadora", "Resultado final = " + resultado);

        return resultado;
    }

    /**
     * Calcula una integral definida usando el método de Simpson compuesto
     */
    public static double calcularIntegralDefinida(String funcion, double a, double b, int n) {
        if (n % 2 != 0) n++; // Aseguramos que n sea par

        Log.d("Calculadora", "Calculando integral definida de: " + funcion);
        Log.d("Calculadora", "Límites: de " + a + " a " + b);
        Log.d("Calculadora", "Número de subdivisiones: " + n);

        double h = (b - a) / n;
        double suma = 0;

        // Primera evaluación en a
        CalculadoraActivity.Calculadora.setValorX(a);
        double fa = CalculadoraActivity.Calculadora.evaluar(funcion);
        Log.d("Calculadora", "f(" + a + ") = " + fa);

        // Suma de términos pares e impares
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            CalculadoraActivity.Calculadora.setValorX(x);
            double fx = CalculadoraActivity.Calculadora.evaluar(funcion);

            // Multiplicamos por 4 los términos impares y por 2 los pares
            double coef = (i % 2 == 0) ? 2 : 4;
            suma += coef * fx;

            Log.d("Calculadora", "f(" + x + ") = " + fx + " * " + coef);
        }

        // Última evaluación en b
        CalculadoraActivity.Calculadora.setValorX(b);
        double fb = CalculadoraActivity.Calculadora.evaluar(funcion);
        Log.d("Calculadora", "f(" + b + ") = " + fb);

        double resultado = (h/3) * (fa + suma + fb);
        Log.d("Calculadora", "Resultado final = " + resultado);

        return resultado;
    }

    /**
     * Calcula integrales impropias manejando diferentes casos:
     * 1. Límite superior infinito
     * 2. Límite inferior infinito
     * 3. Ambos límites infinitos
     * 4. Discontinuidad en los límites
     */
    public static double calcularIntegralImpropia(String funcion, double a, double b) {
        Log.d("Calculadora", "Calculando integral impropia de: " + funcion);
        Log.d("Calculadora", "Límites: de " + a + " a " + b);

        try {
            // Caso 1: Límite superior es infinito
            if (Double.isInfinite(b) && !Double.isInfinite(a)) {
                return calcularIntegralLimiteInfinitoSuperior(funcion, a);
            }
            // Caso 2: Límite inferior es infinito
            else if (Double.isInfinite(a) && !Double.isInfinite(b)) {
                return calcularIntegralLimiteInfinitoInferior(funcion, b);
            }
            // Caso 3: Ambos límites son infinitos
            else if (Double.isInfinite(a) && Double.isInfinite(b)) {
                return calcularIntegralLimitesInfinitos(funcion);
            }
            // Caso 4: Discontinuidad en uno de los límites
            else {
                return calcularIntegralDefinida(funcion, a, b, 1000);
            }
        } catch (Exception e) {
            Log.e("Calculadora", "Error en integral impropia", e);
            throw new RuntimeException("Error en el cálculo de la integral impropia: " + e.getMessage());
        }
    }

    private static double calcularIntegralLimiteInfinitoSuperior(String funcion, double a) {
        // Para integrales de a hasta infinito, usamos la sustitución t = 1/(1+x)
        final int PASOS = 1000;
        double suma = 0;
        double h = (1.0) / PASOS; // Intervalo [0,1] para t

        try {
            for (int i = 0; i <= PASOS; i++) {
                double t = i * h;
                if (t == 0) continue; // Evitar división por cero

                // La sustitución convierte x = (1-t)/t
                double x = (1-t)/t + a;
                CalculadoraActivity.Calculadora.setValorX(x);
                double fx = CalculadoraActivity.Calculadora.evaluar(funcion);

                // Factor del método del trapecio y el jacobiano dx/dt = -1/t^2
                double factor = (i == 0 || i == PASOS) ? 1 : 2;
                suma += factor * (fx / (t * t));
            }

            double resultado = h/2 * suma;
            Log.d("Calculadora", "Resultado integral impropia superior: " + resultado);
            return resultado;

        } catch (Exception e) {
            Log.e("Calculadora", "Error en integral impropia superior", e);
            throw e;
        }
    }

    private static double calcularIntegralLimiteInfinitoInferior(String funcion, double b) {
        // Para integrales desde -infinito hasta b
        final int PASOS = 1000;
        double suma = 0;
        double h = 1.0 / PASOS;

        try {
            Log.d("Calculadora", "Calculando integral impropia inferior de " + funcion);
            Log.d("Calculadora", "Desde -infinito hasta " + b);

            for (int i = 0; i <= PASOS; i++) {
                double t = i * h;  // t va de 0 a 1
                if (t == 0) continue;  // Evitar el punto donde x sería infinito

                // Sustitución: x = b - 1/t
                double x = b - 1/t;
                CalculadoraActivity.Calculadora.setValorX(x);
                double fx = CalculadoraActivity.Calculadora.evaluar(funcion);

                // El factor 2 es para el método del trapecio
                double factor = (i == 0 || i == PASOS) ? 1 : 2;
                // El jacobiano es 1/t²
                suma += factor * fx / (t * t);

                Log.d("Calculadora", "t=" + t + ", x=" + x + ", f(x)=" + fx);
            }

            double resultado = h/2 * suma;
            Log.d("Calculadora", "Resultado final: " + resultado);
            return resultado;

        } catch (Exception e) {
            Log.e("Calculadora", "Error en integral impropia inferior", e);
            throw e;
        }
    }
    //Aqui sirve
    //Ya no le heches para atras

    private static double calcularIntegralLimitesInfinitos(String funcion) {
        // Para integrales de -∞ a ∞, usamos la sustitución u = tan(t)
        final int PASOS = 1000;
        double suma = 0;
        double h = (Math.PI - (-Math.PI/2)) / PASOS;

        for (int i = 0; i <= PASOS; i++) {
            double t = -Math.PI/2 + i * h;
            double x = Math.tan(t);
            CalculadoraActivity.Calculadora.setValorX(x);
            double fx = CalculadoraActivity.Calculadora.evaluar(funcion);

            double factor = (i == 0 || i == PASOS) ? 1 : 2;
            suma += factor * (fx * (1 + x * x)); // Incluir el jacobiano dx/dt = sec²(t)
        }

        return (h/2) * suma;
    }

    /**
     * Calcula el valor promedio de una función en un intervalo
     */
    public static double calcularValorPromedio(String funcion, double a, double b) {
        double integral = calcularIntegralDefinida(funcion, a, b, 1000);
        return integral / (b - a);
    }
}