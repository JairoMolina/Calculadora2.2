package gt.edu.umg.mycalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_calculadora);

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
        btnIgual.setOnClickListener(v -> {


        });
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

        btnIgual.setOnClickListener(v -> {
            String expresion = txtPantalla.getText().toString();
            if (!expresion.isEmpty()) {
                // Verificar si hay un método seleccionado en el spinner
                if (spinner_Integrales.getSelectedItemPosition() > 0) {  // 0 sería "Seleccione un método" o el primer item
                    // Si hay un método seleccionado, mostrar diálogo de límites
                    mostrarDialogoLimites();

                    // Si no hay método seleccionado, realizar cálculo normal
                    try {
                        double limInf = Double.parseDouble(etlimiteInferior.getText().toString());
                        double limSup = Double.parseDouble(etLimiteSuperior.getText().toString());
                        calcularSegunMetodo(expresion, limInf, limSup);
                    } catch (NumberFormatException e) {
                        Toast.makeText(CalculadoraActivity.this,
                                "Ingrese límites válidos", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    try{
                        Calculadora.setValorX(1);
                        Calculadora.setValorY(1);
                    }catch (Exception e){
                        Toast.makeText(CalculadoraActivity.this,
                                "Error en el cálculo", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
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
    private static class Calculadora {
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
                expresion = preprocesarExpresion(expresion);
                return evaluarExpresion(expresion);
            } catch (Exception e) {
                return Double.NaN;
            }
        }

        private static String preprocesarExpresion(String expresion) {
            expresion = expresion.replace(" ", "");

            expresion = expresion.replace("X", String.valueOf(valorX));
            expresion = expresion.replace("Y", String.valueOf(valorY));
            expresion = expresion.replaceAll("(\\d)\\(", "$1*(");
            expresion = expresion.replaceAll("\\)(\\d)", ")*$1");
            expresion = expresion.replaceAll("\\)\\(", ")*(");

            if (expresion.startsWith("-")) {
                expresion = "0" + expresion;
            }

            return expresion;
        }

        private static double evaluarExpresion(String expresion) {
            int parentesis = 0;
            int pos = -1;
            for (int i = expresion.length() - 1; i >= 0; i--) {
                char c = expresion.charAt(i);
                if (c == ')') parentesis++;
                if (c == '(') parentesis--;
                if (parentesis == 0 && c == '^') {
                    pos = i;
                    break;
                }
            }

            if (pos > 0) {
                String base = expresion.substring(0, pos);
                String exponente = expresion.substring(pos + 1);
                return Math.pow(evaluarExpresion(base), evaluarExpresion(exponente));
            }
            parentesis = 0;
            pos = -1;
            for (int i = expresion.length() - 1; i >= 0; i--) {
                char c = expresion.charAt(i);
                if (c == ')') parentesis++;
                if (c == '(') parentesis--;
                if (parentesis == 0 && (c == '+' || (c == '-' && i > 0))) {
                    pos = i;
                    break;
                }
            }

            if (pos > 0) {
                String izquierda = expresion.substring(0, pos);
                String derecha = expresion.substring(pos + 1);
                if (expresion.charAt(pos) == '+') {
                    return evaluarExpresion(izquierda) + evaluarExpresion(derecha);
                } else {
                    return evaluarExpresion(izquierda) - evaluarExpresion(derecha);
                }
            }
            parentesis = 0;
            pos = -1;

            for (int i = expresion.length() - 1; i >= 0; i--) {
                char c = expresion.charAt(i);
                if (c == ')') parentesis++;
                if (c == '(') parentesis--;
                if (parentesis == 0 && (c == '*' || c == '/')) {
                    pos = i;
                    break;
                }
            }

            if (pos > 0) {
                String izquierda = expresion.substring(0, pos);
                String derecha = expresion.substring(pos + 1);
                if (expresion.charAt(pos) == '*') {
                    return evaluarExpresion(izquierda) * evaluarExpresion(derecha);
                } else {
                    double divisor = evaluarExpresion(derecha);
                    if (divisor == 0) throw new ArithmeticException("División por cero");
                    return evaluarExpresion(izquierda) / divisor;
                }
            }

            if (expresion.startsWith("sen")) {
                return Math.sin(evaluarExpresion(expresion.substring(3)));
            }
            if (expresion.startsWith("cos")) {
                return Math.cos(evaluarExpresion(expresion.substring(3)));
            }
            if (expresion.startsWith("ln")) {
                return Math.log(evaluarExpresion(expresion.substring(2)));
            }
            if (expresion.startsWith("√")) {
                return Math.sqrt(evaluarExpresion(expresion.substring(1)));
            }
            if (expresion.equals("π")) {
                return Math.PI;
            }
            if (expresion.equals("e")) {
                return Math.E;
            }

            if (expresion.startsWith("(") && expresion.endsWith(")")) {
                return evaluarExpresion(expresion.substring(1, expresion.length() - 1));
            }

            try {
                return Double.parseDouble(expresion);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Expresión inválida: " + expresion);
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
            switch (metodoSeleccionado) {
                case 0: // Área Bajo la Curva
                    LogicaAreaBajoCurva.ResultadoAreaBajoCurva resultado =
                            LogicaAreaBajoCurva.calcularArea(funcion, limInf, limSup);
                    if (resultado.exito) {
                        txtPantalla.setText(String.format("Área = %.4f", resultado.area));
                    } else {
                        mostrarError(resultado.mensaje);
                    }
                    break;

                case 1: // Centroides
                    LogicaCentroides.ResultadoCentroides resultadoCent =
                            LogicaCentroides.calcularCentroides(funcion, limInf, limSup);
                    if (resultadoCent.exito) {
                        txtPantalla.setText(String.format("Centro de masa:\nX̄ = %.4f\nȲ = %.4f",
                                resultadoCent.xBar, resultadoCent.yBar));
                    } else {
                        mostrarError(resultadoCent.mensaje);
                    }
                    break;

                case 2: // Definidas
                    LogicaDefinidas.ResultadoIntegral resultadoDef =
                            LogicaDefinidas.integrar(funcion, limInf, limSup,0,0, "x");
                    if (resultadoDef.exito) {
                        txtPantalla.setText(String.format("Integral = %.4f", resultadoDef.valor));
                    } else {
                        mostrarError(resultadoDef.mensaje);
                    }
                    break;

                // Agregar los demás casos según los métodos que adaptamos

                default:
                    mostrarError("Método no implementado aún");
                    break;
            }
        } catch (Exception e) {
            mostrarError("Error en el cálculo: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }




}
