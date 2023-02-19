package com.brasil.app.notas.apresentacao;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.brasil.app.notas.R;
import com.brasil.app.notas.dal.NotaDAO;
import com.brasil.app.notas.modelo.Nota;
import com.brasil.app.notas.modelo.NotasAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, MaterialSearchBar.OnSearchActionListener {

    private Context context;
    private RecyclerView recyclerView;
    private FloatingActionButton floatbuttom;
    private LinearLayout semNotas;
    private Intent intent;
    private NotasAdapter adapter;

    private MaterialSearchBar searchBar;

    ActionBar actionBar;

    @Override
    protected void onRestart() {
        super.onRestart();

        Notas notas = new Notas();
        notas.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configuracao();
        eventos();

        actionBar = getSupportActionBar();


        Notas notas = new Notas();
        notas.execute();

    }

    private void configuracao(){
        context = getApplicationContext();
        recyclerView = findViewById(R.id.recycleView);
        floatbuttom = findViewById(R.id.floatingActionButton);
        semNotas = findViewById(R.id.layout_sem_notas);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
    }

    private void eventos(){

        searchBar.setOnSearchActionListener(this);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)){
                    adapter.filter("");
                }
                else {
                    adapter.filter(editable.toString());
                }
            }


        });

        floatbuttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context,AdicionarNotas.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                searchBar.closeSearch();
                break;
        }
    }


    public class Notas extends AsyncTask<List<Nota>, Void, List<Nota>>
    {

        @Override
        protected List<Nota> doInBackground(List<Nota>... lists) {

           List<Nota> notas = new ArrayList<>();

            NotaDAO notaDAO = new NotaDAO(context);

            notas = notaDAO.todasNotas();

            return notas;
        }

        @Override
        protected void onPostExecute(List<Nota> notas) {
            super.onPostExecute(notas);

           if(notas.size() > 0)
           {

               if(actionBar != null)
                   actionBar.hide();

               searchBar.setVisibility(View.VISIBLE);
               semNotas.setVisibility(View.INVISIBLE);
               recyclerView.setVisibility(View.VISIBLE);
               LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
               recyclerView.setLayoutManager(layoutManager);

               adapter = new NotasAdapter(notas, context, MainActivity.this);

               recyclerView.setAdapter(adapter);
           }
           else {
               if(actionBar != null)
               {
                   actionBar.show();
                   actionBar.setTitle("Minhas Notas");
               }

               searchBar.setVisibility(View.GONE);
               semNotas.setVisibility(View.VISIBLE);
               recyclerView.setVisibility(View.INVISIBLE);

           }


        }



    }
}