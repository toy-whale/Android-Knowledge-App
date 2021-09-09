package com.java.qitianliang.ui.TestSearchInstance;

import com.alibaba.fastjson.JSONException;

public class Instance {
    private String label;

    public Instance(String name) throws JSONException {
        this.label = name;
    }

    public String getLabel() {
        return label;
    }
}
