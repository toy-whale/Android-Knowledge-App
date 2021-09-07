package com.java.qitianliang.SQLite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EntityDBManager {

    private static EntityDBManager sManager;

    private EntitySQLiteOpenHelper mHelper;

    private EntityDBManager(Context context, String username) {
        mHelper = new EntitySQLiteOpenHelper(context, username);
    }
    public static EntityDBManager getInstance(Context context, String username) {
        if (sManager == null) {
            synchronized (EntityDBManager.class) {
                if (sManager == null) {
                    sManager = new EntityDBManager(context, username);
                }
            }
        }
        return sManager;
    }

    public List<Entity> getAllEntity() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Entity order by time ASC", null);
        List<Entity> entityList = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex("subject"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range") String property = cursor.getString(cursor.getColumnIndex("property"));
            @SuppressLint("Range") String relative = cursor.getString(cursor.getColumnIndex("relative"));
            @SuppressLint("Range") String question = cursor.getString(cursor.getColumnIndex("question"));
            Entity entity = new Entity(name, subject, description, property, relative, question);
            entityList.add(entity);
        }
        mHelper.close();
        return entityList;
    }

    public void deleteAllEntity() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from Entity");
        mHelper.close();
    }

    public void deleteEntityByUri(String Name, String Course) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String u = Name + Course;
        db.execSQL("delete from Entity where uri = " + "'" + u + "'");
        mHelper.close();
    }

    public Entity getEntityByUri(String Name, String Course) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String u = Name + Course;
        Cursor cursor = db.rawQuery("select * from entity where uri = ?", new String[]{u});
        Entity entity = null;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex("subject"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range") String property = cursor.getString(cursor.getColumnIndex("property"));
            @SuppressLint("Range") String relative = cursor.getString(cursor.getColumnIndex("relative"));
            @SuppressLint("Range") String question = cursor.getString(cursor.getColumnIndex("question"));
            entity = new Entity(name, subject, description, property, relative, question);
            mHelper.close();
        }
        return entity;
    }

    public void insertAllEntity(List<Entity> entityList) {
        for (Entity entity : entityList) {
            insertEntity(entity);
        }
    }

    public void insertEntity(Entity entity) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String u = entity.getName() + entity.getSubject();
        Entity judge = getEntityByUri(entity.getName(), entity.getSubject());
        if (judge != null) return;
        db.execSQL("insert into entity(uri, name, time, subject, description, property, relative, question) values(?,?,?,?,?,?,?,?)", new Object[]{u, entity.getName(), System.currentTimeMillis(), entity.getSubject(), entity.getDescription(), entity.getProperty(), entity.getRelative(), entity.getQuestion()});
    }
}
