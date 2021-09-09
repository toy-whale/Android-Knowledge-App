package com.java.qitianliang.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TitleSQLiteOpenHelper extends SQLiteOpenHelper {
    // 数据库名
    private static final String NAME = "C.db";
    // 数据库版本
    private static final int VERSION = 1;
    // 建表语句
    private static final String CREATE_Title = "create table if not exists Title(" +
            "uri text primary key," +
            "title text," +
            "time integer," +
            "subject text)";

    public TitleSQLiteOpenHelper(Context context, String username) {
        super(context, NAME + username, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Title);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
