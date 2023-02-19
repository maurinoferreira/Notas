package com.brasil.app.notas.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.brasil.app.notas.modelo.Nota;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class NotaDAO {

    private String mensagem;
    private Context contexto;

    public NotaDAO(Context contexto) {
        this.contexto = contexto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void cadastrarNota(Nota nota){
        setMensagem("");

        NotasDB notasDB = new NotasDB(contexto);
        SQLiteDatabase sqLiteDatabase = notasDB.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("titulo", nota.getTitulo());
        valores.put("nota", nota.getMensagem());
        valores.put("data", nota.getData());

        long resultado = sqLiteDatabase.insert("notas", null, valores);

        if(resultado == -1 )
        {
            this.mensagem = "Erro de BD";

        }
        else
        {
            this.mensagem = "Nota cadastrada";
        }

        notasDB.close();

    }

    public List<Nota> todasNotas()
    {
        this.mensagem = "";

        Nota nota;
        List<Nota> notas = new ArrayList<>();

        NotasDB notasDB = new NotasDB(contexto);
        SQLiteDatabase sqLite = notasDB.getReadableDatabase();
        Cursor cursor = sqLite.rawQuery("select * from notas order by id desc", null);

        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                nota = new Nota();
                nota.setId(cursor.getInt(0));
                nota.setTitulo(cursor.getString(1));
                nota.setMensagem(cursor.getString(2));
                nota.setData(cursor.getString(3));

                notas.add(nota);
                cursor.moveToNext();

            }
            cursor.close();
            notasDB.close();

        }
        else
            this.mensagem = "Nenhuma Nota";

        return notas;
    }

    public void alterarNota(Nota nota)
    {
        this.mensagem = "";

        try{

            NotasDB notasDB = new NotasDB(contexto);
            SQLiteDatabase sqLite = notasDB.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("titulo", nota.getTitulo());
            valores.put("nota", nota.getMensagem());
            valores.put("data", nota.getData());


            sqLite.update("notas", valores, "id = ?", new String[]{nota.getId().toString()});


            notasDB.close();

            setMensagem("Nota Alterada");

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }



    }

    public void deletarNota(Nota nota)
    {
        this.mensagem = "";


        try {

            NotasDB notasDB = new NotasDB(contexto);
            SQLiteDatabase sqLite = notasDB.getWritableDatabase();

            sqLite.delete("notas",  "id = ?", new String[]{nota.getId().toString()});

            notasDB.close();

            setMensagem("Nota Apagada");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }





    }


}
