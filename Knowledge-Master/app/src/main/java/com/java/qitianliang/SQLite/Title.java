package com.java.qitianliang.SQLite;

public class Title { //收藏
    private String title;
    private String subject;
    public Title(String title, String subject) {
        this.title = title;
        this.subject = subject;
    }

    public  String getTitle() {
        return title;
    }

    public String getSubject() { return subject; }
}
