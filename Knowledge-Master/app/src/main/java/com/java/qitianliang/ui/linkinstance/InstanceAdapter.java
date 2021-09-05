package com.java.qitianliang.ui.linkinstance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.java.qitianliang.DetailsActivity;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;

import com.java.qitianliang.roundBackgroundColorSpan.*;
public class InstanceAdapter extends ArrayAdapter<Instance> {
    private final int resourceId;

    public InstanceAdapter(Context context, int textViewResourceId, List<Instance> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Instance instance = (Instance) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        //题目
        TextView entity = (TextView) view.findViewById(R.id.link_instance);
        TextView type = (TextView) view.findViewById(R.id.link_instance2);
        String Entity = instance.getLabel();
        String Type = instance.getObject();
        entity.setText(Entity);
        type.setText(Type);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailsActivity.class);
                intent.putExtra("name", Entity);
                intent.putExtra("course", MainActivity.currentSubject);
                intent.putExtra("is_collect","false");
                getContext().startActivity(intent);
            }
        });

        return view;
    }
}