package com.java.qitianliang.ui.list_instance;

import com.alibaba.fastjson.JSONException;

public class Instance_list {
    private String label;

    public Instance_list(String name) throws JSONException {
        this.label = name;
    }

    public String getLabel() {
        return label;
    }
}
