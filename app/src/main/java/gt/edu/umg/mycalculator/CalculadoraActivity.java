package gt.edu.umg.mycalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import gt.edu.umg.mycalculator.Metodos.AreaBajoCurva.LogicaAreaBajoCurva;
import gt.edu.umg.mycalculator.Metodos.CentroidesyCentroides.LogicaCentroides;
import gt.edu.umg.mycalculator.Metodos.Definidas.LogicaDefinidas;

public class CalculadoraActivity extends AppCompatActivity {
    private LinearLayout layoutLimites;
    private EditText etlimiteInferior, etLimiteSuperior;
    private TextView txtPantalla;
    private Spinner spinner_Integrales;
    private int meotodosSeleccion=0;
    private String funcionActual="";
    private int metodoSeleccionado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        // Inicialización de vistas
        inicializarVistas();
        // Configuración de listeners
        configurarListeners();
        // Configuración del Spinner
        configurarSpinner();



       Spinner spinner_Integrales = findViewById(R.id.spinner_Integrales);
        Button btnRegresarcalcu = findViewById(R.id.btnRegresarcalcu);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btn0 = findViewById(R.id.btn0);
        Button btnPunto = findViewById(R.id.btnPunto);
        Button btnIgual = findViewById(R.id.btnIgual);
        Button btnX = findViewById(R.id.btnX);
        Button btnY = findViewById(R.id.btnY);
        Button btnElvar = findViewById(R.id.btnElevar);
        Button btnIntegral = findViewById(R.id.btnIntegral);
        Button btnParentesis1 = findViewById(R.id.btnParentesis1);
        Button btnParentesis2 = findViewById(R.id.btnParentesis2);
        Button btnValorAbsoluto = findViewById(R.id.btnValorAbsoluto);
        Button btnEuler = findViewById(R.id.btnEuler);
        Button btnSeno = findViewById(R.id.btnSeno);
        Button btnCoseno = findViewById(R.id.btnCoseno);
        Button btnLogaritmo = findViewById(R.id.btnLogaritmo);
        Button btnPi = findViewById(R.id.btnPi);
        Button btnMulti = findViewById(R.id.btnMulti);
        Button btnSuma = findViewById(R.id.btnSuma);
        Button btnResta = findViewById(R.id.btnResta);
        Button btnDivision = findViewById(R.id.btnDivision);
        Button btnRaiz = findViewById(R.id.btnRaiz);
        TextView txtPantalla = findViewById(R.id.txtPantalla);
        Button btnAC = findViewById(R.id.btnAC);
        Button btnBorrar = findViewById(R.id.btnBorrar);
        Button btnInfinito = findViewById(R.id.btnInfinito);
        Button btnFlecha = findViewById(R.id.btnFlecha);
        Button btnDX = findViewById(R.id.btnDx);
        layoutLimites = findViewById(R.id.layoutLimites);
        etlimiteInferior = findViewById(R.id.etLimiteInferior);
        etLimiteSuperior = findViewById(R.id.etLimiteSuperior);

        btnRegresarcalcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalculadoraActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn1.setOnClickListener(v -> txtPantalla.append("1"));
        btn2.setOnClickListener(v -> txtPantalla.append("2"));
        btn3.setOnClickListener(v -> txtPantalla.append("3"));
        btn4.setOnClickListener(v -> txtPantalla.append("4"));
        btn5.setOnClickListener(v -> txtPantalla.append("5"));
        btn6.setOnClickListener(v -> txtPantalla.append("6"));
        btn7.setOnClickListener(v -> txtPantalla.append("7"));
        btn8.setOnClickListener(v -> txtPantalla.append("8"));
        btn9.setOnClickListener(v -> txtPantalla.append("9"));
        btn0.setOnClickListener(v -> txtPantalla.append("0"));
        btnPunto.setOnClickListener(v -> txtPantalla.append("."));
        btnIgual.setOnClickListener(v -> calcular());

