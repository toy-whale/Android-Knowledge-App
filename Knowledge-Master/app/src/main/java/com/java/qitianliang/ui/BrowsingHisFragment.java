package com.java.qitianliang.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.SQLite.Entity;
import com.java.qitianliang.SQLite.EntityDBManager;
import com.java.qitianliang.SQLite.Title;
import com.java.qitianliang.SQLite.TitleDBManager;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.ui.list_instance.Instance_list;
import com.java.qitianliang.ui.list_instance.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BrowsingHisFragment extends Fragment {

    ListAdapter instance_adapter;
    NoScrollListview instance_listView;
    TextView tips;

    public List<Instance_list> InstanceList = new ArrayList<Instance_list>();

    public static BrowsingHisFragment newInstance() {
        return new BrowsingHisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browsing_his_fragment, container, false);
        tips = (TextView) view.findViewById(R.id.browse_tips);
        instance_listView = (NoScrollListview) view.findViewById(R.id.browse_list_view);
        instance_adapter = new ListAdapter(getActivity(), R.layout.instance_list_item, InstanceList);
        instance_listView.setAdapter(instance_adapter);

        // 不展示浏览
        if(MainActivity.loginUsername == null) {
            tips.setText("用户不存在,无法获取浏览记录!");
            return view;
        }
        // 加载显示
        LoadAll();
        tips.setText("您过去浏览了" + InstanceList.size() + "条" +
                MainActivity.transEng2Chi(MainActivity.currentSubject) + "学科的实体");

        return view;
    }


    void LoadAll() {
        EntityDBManager manager = EntityDBManager.getInstance(getActivity(), MainActivity.loginUsername);
        List<Entity> e = manager.getAllEntity();
        InstanceList.clear();
        for (int i = 0; i < e.size(); i++) {
            Entity y = e.get(i);
            if (y.getSubject().equals(MainActivity.currentSubject))
                InstanceList.add(new Instance_list(y.getName()));
        }
        instance_adapter.notifyDataSetChanged();
    }
}