package com.java.qitianliang.relative;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Relative {
    private String label;
    private String object;
    private String flag;
    private String name;

    public Relative(JSONObject q, String name) throws JSONException {
        this.label = q.getString("label");
        this.object = q.getString("object");
        this.flag = q.getString("flag");
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public String getObject() {
        return object;
    }

    public String getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

}