        btnX.setOnClickListener(v -> txtPantalla.append("X"));
        btnY.setOnClickListener(v -> txtPantalla.append("Y"));
        btnElvar.setOnClickListener(v -> txtPantalla.append("^"));
        btnIntegral.setOnClickListener(v -> txtPantalla.append("∫"));
        btnParentesis1.setOnClickListener(v -> txtPantalla.append("("));
        btnParentesis2.setOnClickListener(v -> txtPantalla.append(")"));
        btnValorAbsoluto.setOnClickListener(v -> txtPantalla.append("|"));
        btnEuler.setOnClickListener(v -> txtPantalla.append("e"));
        btnSeno.setOnClickListener(v -> txtPantalla.append("sen"));
        btnCoseno.setOnClickListener(v -> txtPantalla.append("cos"));
        btnLogaritmo.setOnClickListener(v -> txtPantalla.append("ln"));
        btnPi.setOnClickListener(v -> txtPantalla.append("π"));
        btnMulti.setOnClickListener(v -> txtPantalla.append("*"));
        btnSuma.setOnClickListener(v -> txtPantalla.append("+"));
        btnResta.setOnClickListener(v -> txtPantalla.append("-"));
        btnDivision.setOnClickListener(v -> txtPantalla.append("/"));
        btnRaiz.setOnClickListener(v -> txtPantalla.append("√"));
        btnBorrar.setOnClickListener(view -> txtPantalla.append(""));
        btnFlecha.setOnClickListener(view -> txtPantalla.append("➜"));
        btnInfinito.setOnClickListener(view -> txtPantalla.append("∞"));
        btnAC.setOnClickListener(v -> txtPantalla.setText(""));

