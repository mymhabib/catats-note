package com.mym.catats;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        String stringTanggal = formatter.format(date);
//        Log.d("mengecekSTR",stringTanggal.toString());
        String sql = "CREATE TABLE IF NOT EXISTS notes" +
                "(id_note INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "judul TEXT, " +
                "tanggal TEXT, " +
                "isi TEXT NULL);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);

        SQLiteStatement stmt = db.compileStatement("INSERT INTO notes (judul, tanggal, isi) VALUES (?,?,?);");
        stmt.bindString(1, "Create your first note");
        stmt.bindString(2, stringTanggal);
        stmt.bindString(3, "You can create notes by tapping the (+) button, then start typing what's on your mind. Or you can edit this note by tapping this then tap the edit button.");
        stmt.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
