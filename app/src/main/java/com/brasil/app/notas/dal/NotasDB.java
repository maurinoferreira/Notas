package com.brasil.app.notas.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotasDB extends SQLiteOpenHelper {

    public NotasDB( Context context) {
        super(context, "Notas.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String instrucaoSql = "create table notas " +
                "(id integer primary key autoincrement," +
                "titulo text," +
                "nota text," +
                "data text)";

        sqLiteDatabase.execSQL(instrucaoSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