        btnBorrar.setOnClickListener(view -> {
            String textoActual = txtPantalla.getText().toString();
            if (!textoActual.isEmpty()) {
                String nuevoTexto = textoActual.substring(0, textoActual.length() - 1);
                txtPantalla.setText(nuevoTexto);
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_integrales, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Integrales.setAdapter(adapter);
        spinner_Integrales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                metodoSeleccionado = position;
                layoutLimites.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                String seleccion = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Seleccionado: " + seleccion, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                metodoSeleccionado = 0;
                layoutLimites.setVisibility(View.GONE);
            }
        });
    }
    private void inicializarVistas() {
        layoutLimites = findViewById(R.id.layoutLimites);
        etlimiteInferior = findViewById(R.id.etLimiteInferior);
        etLimiteSuperior = findViewById(R.id.etLimiteSuperior);
        txtPantalla = findViewById(R.id.txtPantalla);
        spinner_Integrales = findViewById(R.id.spinner_Integrales);

        // Inicialmente ocultar el layout de límites
        layoutLimites.setVisibility(View.GONE);
    }
    private void configurarListeners() {
        // Configurar botones numéricos
        configurarBotonesNumericos();

        // Configurar botones de operaciones
        configurarBotonesOperaciones();

        // Configurar botón igual
        findViewById(R.id.btnIgual).setOnClickListener(v -> calcular());

        // Configurar botón borrar
        findViewById(R.id.btnBorrar).setOnClickListener(v -> {
            String textoActual = txtPantalla.getText().toString();
            if (!textoActual.isEmpty()) {
                txtPantalla.setText(textoActual.substring(0, textoActual.length() - 1));
            }
        });

        // Configurar botón AC
        findViewById(R.id.btnAC).setOnClickListener(v -> txtPantalla.setText(""));
    }

    private void configurarBotonesNumericos() {
        int[] botonesIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        for (int id : botonesIds) {
            Button boton = findViewById(id);
            boton.setOnClickListener(v -> txtPantalla.append(((Button) v).getText()));
        }
    }

    private void configurarBotonesOperaciones() {
        // Configurar cada botón de operación matemática
        configurarBotonOperacion(R.id.btnX, "x");
        configurarBotonOperacion(R.id.btnY, "y");
        configurarBotonOperacion(R.id.btnElevar, "^");
        configurarBotonOperacion(R.id.btnParentesis1, "(");
        configurarBotonOperacion(R.id.btnParentesis2, ")");
        configurarBotonOperacion(R.id.btnSeno, "sen");
        configurarBotonOperacion(R.id.btnCoseno, "cos");
        configurarBotonOperacion(R.id.btnMulti, "*");
        configurarBotonOperacion(R.id.btnSuma, "+");
        configurarBotonOperacion(R.id.btnResta, "-");
        configurarBotonOperacion(R.id.btnDivision, "/");
        configurarBotonOperacion(R.id.btnPunto, ".");
    }

    private void configurarBotonOperacion(int idBoton, String operacion) {
        findViewById(idBoton).setOnClickListener(v -> txtPantalla.append(operacion));
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_integrales, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Integrales.setAdapter(adapter);

        spinner_Integrales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                metodoSeleccionado = position;
                layoutLimites.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                metodoSeleccionado = 0;
                layoutLimites.setVisibility(View.GONE);
            }
        });
    }

    private void calcular() {
        funcionActual = txtPantalla.getText().toString();
        if (funcionActual.isEmpty()) {
            mostrarError("Ingrese una función");
            return;
        }

        if (metodoSeleccionado > 0) {
            try {
                String limInfStr = etlimiteInferior.getText().toString();
                String limSupStr = etLimiteSuperior.getText().toString();

                if (limInfStr.isEmpty() || limSupStr.isEmpty()) {
                    mostrarError("Ingrese ambos límites");
                    return;
                }

                double limInf = Double.parseDouble(limInfStr);
                double limSup = Double.parseDouble(limSupStr);

                calcularSegunMetodo(funcionActual, limInf, limSup);
            } catch (NumberFormatException e) {
                mostrarError("Límites inválidos");
            }
        } else {
            // Si no hay método seleccionado, mostrar mensaje
            mostrarError("Seleccione un método de integración");
        }
    }
    static class Calculadora {
        private static double valorX = 0;
        private static double valorY = 0;

        public static void setValorX(double x) {
            valorX = x;
        }

        public static void setValorY(double y) {
            valorY = y;
        }
        public static double evaluar(String expresion) {
            try {
                // Log para debug
                Log.d("Calculadora", "Evaluando expresión: " + expresion);

                expresion = preprocesarExpresion(expresion);
                Log.d("Calculadora", "Expresión preprocesada: " + expresion);

                double resultado = evaluarExpresion(expresion);
                Log.d("Calculadora", "Resultado de la evaluación: " + resultado);

                return resultado;
            } catch (Exception e) {
                Log.e("Calculadora", "Error al evaluar expresión", e);
                return Double.NaN;
            }
        }
        private static String preprocesarExpresion(String expresion) {
            // Log inicial
            Log.d("Calculadora", "Preprocesando expresión: " + expresion);

            // Eliminar espacios
            expresion = expresion.replace(" ", "");

            // Convertir explícitamente multiplicaciones implícitas
            expresion = expresion.replaceAll("(\\d)x", "$1*x");
            expresion = expresion.replaceAll("(\\d)X", "$1*X");

            // Reemplazar variables con sus valores
            expresion = expresion.replaceAll("x", String.valueOf(valorX));
            expresion = expresion.replaceAll("X", String.valueOf(valorX));

            // Manejar otras multiplicaciones implícitas
            expresion = expresion.replaceAll("(\\d)\\(", "$1*(");
            expresion = expresion.replaceAll("\\)(\\d)", ")*$1");
            expresion = expresion.replaceAll("\\)\\(", ")*(");

            // Manejar números negativos
            if (expresion.startsWith("-")) {
                expresion = "0" + expresion;
            }

            // Log final
            Log.d("Calculadora", "Expresión preprocesada: " + expresion);

            return expresion;
        }
        private static double evaluarExpresion(String expresion) {
            try {
                // Primero evaluar paréntesis
                while (expresion.contains("(")) {
                    int inicio = expresion.lastIndexOf("(");
                    int fin = expresion.indexOf(")", inicio);
                    if (fin == -1) throw new RuntimeException("Paréntesis no balanceados");

                    String subExpresion = expresion.substring(inicio + 1, fin);
                    double resultado = evaluarExpresion(subExpresion);

                    expresion = expresion.substring(0, inicio) +
                            resultado +
                            expresion.substring(fin + 1);
                }

                // Evaluar funciones especiales
                if (expresion.startsWith("sen")) {
                    return Math.sin(evaluarExpresion(expresion.substring(3)));
                }
                if (expresion.startsWith("cos")) {
                    return Math.cos(evaluarExpresion(expresion.substring(3)));
                }
                if (expresion.startsWith("ln")) {
                    double valor = evaluarExpresion(expresion.substring(2));
                    if (valor <= 0) throw new RuntimeException("Logaritmo de número no positivo");
                    return Math.log(valor);
                }
                if (expresion.startsWith("√")) {
                    double valor = evaluarExpresion(expresion.substring(1));
                    if (valor < 0) throw new RuntimeException("Raíz cuadrada de número negativo");
                    return Math.sqrt(valor);
                }

                // Evaluar operaciones básicas
                String[] operadores = {"+", "-", "*", "/", "^"};
                for (String operador : operadores) {
                    int pos = expresion.lastIndexOf(operador);
                    if (pos > 0) {
                        double izq = evaluarExpresion(expresion.substring(0, pos));
                        double der = evaluarExpresion(expresion.substring(pos + 1));

                        switch (operador) {
                            case "+": return izq + der;
                            case "-": return izq - der;
                            case "*": return izq * der;
                            case "/":
                                if (der == 0) throw new RuntimeException("División por cero");
                                return izq / der;
                            case "^": return Math.pow(izq, der);
                        }
                    }
                }

                // Constantes especiales
                if (expresion.equals("π")) return Math.PI;
                if (expresion.equals("e")) return Math.E;

                // Intentar convertir a número
                try {
                    return Double.parseDouble(expresion);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Expresión inválida: " + expresion);
                }
            } catch (Exception e) {
                Log.e("Calculadora", "Error en evaluarExpresion: " + expresion, e);
                throw e;
            }
        }
    }
    private void mostrarDialogoLimites() {
        View view = getLayoutInflater().inflate(R.layout.activity_calculadora, null);
        EditText etLimInf = view.findViewById(R.id.etLimiteInferior);
        EditText etLimSup = view.findViewById(R.id.etLimiteSuperior);

        new AlertDialog.Builder(this)
                .setTitle("Ingrese los límites")
                .setView(view)
                .setPositiveButton("Calcular", (dialog, which) -> {
                    try {
                        double limInf = Double.parseDouble(etLimInf.getText().toString());
                        double limSup = Double.parseDouble(etLimSup.getText().toString());
                        calcularSegunMetodo(funcionActual, limInf, limSup);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Límites inválidos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void calcularSegunMetodo(String funcion, double limInf, double limSup) {
        try {
            // Log para debug
            Log.d("Calculadora", "Función a evaluar: " + funcion);
            Log.d("Calculadora", "Límites: " + limInf + " a " + limSup);

            double resultado = 0;
            switch (metodoSeleccionado) {
                case 1: // Área bajo la curva
                    resultado = IntegralCalculator.calcularAreaBajoCurva(funcion, limInf, limSup, 1000);
                    break;
                case 2: // Integrales definidas
                    resultado = IntegralCalculator.calcularIntegralDefinida(funcion, limInf, limSup, 1000);
                    break;
                case 3: // Integrales impropias
                    resultado = IntegralCalculator.calcularIntegralImpropia(funcion, limInf, limSup);
                    break;
                case 5: // Valor promedio
                    resultado = IntegralCalculator.calcularValorPromedio(funcion, limInf, limSup);
                    break;
                default:
                    mostrarError("Método no implementado aún");
                    return;
            }

            // Log del resultado
            Log.d("Calculadora", "Resultado: " + resultado);

            if (Double.isNaN(resultado)) {
                mostrarError("Error en el cálculo. Verifique la función y los límites.");
                return;
            }

            mostrarResultado(resultado);
        } catch (Exception e) {
            Log.e("Calculadora", "Error en el cálculo", e);
            mostrarError("Error en el cálculo: " + e.getMessage());
        }
    }
    private void mostrarResultado(double resultado) {
        try {
            String resultadoFormateado;

            // Si el resultado es prácticamente cero (considerando errores de punto flotante)
            if (Math.abs(resultado) < 0.000001) {
                resultadoFormateado = "0";
            }
            // Si el resultado es prácticamente un número entero
            else if (Math.abs(resultado - Math.round(resultado)) < 0.000001) {
                resultadoFormateado = String.valueOf((int)Math.round(resultado));
            }
            // Para otros números, mostrar 2 decimales
            else {
                resultadoFormateado = String.format("%.2f", resultado);
            }

            txtPantalla.setText(resultadoFormateado);

            // Opcionalmente mostrar un Toast con el resultado
            Toast.makeText(this, "Resultado: " + resultadoFormateado, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            mostrarError("Error al mostrar el resultado: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}