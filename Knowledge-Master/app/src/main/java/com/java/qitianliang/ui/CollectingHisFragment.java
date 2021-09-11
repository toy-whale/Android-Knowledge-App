package com.java.qitianliang.ui;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.SQLite.Title;
import com.java.qitianliang.SQLite.TitleDBManager;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.ui.collect_instance.CollectAdapter;
import com.java.qitianliang.ui.collect_instance.Instance_collect;
import com.java.qitianliang.ui.list_instance.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollectingHisFragment extends Fragment {

    CollectAdapter instance_adapter;
    NoScrollListview instance_listView;
    TextView tips;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            reLoad();
        }
    };

    public List<Instance_collect> InstanceList = new ArrayList<>();

    public static CollectingHisFragment newInstance() {
        return new CollectingHisFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collecting_his_fragment, container, false);
        tips = (TextView) view.findViewById(R.id.collect_tips);
        instance_listView = (NoScrollListview) view.findViewById(R.id.collect_list_view);
        instance_adapter = new CollectAdapter(getActivity(), R.layout.instance_list_item, InstanceList);
        instance_listView.setAdapter(instance_adapter);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().getApplicationContext().registerReceiver(receiver, filter);

        // 不展示收藏
        if(MainActivity.loginUsername == null) {
            tips.setText("用户不存在,无法获取收藏记录!");
            return view;
        }
        // 加载显示
        reLoad();
        return view;
    }

    public void LoadAll() {
        TitleDBManager u = TitleDBManager.getInstance(getActivity(), MainActivity.loginUsername);
        List<Title> v = u.getAllTitle();
        InstanceList.clear();
        for (int i = 0; i < v.size(); i++) {
            Title y = v.get(i);
            if (y.getSubject().equals(MainActivity.currentSubject))
                InstanceList.add(new Instance_collect(y.getTitle()));
        }
        instance_listView.setAdapter(instance_adapter);
        instance_adapter.notifyDataSetChanged();
    }

    public void reLoad() {
        LoadAll();
        tips.setText("您共收藏了" + InstanceList.size() + "条" +
                MainActivity.transEng2Chi(MainActivity.currentSubject) + "学科的实体");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(receiver);
    }
}
