package com.java.qitianliang.ui.collect_instance;

import android.annotation.SuppressLint;
import android.app.TaskInfo;
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
import com.java.qitianliang.SQLite.TitleDBManager;
import com.java.qitianliang.roundBackgroundColorSpan.*;
import com.java.qitianliang.ui.list_instance.Instance_list;

public class CollectAdapter extends ArrayAdapter<Instance_collect> {
    private final int resourceId;

    public CollectAdapter(Context context, int textViewResourceId, List<Instance_collect> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Instance_collect instance = (Instance_collect) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView entity = (TextView) view.findViewById(R.id.list_instance);
        String name = instance.getLabel();
        entity.setText(name);
        // 浏览记录检测
        if (MainActivity.loginUsername != null) {
            EntityDBManager manager = EntityDBManager.getInstance(getContext(), MainActivity.loginUsername);
            List<com.java.qitianliang.SQLite.Entity> e = manager.getAllEntity();
            for (int i = 0; i < e.size(); i++)
                if (e.get(i).getName().equals(name))
                    entity.setTextColor(R.color.purple_500);
        }
        // 收藏检测
        if (MainActivity.loginUsername != null) {
            TitleDBManager manager = TitleDBManager.getInstance(getContext(), MainActivity.loginUsername);
            List<com.java.qitianliang.SQLite.Title> t = manager.getAllTitle();
            boolean collected = false;
            for (int i = 0; i < t.size(); i++)
                if (t.get(i).getTitle().equals(name))
                    collected = true;
            if (!collected) {
                view.setVisibility(View.GONE);
                return view;
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.loginUsername != null)
                    entity.setTextColor(R.color.purple_500);
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