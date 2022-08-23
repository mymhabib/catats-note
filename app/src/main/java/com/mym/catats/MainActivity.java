package com.mym.catats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    String [] id, judul, tanggal, isi;
    ListView listView;
    protected Cursor cursor;
    DataHelper dbcenter;
    FloatingActionButton BTambah;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        ma = this;
        dbcenter = new DataHelper(this);
        refreshList();

        BTambah = (FloatingActionButton) findViewById(R.id.fab_create_new_note);
        BTambah.setOnClickListener(v -> addNote());

    }


    public void addNote(){
        Intent i = new Intent(getApplicationContext(), AddNotesActivity.class);
        startActivity(i);
    }

    public void refreshList(){
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM notes", null);
        id = new String[cursor.getCount()];
        judul = new String[cursor.getCount()];
        tanggal = new String[cursor.getCount()];
        isi = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            id[cc] = cursor.getString(0).toString();
            judul[cc] = cursor.getString(1).toString();
            tanggal[cc] = cursor.getString(2).toString();
            isi [cc] = cursor.getString(3).toString();
        }
        listView = (ListView)findViewById(R.id.list_item);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,id, judul, tanggal, isi);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            final String selection = id[arg2];
            Intent i = new Intent(getApplicationContext(), ContentActivity.class);
            i.putExtra("id_note", selection);
            startActivity(i);
        });

    }
    private class SimpleAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView tanggal, judul ,isi;
        private String[] tanggalArray;
        private String[] judulArray;
        private String[] idArray;
        private String[] isiArray;

        public SimpleAdapter(Context context, String [] id, String[] judul, String[] tanggal, String[] isi){
            mContext = context;
            idArray = id;
            tanggalArray = tanggal;
            judulArray = judul;
            isiArray = isi;
            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return judulArray.length;
        }

        @Override
        public Object getItem(int position) {
            return judulArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = layoutInflater.inflate(R.layout.main_activity_single_list, null);
            }

            judul = (TextView)convertView.findViewById(R.id.textJudul);
            tanggal = (TextView)convertView.findViewById(R.id.textTanggal);
            isi = (TextView)convertView.findViewById(R.id.textIsi);

            judul.setText(judulArray[position]);
            tanggal.setText(tanggalArray[position]);
            isi.setText(isiArray[position]);

            return convertView;
        }
    }
}