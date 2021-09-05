package com.java.qitianliang.SQLite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class Entity {
    private String name;
    private String subject;
    private String description;
    private String property;
    private String relative;
    private String question;
    public Entity(String name, String subject, String description, String property, String relative, String question) {
        this.name = name;
        this.subject = subject;
        this.description = description;
        this.property = property;
        this.relative = relative;
        this.question = question;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getProperty() {
        return property;
    }

    public String getRelative() {
        return relative;
    }

    public String getQuestion() {
        return question;
    }
}