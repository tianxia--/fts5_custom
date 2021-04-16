package com.example.sqlcipherdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static net.sqlcipher.database.SQLiteDatabase.CONFLICT_REPLACE;

public class FtsDbDao {
    private SupportSQLiteOpenHelper sqLiteOpenHelper;
    private final static String TABLE_NAME = "person_table";

    private final static String FIELD_NAME = "name";
    private final static String FIELD_AGE = "age";

    public FtsDbDao(SupportSQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }


    public void createTableIfNoExists() {
        String sql = new StringBuilder()
                .append("CREATE VIRTUAL TABLE  IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append(" USING fts5(")
                .append(FIELD_NAME)
                .append(",")
                .append(FIELD_AGE)
                .append(",")
                .append("tokenize=unicode61);")
                .toString();

        Log.d("current sqlite version:", sqLiteOpenHelper.getWritableDatabase().getVersion() + "");

        sqLiteOpenHelper.getWritableDatabase().execSQL(sql);
    }

    public void insertData(String name, int age){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_NAME,name);
        contentValues.put(FIELD_AGE ,age);
        sqLiteOpenHelper.getWritableDatabase().insert(TABLE_NAME,CONFLICT_REPLACE,contentValues);
    }

    public List<String> search(){
        Cursor cursor =sqLiteOpenHelper.getWritableDatabase().query(new StringBuilder()
                .append(" SELECT ")
                .append(FIELD_NAME)
                .append(",")
                .append(FIELD_AGE)
                .append(" FROM ")
                .append(TABLE_NAME)
                .toString());

        List<String> result = new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(0);
            int age = cursor.getInt(1);

            result.add(name + "_" + age);
        }
        return result;
    }
}
