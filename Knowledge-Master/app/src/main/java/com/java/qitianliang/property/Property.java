package com.java.qitianliang.property;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Property {
    private String label;
    private String object;

    public Property(JSONObject q) throws JSONException {
        this.label = q.getString("label");
        this.object = q.getString("object");
    }

    public String getLabel() {
        return label;
    }

    public String getObject() {
        return object;
    }
}
