package org.sqlite.customsqlitetest.sqlcipher;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SupportFactory;

import java.text.MessageFormat;

/**
 * created by 103style  2019/5/1 22:17
 */
public class SQLiteDbHelper {

    public static final String DB_NAME = "testCipher.db";

    public static final int DB_VERSION = 1;

    public static final String TABLE_TEST = "ftsmessage";
    private static final String SQL_PASSWORD = "12312sda";

    public static SupportSQLiteOpenHelper create(Context context) {
        SupportSQLiteOpenHelper.Configuration config = SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(DB_NAME)
                .callback(new SupportSQLiteOpenHelper.Callback(DB_VERSION) {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {

                    }

                    @Override
                    public void onUpgrade(@NonNull SupportSQLiteDatabase db, int oldVersion, int newVersion) {
                        // 数据库版本号变更会调用 onUpgrade 函数，在这根据版本号进行升级数据库
                        switch (oldVersion) {
                            case 1:
                                // do something
                                break;

                            default:
                                break;
                        }
                    }
                }).build();
        return new SupportFactory(SQL_PASSWORD.getBytes(), null, false).create(config);

    }


    public static void createTable(SupportSQLiteOpenHelper helper) {
        try {
            SupportSQLiteDatabase db = helper.getWritableDatabase();
            String nofts = "CREATE TABLE IF NOT EXISTS message(msg TEXT);";
            db.execSQL(nofts);

//            String fts = "CREATE VIRTUAL TABLE IF NOT EXISTS ftsmessage USING fts5(msg);";
//            String sql = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TABLE_TEST + " USING fts4(tokenize=unicode61);";

            String fts = "CREATE VIRTUAL TABLE IF NOT EXISTS ftsmessage USING fts5(content,tokenize=unicode61);";
            db.execSQL(fts);
        } finally {

        }
    }
}