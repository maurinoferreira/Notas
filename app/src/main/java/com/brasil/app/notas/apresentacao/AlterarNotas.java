package com.brasil.app.notas.apresentacao;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.brasil.app.notas.R;
import com.brasil.app.notas.dal.NotaDAO;
import com.brasil.app.notas.modelo.Nota;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlterarNotas extends AppCompatActivity {

    private List<String> dadosNota = new ArrayList<>();
    private List<String> dados = new ArrayList<>();
    private Context context;
    private Nota nota;
    private EditText titulo;
    private EditText mensagem;
    private FloatingActionButton salvar;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_notas);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar!= null)
            actionBar.hide();

        configuracao();
        eventos();
    }
    private void configuracao(){
        context = getApplicationContext();
        titulo = findViewById(R.id.titulo_novo);
        mensagem = findViewById(R.id.novadescricao);
        salvar = findViewById(R.id.salvar_alteracao);

    }

    private void eventos(){


        try{
            dadosNota.add(getIntent().getStringExtra("id"));
            dadosNota.add(getIntent().getStringExtra("titulo"));
            dadosNota.add(getIntent().getStringExtra("nota"));
            dadosNota.add(getIntent().getStringExtra("data"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {

            if(dadosNota.size() > 0)
            {
                titulo.setText(dadosNota.get(1));
                mensagem.setText(dadosNota.get(2));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {

                    Date date = new Date();

                    CharSequence charSequence = DateFormat.format("dd MMMM yyyy", date.getTime());

                    NotaDAO notaDAO = new NotaDAO(context);


                    dados = new ArrayList<>();

                    dados.add(dadosNota.get(0));
                    dados.add(titulo.getText().toString());
                    dados.add(mensagem.getText().toString());
                    dados.add(charSequence.toString());

                    nota = new Nota();

                    nota.setId(Integer.parseInt(dados.get(0)));
                    nota.setTitulo(dados.get(1));
                    nota.setMensagem(dados.get(2));
                    nota.setData(dados.get(3));

                    notaDAO.alterarNota(nota);


                    if (notaDAO.getMensagem().equals("")) {
                        Toast.makeText(context, "Erro ao alterar a nota!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, notaDAO.getMensagem(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });


    }
}
