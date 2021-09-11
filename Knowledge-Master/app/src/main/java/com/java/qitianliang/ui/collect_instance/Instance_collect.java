package com.java.qitianliang.ui.collect_instance;

import com.alibaba.fastjson.JSONException;

public class Instance_collect {
    private String label;

    public Instance_collect(String name) throws JSONException {
        this.label = name;
    }

    public String getLabel() {
        return label;
    }
}
