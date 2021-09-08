package com.java.qitianliang.ui.TestSearchInstance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.java.qitianliang.DetailsActivity;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;

import com.java.qitianliang.SQLite.EntityDBManager;
import com.java.qitianliang.roundBackgroundColorSpan.*;
import com.java.qitianliang.ui.list_instance.Instance_list;

public class InstanceAdapter extends ArrayAdapter<Instance> {
    private final int resourceId;

    public InstanceAdapter(Context context, int textViewResourceId, List<Instance> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Instance instance = (Instance) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView entity = (TextView) view.findViewById(R.id.link_instance);
        String name = instance.getLabel();
        entity.setText(name);
        return view;
    }
}