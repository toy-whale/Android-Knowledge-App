package com.java.qitianliang.ui.linkinstance;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Instance {
    private String entity;
    private String type;

    public Instance(JSONObject q) throws JSONException {
        this.entity = q.getString("entity");
        this.type = q.getString("type");
    }

    public String getLabel() {
        return entity;
    }

    public String getObject() {
        return type;
    }
}
