package gt.edu.umg.mycalculator;

import android.util.Log;

public class IntegralCalculator {

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

    public static double calcularIntegralDefinida(String funcion, double a, double b, int n) {
        if (n % 2 != 0) n++; // Aseguramos que n sea par

        double h = (b - a) / n;
        double suma = 0;

        for (int i = 2; i < n; i += 2) {
            CalculadoraActivity.Calculadora.setValorX(a + i*h);  // Corregido aquí
            suma += 2 * CalculadoraActivity.Calculadora.evaluar(funcion);  // Corregido aquí
        }

        for (int i = 1; i < n; i += 2) {
            CalculadoraActivity.Calculadora.setValorX(a + i*h);  // Corregido aquí
            suma += 4 * CalculadoraActivity.Calculadora.evaluar(funcion);  // Corregido aquí
        }

        CalculadoraActivity.Calculadora.setValorX(a);
        suma += CalculadoraActivity.Calculadora.evaluar(funcion);  // Corregido aquí
        CalculadoraActivity.Calculadora.setValorX(b);
        suma += CalculadoraActivity.Calculadora.evaluar(funcion);  // Corregido aquí

        return (h/3) * suma;
    }

    public static double calcularIntegralImpropia(String funcion, double a, double b) {
        if (Double.isInfinite(b)) {
            // Caso límite superior infinito
            return calcularIntegralDefinida(funcion, a, 100, 1000); // Aproximación
        } else if (Double.isInfinite(a)) {
            // Caso límite inferior infinito
            return calcularIntegralDefinida(funcion, -100, b, 1000); // Aproximación
        }
        return calcularIntegralDefinida(funcion, a, b, 1000);
    }

    public static double calcularValorPromedio(String funcion, double a, double b) {
        double integral = calcularIntegralDefinida(funcion, a, b, 1000);
        return integral / (b - a);
    }
}