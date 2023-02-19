package com.brasil.app.notas.apresentacao;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brasil.app.notas.R;
import com.brasil.app.notas.dal.NotaDAO;
import com.brasil.app.notas.modelo.Nota;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdicionarNotas extends AppCompatActivity {

    private List<String> dados = new ArrayList<>();
    private Context context;
    private Nota nota;
    private EditText titulo;
    private EditText mensagem;
    private FloatingActionButton salvar;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_nota);

        configuracao();
        eventos();

        ActionBar actionBar = getSupportActionBar();

        if(actionBar!= null)
            actionBar.hide();





    }

    private void configuracao(){
        context = getApplicationContext();
        titulo = findViewById(R.id.titulo);
        mensagem = findViewById(R.id.descricao);
        salvar = findViewById(R.id.salvar);
    }

    private void eventos(){

        salvar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                try
                {

                    Date date = new Date();

                    CharSequence charSequence = DateFormat.format("dd MMMM yyyy", date.getTime());

                    NotaDAO notaDAO = new NotaDAO(context);


                    dados = new ArrayList<>();

                    dados.add("0");
                    dados.add(titulo.getText().toString());
                    dados.add(mensagem.getText().toString());
                    dados.add(charSequence.toString());

                    nota = new Nota();

                    nota.setId(Integer.parseInt(dados.get(0)));
                    nota.setTitulo(dados.get(1));
                    nota.setMensagem(dados.get(2));
                    nota.setData(dados.get(3));

                    notaDAO.cadastrarNota(nota);


                    if (notaDAO.getMensagem().equals("")) {
                        Toast.makeText(context, "Erro ao adicionar a nota!!", Toast.LENGTH_SHORT).show();
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