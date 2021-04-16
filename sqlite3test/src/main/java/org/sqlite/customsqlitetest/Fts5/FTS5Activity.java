package org.sqlite.customsqlitetest.Fts5;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.sqlite.customsqlitetest.LoadDialogUtils;
import org.sqlite.customsqlitetest.R;
import org.sqlite.database.sqlite.SQLiteDatabase;

import java.io.File;

public class FTS5Activity extends Activity {

    private EditText search;
    private EditText insert;
    private TextView showing;
    private SQLiteDatabase db;
    private File dbFile;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fts5);
        this.showing = findViewById(R.id.result);
        this.search = findViewById(R.id.content);
        this.insert = findViewById(R.id.insert);

        load();
        dbFile = getApplicationContext().getDatabasePath("test2.db");
        dbFile.getParentFile().mkdirs();
    }

    public void search(View view) {
        if (db == null) return;
        showing.setText("");
        final String text = search.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            final Dialog dialog = LoadDialogUtils.createLoadingDialog(this,null);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor query = db.rawQuery("SELECT * FROM message WHERE message MATCH ?", new String[]{text});
                    StringBuilder b = new StringBuilder("Count:");
                    b.append(query.getCount()).append("\n");
                    while (query.moveToNext()) {
                        String s1 = query.getString(0);
                        b.append("content:").append(s1).append("\n");
                    }
                    appenMsg(b.toString());
                    query.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LoadDialogUtils.closeDialog(dialog);
                        }
                    });
                }
            });
            thread.start();
        }else{
            showToast("please input text");
        }
    }

    public void create(View view) {
        if (db == null) {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            try {
                String a = "CREATE VIRTUAL TABLE IF NOT EXISTS message USING fts5(msg,tokenize='wcicu zh_CN');";
                db.execSQL(a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void load() {
        try {
            System.loadLibrary("sqliteX");
        } catch (Exception e) {
            //igoned
        }
    }

    public void insert(View view) {
        if (db == null) {
            showToast("db object is null ");
            return;
        }
        String text = insert.getText().toString();
        ContentValues values = new ContentValues();
        values.put("msg", text);
        db.insert("message", null, values);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void appenMsg(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                showing.append(msg);
            }
        });
    }
}
