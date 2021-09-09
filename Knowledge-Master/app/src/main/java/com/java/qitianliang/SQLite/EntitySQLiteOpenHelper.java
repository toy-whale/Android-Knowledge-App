package com.java.qitianliang.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EntitySQLiteOpenHelper extends SQLiteOpenHelper {
    // 数据库名
    private static final String NAME = "E.db";
    // 数据库版本
    private static final int VERSION = 1;
    // 建表语句
    private static final String CREATE_Entity = "create table if not exists Entity(" +
            "uri text primary key," +
            "name text," +
            "time integer," +
            "subject text," +
            "description text," +
            "property text," +
            "relative text," +
            "question text," +
            "image text)";

    public EntitySQLiteOpenHelper(Context context, String username) {
        super(context, NAME + username, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Entity);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
