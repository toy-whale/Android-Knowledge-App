package com.java.qitianliang.ui.list_instance;

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

import com.java.qitianliang.roundBackgroundColorSpan.*;
import com.java.qitianliang.ui.list_instance.Instance_list;

public class ListAdapter extends ArrayAdapter<Instance_list> {
    private final int resourceId;

    public ListAdapter(Context context, int textViewResourceId, List<Instance_list> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Instance_list instance = (Instance_list) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView entity = (TextView) view.findViewById(R.id.list_instance);
        String name = instance.getLabel();
        entity.setText(name);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("course", MainActivity.currentSubject);
                intent.putExtra("is_collect","false");
                getContext().startActivity(intent);
            }
        });

        return view;
    }
}