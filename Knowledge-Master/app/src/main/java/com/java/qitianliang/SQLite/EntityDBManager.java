package com.java.qitianliang.SQLite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EntityDBManager implements IEntityDB {

    private static EntityDBManager sManager;

    private EntitySQLiteOpenHelper mHelper;

    private EntityDBManager(Context context) {
        mHelper = new EntitySQLiteOpenHelper(context);
    }
    public static EntityDBManager getInstance(Context context) {
        if (sManager == null) {
            synchronized (EntityDBManager.class) {
                if (sManager == null) {
                    sManager = new EntityDBManager(context);
                }
            }
        }
        return sManager;
    }
    /**
     * 获取所有条目
     */
    @Override
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
    /**
     * 删除所有条目
     */
    @Override
    public void deleteAllEntity() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from Entity");
        mHelper.close();
    }

    @Override
    public void deleteEntityByName(String Name) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from Entity where name = " + Name);
        mHelper.close();
    }

    @Override
    public Entity getEntityByName(String Name) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from entity where name = ?", new String[]{Name});
        cursor.moveToNext();
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
        @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex("subject"));
        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
        @SuppressLint("Range") String property = cursor.getString(cursor.getColumnIndex("property"));
        @SuppressLint("Range") String relative = cursor.getString(cursor.getColumnIndex("relative"));
        @SuppressLint("Range") String question = cursor.getString(cursor.getColumnIndex("question"));
        Entity entity = new Entity(name, subject, description, property, relative, question);
        mHelper.close();
        return entity;
    }

    @Override
    public void insertAllEntity(List<Entity> entityList) {
        for (Entity entity : entityList) {
            insertEntity(entity);
        }
    }

    @Override
    public void insertEntity(Entity entity) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into entity(name, time, subject, description, property, relative, question) values(?,?,?,?,?,?,?)", new Object[]{entity.getName(), System.currentTimeMillis(), entity.getDescription(), entity.getSubject(), entity.getProperty(), entity.getRelative(), entity.getQuestion()});
    }
}
