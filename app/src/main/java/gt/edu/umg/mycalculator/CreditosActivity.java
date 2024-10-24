package gt.edu.umg.mycalculator;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CreditosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        ScrollView scrollView = findViewById(R.id.scrollView);
        TextView tvCreditos = findViewById(R.id.tvCreditos);

        String creditosTexto = getString(R.string.creditos_texto);
        tvCreditos.setText(creditosTexto);

        scrollView.postDelayed(() -> scrollView.scrollTo(0, 0), 1000);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollBy(0, 5000);
                scrollView.postDelayed(this, 5000);
            }
        });
    }
}