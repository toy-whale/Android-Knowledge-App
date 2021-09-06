package com.java.qitianliang.ui.find_instance;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.java.qitianliang.DetailsActivity;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;

import com.java.qitianliang.roundBackgroundColorSpan.*;
import com.java.qitianliang.ui.list_instance.Instance_list;

import org.w3c.dom.Text;

public class FindPairAdapter extends ArrayAdapter<Instance_find_pair> {
    private final int resourceId;

    public FindPairAdapter(Context context, int textViewResourceId, List<Instance_find_pair> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Instance_find_pair instance = (Instance_find_pair) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView left1 = (TextView) view.findViewById(R.id.find_instance_left1);
        String name_l = instance.getLabelLeft();
        left1.setText(name_l);
        TextView left2 = (TextView) view.findViewById(R.id.find_instance_left2);
        String cate_l = instance.getCategoryLeft();
        left2.setText(cate_l);

        TextView right1 = (TextView) view.findViewById(R.id.find_instance_right1);
        String name_r = instance.getLabelRight();
        right1.setText(name_r);
        TextView right2 = (TextView) view.findViewById(R.id.find_instance_right2);
        String cate_r = instance.getCategoryRight();
        right2.setText(cate_r);

        if (name_r.equals("") && cate_r.equals(""))
            view.findViewById(R.id.hide_or_not).setVisibility(View.INVISIBLE);

        LinearLayout left = view.findViewById(R.id.always_show);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailsActivity.class);
                intent.putExtra("name", name_l);
                intent.putExtra("course", MainActivity.currentSubject);
                getContext().startActivity(intent);
            }
        });

        LinearLayout right = view.findViewById(R.id.hide_or_not);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(), DetailsActivity.class);
                intent.putExtra("name", name_r);
                intent.putExtra("course", MainActivity.currentSubject);
                getContext().startActivity(intent);
            }
        });

        return view;
    }
}