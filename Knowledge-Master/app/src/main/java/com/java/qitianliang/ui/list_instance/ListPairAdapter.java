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

import org.w3c.dom.Text;

public class ListPairAdapter extends ArrayAdapter<Instance_list_pair> {
    private final int resourceId;

    public ListPairAdapter(Context context, int textViewResourceId, List<Instance_list_pair> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Instance_list_pair instance = (Instance_list_pair) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView t_left = (TextView) view.findViewById(R.id.list_instance_left);
        String name_l = instance.getLabelLeft();
        t_left.setText(name_l);
        TextView t_right = (TextView) view.findViewById(R.id.list_instance_right);
        String name_r = instance.getLabelRight();
        t_right.setText(name_r);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 // judge
                if (v.getId() == R.id.list_instance_left) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), DetailsActivity.class);
                    intent.putExtra("name", name_l);
                    intent.putExtra("course", MainActivity.currentSubject);
                    intent.putExtra("is_collect","false");
                    getContext().startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), DetailsActivity.class);
                    intent.putExtra("name", name_r);
                    intent.putExtra("course",MainActivity.currentSubject);
                    intent.putExtra("is_collect","false");
                    getContext().startActivity(intent);
                }
            }
        });

        return view;
    }
}