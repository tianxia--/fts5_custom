package org.sqlite.customsqlitetest.benchmark;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.sqlite.customsqlitetest.LoadDialogUtils;
import org.sqlite.customsqlitetest.R;
import org.sqlite.customsqlitetest.sqlcipher.SQLiteDbHelper;
import org.sqlite.database.sqlite.SQLiteDatabase;

import java.io.File;

public class BenchmarkActivity extends AppCompatActivity {

    private static final String TAG = "BenchmarkActivity";
    private android.widget.EditText insert;
    private android.widget.EditText content;
    private android.widget.TextView result;
    private File dbFile;
    private SQLiteDatabase db;


    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);
        this.result = findViewById(R.id.result);
        this.content = findViewById(R.id.content);
        this.insert = findViewById(R.id.insert);

        dbFile = getApplication().getDatabasePath("test3.db");
        dbFile.getParentFile().mkdir();
    }

    static {
        System.loadLibrary("sqliteX");
    }

    public void initTable(View view) {
        if (db == null) {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            try {
                String nofts = "CREATE TABLE IF NOT EXISTS message(msg TEXT);";
                db.execSQL(nofts);
//                String fts = "CREATE VIRTUAL TABLE IF NOT EXISTS ftsmessage USING fts5(msg,tokenize='wcicu zh_CN');";
                String fts = "CREATE VIRTUAL TABLE IF NOT EXISTS ftsmessage USING fts5(msg,tokenize='unicode61');";
                db.execSQL(fts);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(View view) {
        if (db == null) return;
        String s = insert.getText().toString();
        if(TextUtils.isEmpty(s)){
            showToast("please input text");
            return;
        }
        int howMach = 1;
        try {
            howMach = Integer.parseInt(s);
        } catch (Exception e) {
            showToast("please input number text");
        }


        final int finalHowMach = howMach;
        final Dialog dialog = LoadDialogUtils.createLoadingDialog(this,null);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int r = 10000 * finalHowMach;
                ContentValues values = new ContentValues();
                db.beginTransaction();
                for (int i = 1; i <= r; i++) {
                    values.clear();
                    String msg = StringUtil.randomString();
                    values.put("msg", msg);
                    long ftsmessage = db.insert("ftsmessage", null, values);
                    long message = db.insert("message", null, values);
                    Log.i(TAG, i + "/" + r + ": " + msg);
                }

                appenMsg(":insert sucess!");
                db.setTransactionSuccessful();
                db.endTransaction();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LoadDialogUtils.closeDialog(dialog);
                    }
                });
            }
        });
        thread.start();
    }

    public void search(View view) {
        if (db == null) return;

        result.setText("");
        final String text = content.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            final Dialog dialog = LoadDialogUtils.createLoadingDialog(this,null);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor query = db.rawQuery("SELECT * FROM message WHERE msg LIKE '%" + text + "%'", null);
                    StringBuilder b = new StringBuilder("LIKE search Count:");
                    long start = System.currentTimeMillis();
                    int count = query.getCount();
                    long end = System.currentTimeMillis() - start;
                    b.append(count).append("\n")
                            .append("time:").append(end).append("ms");
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

    public void ftsSearch(View view) {
        if (db == null) return;

        result.setText("");
        final String text = content.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            final Dialog dialog = LoadDialogUtils.createLoadingDialog(this,null);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
//                    String sql = "SELECT * FROM ftsmessage WHERE ftsmessage MATCH ? " + text ;
//                    Cursor query = db.rawQuery(sql,null);
                    Cursor query = db.rawQuery("SELECT * FROM ftsmessage WHERE ftsmessage MATCH ?", new String[]{text});
                    StringBuilder b = new StringBuilder("MATCH search Count:");
                    long start = System.currentTimeMillis();
                    int count = query.getCount();
                    long end = System.currentTimeMillis() - start;
                    b.append(count).append("\n").append("time:").append(end).append("ms");
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

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void appenMsg(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                result.append(msg);
            }
        });
    }
}