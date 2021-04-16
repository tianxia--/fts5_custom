package com.example.sqlcipherdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import net.sqlcipher.database.SupportFactory;

public class SqlCipherHelper {

    private final static String TAG = "SqlCipherHelper";
    private final static String dbName = "test.db";
    private final static int DB_VERSION = 1;
    private final static String PASSWORD = "123456";
    public static SupportSQLiteOpenHelper bulid(Context context) {
        SupportSQLiteOpenHelper.Configuration c = SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(dbName)
                .callback(new SupportSQLiteOpenHelper.Callback(DB_VERSION) {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {

                    }

                    @Override
                    public void onUpgrade(@NonNull SupportSQLiteDatabase db, int oldVersion, int newVersion) {

                    }
                })
                .build();

        return new SupportFactory(PASSWORD.getBytes()).create(c);
    }
}
