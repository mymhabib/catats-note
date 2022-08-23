package com.mym.catats;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNotesActivity extends AppCompatActivity {

    DataHelper dbHelper;
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText ETJudulTambah;
    EditText ETIsiTambah;
    String tanggalSekarang;
    FloatingActionButton simpan;
    ConstraintLayout layoutAdd;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        ETJudulTambah = (EditText) findViewById(R.id.judul_tambah);
        ETIsiTambah = (EditText) findViewById(R.id.isi_tambah);
        simpan = (FloatingActionButton) findViewById(R.id.fab_save_added_note);
        layoutAdd = (ConstraintLayout) findViewById(R.id.tambah_layout);
        dbHelper = new DataHelper(this);
        simpan.setOnClickListener(v -> {

            if (TextUtils.isEmpty(ETIsiTambah.getText()) && TextUtils.isEmpty(ETJudulTambah.getText())){
                AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(AddNotesActivity.this);
                myAlertBuilder.setTitle("Confirm");
                myAlertBuilder.setMessage("Are you sure you want to add empty note?");
                myAlertBuilder.setPositiveButton(Html.fromHtml("<font color='#000000'>Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertQuery();
                    }
                });
                myAlertBuilder.setNegativeButton(Html.fromHtml("<font color='#000000'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                myAlertBuilder.show();
            }
            else
                insertQuery();
        });
        layoutAdd.setOnTouchListener(new OnSwipeTouchListener(AddNotesActivity.this) {
            public void onSwipeRight() {
                finish();
            }
        });
    }
    public void insertQuery(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        String stringTanggal = formatter.format(date);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("INSERT INTO notes(judul, isi, tanggal)VALUES(?, ?, ?)");
        stmt.bindString(1, ETJudulTambah.getText().toString());
        stmt.bindString(2, ETIsiTambah.getText().toString());
        stmt.bindString(3, stringTanggal);
        stmt.execute();
        MainActivity.ma.refreshList();
        finish();
    }
}