package gt.edu.umg.mycalculator.Metodos.AreaBajoCurva;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class LogicaAreaBajoCurva {

    /**
     * Calcula el área bajo la curva usando el método de Simpson
     */
    public String calcularAreaBajoCurva(String funcion, double limiteInferior, double limiteSuperior, String variable) {
        try {
            // Número de subintervalos (debe ser par)
            int n = 1000;
            double h = (limiteSuperior - limiteInferior) / n;

            double suma = evaluarFuncion(funcion, variable, limiteInferior) +
                    evaluarFuncion(funcion, variable, limiteSuperior);

            // Suma los términos pares
            for (int i = 2; i < n; i += 2) {
                double x = limiteInferior + i * h;
                suma += 2 * evaluarFuncion(funcion, variable, x);
            }

            // Suma los términos impares
            for (int i = 1; i < n; i += 2) {
                double x = limiteInferior + i * h;
                suma += 4 * evaluarFuncion(funcion, variable, x);
            }

            double area = (h / 3) * suma;
            return String.format("El área bajo la curva desde %.2f hasta %.2f es: %.4f",
                    limiteInferior, limiteSuperior, area);

        } catch (Exception e) {
            return "Error al calcular el área bajo la curva: " + e.getMessage();
        }
    }

    /**
     * Evalúa la función en un punto x dado usando exp4j
     */
    private double evaluarFuncion(String funcion, String variable, double x) {
        try {
            Expression expression = new ExpressionBuilder(funcion)
                    .variable(variable)
                    .build();
            return expression.setVariable(variable, x).evaluate();
        } catch (Exception e) {
            throw new RuntimeException("Error al evaluar la función en x = " + x);
        }
    }
}