package com.java.qitianliang.SQLite;

public class Title { //收藏
    private String title;
    private String subject;
    public Title(String title, String subject) {
        this.title = title;
        this.subject = subject;
    }

    public  String getTitle() {
        if (title == null || title.equals(""))
            return "null";
        return title;
    }

    public String getSubject() {
        if (subject == null || subject.equals(""))
            return "null";
        return subject;
    }
}
