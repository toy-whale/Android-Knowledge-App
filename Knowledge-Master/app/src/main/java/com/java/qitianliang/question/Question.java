package com.java.qitianliang.question;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Question {
    private String body;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;
    private int imageId;

    public Question(JSONObject q) throws JSONException {
        this.body = q.getString("Body");
        this.A = q.getString("A");
        this.B = q.getString("B");
        this.C = q.getString("C");
        this.D = q.getString("D");
        this.answer = q.getString("qAnswer");
    }

    public String getBody() {
        return body;
    }

    public String getA() {
        return A;
    }

    public String getB() {
        return B;
    }

    public String getC() {
        return C;
    }

    public String getD() {
        return D;
    }

    public String getAnswer() {
        return answer;
    }
}
