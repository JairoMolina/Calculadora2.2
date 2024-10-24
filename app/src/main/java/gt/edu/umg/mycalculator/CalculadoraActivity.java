package gt.edu.umg.mycalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalculadoraActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_calculadora);

        Spinner spinnerintegrales = findViewById(R.id.spinner_Integrales);

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
                try {
                    Calculadora.setValorX(1);
                    Calculadora.setValorY(1);

                    double resultado = Calculadora.evaluar(expresion);
                    if (Double.isNaN(resultado)) {
                        Toast.makeText(CalculadoraActivity.this,
                                "Expresión matemática inválida",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String resultadoStr;
                        if (resultado == (long) resultado) {
                            resultadoStr = String.format("%d", (long) resultado);
                        } else {
                            resultadoStr = String.format("%.8f", resultado);
                        }
                        txtPantalla.setText(resultadoStr);
                    }
                } catch (Exception e) {
                    Toast.makeText(CalculadoraActivity.this,
                            "Error en la expresión",
                            Toast.LENGTH_SHORT).show();
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
        spinnerintegrales.setAdapter(adapter);


        spinnerintegrales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String seleccion = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Seleccionado: " + seleccion, Toast.LENGTH_SHORT).show();
                // Aquí puedes manejar cada opción seleccionada
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Código cuando no se selecciona nada
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
}
