package com.example.sqlcipherdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.List;
import java.util.Random;

/**
 * created by 103style  2019/5/1 22:29
 */
public class MainActivity extends AppCompatActivity {
    private TextView show;

    FtsDbDao ftsDbDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        net.sqlcipher.database.SQLiteDatabase.loadLibs(this);
        SupportSQLiteOpenHelper sqLiteOpenHelper = SqlCipherHelper.bulid(this);

        ftsDbDao = new FtsDbDao(sqLiteOpenHelper);
        show = findViewById(R.id.show);

        ftsDbDao.createTableIfNoExists();

        for (int i = 0 ;i < 100; i++){
            ftsDbDao.insertData("name_" + i, i);
        }

        List<String> result = ftsDbDao.search();
        Log.d("search result：", result.toString());
        if(result != null){
            Log.d("search result size ：", result.size() + "");
        }
    }
}
