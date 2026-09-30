package com.example.conversorbitcoin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final double COTACAO_BTC_EM_REAIS = 435000.00;

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private EditText etValor;
    private RadioGroup rgModo;
    private TextView tvLabelValor;
    private TextView tvResultado;
    private Button btnConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvCotacao = findViewById(R.id.tvCotacao);
        etValor = findViewById(R.id.etValor);
        rgModo = findViewById(R.id.rgModo);
        tvLabelValor = findViewById(R.id.tvLabelValor);
        tvResultado = findViewById(R.id.tvResultado);
        btnConverter = findViewById(R.id.btnConverter);

        String cotacaoFormatada = NumberFormat.getCurrencyInstance(PT_BR).format(COTACAO_BTC_EM_REAIS);
        tvCotacao.setText(getString(R.string.cotacao_info, cotacaoFormatada));

        rgModo.setOnCheckedChangeListener((group, checkedId) -> {
            boolean brlParaBtc = checkedId == R.id.rbBrlParaBtc;
            tvLabelValor.setText(brlParaBtc ? R.string.label_valor_brl : R.string.label_valor_btc);
            etValor.setText("");
            tvResultado.setText("");
        });


        btnConverter.setOnClickListener(v -> converter());
    }

    private void converter() {
        double valor;

        try {
            valor = lerNumero(etValor);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.erro_numero, Toast.LENGTH_SHORT).show();
            return;
        }

        NumberFormat moeda = NumberFormat.getCurrencyInstance(PT_BR);

        if (rgModo.getCheckedRadioButtonId() == R.id.rbBrlParaBtc) {
            double btc = valor / COTACAO_BTC_EM_REAIS;
            tvResultado.setText(String.format(PT_BR, "%s = %.8f BTC", moeda.format(valor), btc));
        } else {
            double reais = valor * COTACAO_BTC_EM_REAIS;
            tvResultado.setText(String.format(PT_BR, "%.8f BTC = %s", valor, moeda.format(reais)));
        }
    }
    private double lerNumero(EditText campo) {
        String texto = campo.getText().toString().trim();
        if (texto.isEmpty()) {
            throw new NumberFormatException("campo vazio");
        }
        if (texto.contains(",")) {
            texto = texto.replace(".", "").replace(",", ".");
        }
        return Double.parseDouble(texto);
    }
}
