package com.java.qitianliang.SQLite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TitleDBManager {

    private static TitleDBManager sManager;

    private TitleSQLiteOpenHelper mHelper;

    private TitleDBManager(Context context, String username) {
        mHelper = new TitleSQLiteOpenHelper(context, username);
    }

    public static TitleDBManager getInstance(Context context, String username) {
        if (sManager == null) {
            synchronized (TitleDBManager.class) {
                if (sManager == null) {
                    sManager = new TitleDBManager(context, username);
                }
            }
        }
        return sManager;
    }

    public List<Title> getAllTitle() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Title order by time ASC", null);
        List<Title> TitleList = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex("subject"));
            Title e = new Title(title, subject);
            TitleList.add(e);
        }
        //mHelper.close();
        return TitleList;
    }

    public void deleteAllTitle() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from Title");
        //mHelper.close();
    }

    public void deleteTitleByUri(String Name, String Course) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String u = Name + Course;
        db.execSQL("delete from Title where uri = " + "'" + u + "'");
        //mHelper.close();
    }

    public Title getTitleByUri(String Name, String Course) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String u = Name + Course;
        Cursor cursor = db.rawQuery("select * from Title where uri = ?", new String[]{u});
        Title e = null;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex("subject"));
            e = new Title(title, subject);
            //mHelper.close();
        }
        return e;
    }

    public void insertAllTitle(List<Title> TitleList) {
        for (Title Title : TitleList) {
            insertTitle(Title);
        }
    }

    public void insertTitle(Title title) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String u = title.getTitle() + title.getSubject();
        Title judge = getTitleByUri(title.getTitle(), title.getSubject());
        if (judge != null) return;
        db.execSQL("insert into Title(uri, title, time, subject) values(?,?,?,?)", new Object[]{u, title.getTitle(), System.currentTimeMillis(), title.getSubject()});
    }
}
