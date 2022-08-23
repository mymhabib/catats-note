package com.mym.catats;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ContentActivity extends AppCompatActivity {

    DataHelper dbHelper;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected Cursor cursor;

    FloatingActionButton BEditAndSave;
    FloatingActionButton BDelete;
    EditText ETIsi;
    EditText ETJudul;
    TextView TVTanggal;
    ConstraintLayout layoutContent;
    boolean flag;
    int images[]={R.drawable.ic_baseline_done_24,R.drawable.ic_baseline_edit_24};
    int i = 0;
    private int idNote;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);


        ETIsi = (EditText) findViewById(R.id.isi);
        ETJudul = (EditText) findViewById(R.id.judulDetail);
        TVTanggal = (TextView) findViewById(R.id.tanggalDetail);
        nonEditableText();
        BDelete = (FloatingActionButton) findViewById(R.id.fab_delete);
        BEditAndSave = (FloatingActionButton) findViewById(R.id.fab_edit);
        layoutContent = (ConstraintLayout) findViewById(R.id.content_layout);

        flag=true;

        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM notes WHERE id_note" +
                " = '" +
                getIntent().getStringExtra("id_note") + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            idNote = Integer.parseInt(cursor.getString(0).toString());
            ETJudul.setText(cursor.getString(1).toString());
            TVTanggal.setText(cursor.getString(2).toString());
            ETIsi.setText(cursor.getString(3).toString());
        }

        layoutContent.setOnTouchListener(new OnSwipeTouchListener(ContentActivity.this) {
            public void onSwipeRight() {
                finish();
            }
        });



        BEditAndSave.setOnClickListener(v -> {
            BEditAndSave.setImageResource(images[i]);
            i++;
            if(i==2)
                i=0;
            if(ETIsi.isFocusable()) {
                nonEditableText();

            }
            else{
                EditableText();
            }
            try {
                if (i==0){
                    SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                    SQLiteStatement stmt = db1.compileStatement("update notes set judul= ? , isi= ? where id_note= ?");
                    stmt.bindString(1, ETJudul.getText().toString());
                    stmt.bindString(2, ETIsi.getText().toString());
                    stmt.bindString(3, String.valueOf(idNote));
                    stmt.execute();
                    Snackbar snackbar = Snackbar.make(layoutContent, "Note Saved", Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snackbar.show();

                }
            }catch (SQLException e) {
                }
            MainActivity.ma.refreshList();

        });



        BDelete.setOnClickListener(v -> {

            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(ContentActivity.this);
            myAlertBuilder.setTitle("Confirm");
            myAlertBuilder.setMessage(Html.fromHtml("<font color='#000000'>Are you sure you want to delete this note?</font><br><br><font color='#BEBEBE'>You can't undo this action.</font>"));
            myAlertBuilder.setPositiveButton(Html.fromHtml("<font color='#000000'>Yes</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db12 = dbHelper.getWritableDatabase();
                    db12.execSQL("DELETE FROM notes WHERE id_note = '"
                            + getIntent().getStringExtra("id_note") + "'");
                    MainActivity.ma.refreshList();
                    finish();
                }
            });
            myAlertBuilder.setNegativeButton(Html.fromHtml("<font color='#000000'>Cancel</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            myAlertBuilder.show();
        });
    }


    public void nonEditableText(){
        ETIsi.setFocusable(false);
        ETJudul.setFocusable(false);
        ETIsi.setCursorVisible(false);
        ETJudul.setCursorVisible(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(ContentActivity.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ETIsi.getWindowToken(), 0);
    }
    public void EditableText(){
        ETIsi.setFocusable(true);
        ETIsi.setFocusableInTouchMode(true);
        ETJudul.setFocusable(true);
        ETJudul.setFocusableInTouchMode(true);
        ETIsi.setCursorVisible(true);
        ETJudul.setCursorVisible(true);
        ETIsi.requestFocus();
        ETIsi.setSelection(ETIsi.getText().length());
        InputMethodManager imm = (InputMethodManager) getSystemService(ContentActivity.this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ETIsi, InputMethodManager.SHOW_IMPLICIT);
    }
}