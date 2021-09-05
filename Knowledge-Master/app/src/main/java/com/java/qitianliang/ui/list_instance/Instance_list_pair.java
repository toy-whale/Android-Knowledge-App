package com.java.qitianliang.ui.list_instance;

import com.alibaba.fastjson.JSONException;

public class Instance_list_pair {
    private String label_left;
    private String label_right;

    public Instance_list_pair(String name_left, String name_right) throws JSONException {
        this.label_left = name_left;
        this.label_right = name_right;
    }

    public String getLabelLeft() {
        return label_left;
    }

    public String getLabelRight() {
        return label_right;
    }
}
