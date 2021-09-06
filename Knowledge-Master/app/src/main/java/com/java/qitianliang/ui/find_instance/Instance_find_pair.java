package com.java.qitianliang.ui.find_instance;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class Instance_find_pair {
    private String label_left = "";
    private String label_right = "";
    private String category_left = "";
    private String category_right = "";

    public Instance_find_pair(JSONObject l, JSONObject r) throws JSONException {
        this.label_left = l.getString("label");
        this.category_left = l.getString("category");
        if (r != null) {
            this.label_right = r.getString("label");
            this.category_right = r.getString("category");
        }
    }

    public String getLabelLeft() {
        return label_left;
    }

    public String getLabelRight() {
        return label_right;
    }

    public String getCategoryLeft() {
        return category_left;
    }

    public String getCategoryRight() { return category_right; }
}
