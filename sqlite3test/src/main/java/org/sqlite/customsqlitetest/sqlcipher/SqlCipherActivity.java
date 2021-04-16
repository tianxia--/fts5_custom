package org.sqlite.customsqlitetest.sqlcipher;

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
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

import org.sqlite.customsqlitetest.LoadDialogUtils;
import org.sqlite.customsqlitetest.R;
import org.sqlite.customsqlitetest.benchmark.StringUtil;

import java.io.File;

public class SqlCipherActivity extends AppCompatActivity {

    private static final String TAG = "SqlCipherActivity";
    private EditText insert;
    private EditText content;
    private TextView result;
    private SupportSQLiteDatabase db;


    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SQLiteDatabase.loadLibs(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlcipher);
        this.result = findViewById(R.id.result);
        this.content = findViewById(R.id.content);
        this.insert = findViewById(R.id.insert);
        SupportSQLiteOpenHelper dbHelper = SQLiteDbHelper.create(this);
        SQLiteDbHelper.createTable(dbHelper);

        db = dbHelper.getWritableDatabase();
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
                    long ftsmessage = db.insert("ftsmessage", i, values);
                    long message = db.insert("message", i, values);
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

                    Cursor query = db.query("SELECT * FROM message WHERE msg LIKE '%" + text + "%'", null);
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
                    Cursor query = db.query("SELECT * FROM ftsmessage WHERE ftsmessage MATCH ?", new String[]{text});
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