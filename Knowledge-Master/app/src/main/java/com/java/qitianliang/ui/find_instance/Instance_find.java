package com.java.qitianliang.ui.find_instance;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Instance_find {
    private String label;
    private String category;

    public Instance_find(JSONObject q) throws JSONException {
        this.label = q.getString("label");
        this.category = q.getString("category");
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }
}
