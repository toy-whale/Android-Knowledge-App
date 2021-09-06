package com.java.qitianliang.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.SQLite.Entity;
import com.java.qitianliang.SQLite.EntityDBManager;
import com.java.qitianliang.SQLite.Title;
import com.java.qitianliang.SQLite.TitleDBManager;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.ui.list_instance.Instance_list;
import com.java.qitianliang.ui.list_instance.Instance_list_pair;
import com.java.qitianliang.ui.list_instance.ListAdapter;
import com.java.qitianliang.ui.list_instance.ListPairAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollectingHisFragment extends Fragment {

    ListAdapter instance_adapter;
    NoScrollListview instance_listView;
    TextView tips;

    public List<Instance_list> InstanceList = new ArrayList<Instance_list>();

    public static CollectingHisFragment newInstance() {
        return new CollectingHisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collecting_his_fragment, container, false);
        tips = (TextView) view.findViewById(R.id.collect_tips);
        instance_listView = (NoScrollListview) view.findViewById(R.id.collect_list_view);
        instance_adapter = new ListAdapter(getActivity(), R.layout.instance_list_item, InstanceList);
        instance_listView.setAdapter(instance_adapter);

        // 不展示收藏
        if(MainActivity.loginUsername == null) {
            tips.setText("用户不存在,无法获取收藏记录!");
            return view;
        }
        // 加载显示
        LoadAll();
        tips.setText("您共收藏了" + InstanceList.size() + "条实体信息:");

        return view;
    }

    void LoadAll() {
        TitleDBManager u = TitleDBManager.getInstance(getActivity(), MainActivity.loginUsername);
        List<Title> v = u.getAllTitle();
        InstanceList.clear();
        for (int i = 0; i < v.size(); i++) {
            Title y = v.get(i);
            InstanceList.add(new Instance_list(y.getTitle()));
        }
        instance_adapter.notifyDataSetChanged();
    }

}