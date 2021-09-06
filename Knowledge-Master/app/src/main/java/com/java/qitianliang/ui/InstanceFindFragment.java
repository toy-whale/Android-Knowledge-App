package com.java.qitianliang.ui;

import androidx.appcompat.widget.SearchView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.server.Login;
import com.java.qitianliang.server.InstanceList;
import com.java.qitianliang.ui.find_instance.FindAdapter;
import com.java.qitianliang.ui.find_instance.FindPairAdapter;
import com.java.qitianliang.ui.find_instance.Instance_find;
import com.java.qitianliang.ui.find_instance.Instance_find_pair;
import com.java.qitianliang.ui.linkinstance.Instance;
import com.java.qitianliang.ui.linkinstance.InstanceAdapter;
import com.java.qitianliang.ui.list_instance.Instance_list;
import com.java.qitianliang.ui.list_instance.Instance_list_pair;
import com.java.qitianliang.ui.list_instance.ListAdapter;
import com.java.qitianliang.ui.list_instance.ListPairAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class InstanceFindFragment extends Fragment {

    FindAdapter instance_adapter;
    FindPairAdapter instance_pair_adapter;
    NoScrollListview instance_listView;
    Spinner sort_option;
    Spinner display_option;
    SearchView search;

    public List<Instance_find> InstanceListSingle = new ArrayList<Instance_find>();
    public List<Instance_find_pair> InstanceListPair = new ArrayList<Instance_find_pair>();

    // sort and display
    public final int DEFALUT = 1;
    public final int CHAR = 2;
    public final int LENGTH = 3;
    public final int COLUMN = 4;
    public final int GRID = 5;
    public int currentSort = 1;
    public int currentDis = 4;

    public static InstanceFindFragment newInstance() {
        return new InstanceFindFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instance_find_fragment, container, false);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        search = view.findViewById(R.id.searchInstanceByWords);
        search.setSubmitButtonEnabled(true);
        TextView result = (TextView) view.findViewById(R.id.find_result);
        sort_option = view.findViewById(R.id.sortOptions_find);
        display_option = view.findViewById(R.id.displayOptions_find);

        instance_listView = view.findViewById(R.id.find_list_view);
        instance_adapter = new FindAdapter(getActivity(), R.layout.instance_find_item, InstanceListSingle);
        instance_pair_adapter = new FindPairAdapter(getActivity(), R.layout.instance_find_item_grid, InstanceListPair);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 调用实体查询接口
                int sort_pos = sort_option.getSelectedItemPosition();
                String sort_type = null;
                switch (sort_pos) {
                    case 0:
                        sort_type = "0";
                        break;
                    case 1:
                        sort_type = "2";
                        break;
                    case 2:
                        sort_type = "1";
                        break;
                    default:
                        break;
                }
                String ID = "";
                try {
                    ID = Login.get("14759265980", "Ee123456");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String answer = null;
                JSONObject x = null;
                try {
                    answer = InstanceList.get(MainActivity.currentSubject, query, ID, sort_type, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(answer != null) {
                    x = JSONObject.parseObject(answer);
                }
                InstanceListSingle.clear();
                InstanceListPair.clear();
                initInstances(x);

                // 展示返回数据
                result.setText("共检索到" + InstanceListSingle.size() + "个实体:");
                int dis_pos = display_option.getSelectedItemPosition();
                instance_listView.setAdapter(null);
                // 单列
                if (dis_pos == 0) {
                    instance_listView.setAdapter(instance_adapter);
                    instance_adapter.notifyDataSetChanged();
                }
                // 网格
                else if (dis_pos == 1) {
                    instance_listView.setAdapter(instance_pair_adapter);
                    instance_pair_adapter.notifyDataSetChanged();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    void initInstances(JSONObject x) {
        JSONArray data = x.getJSONArray("data");
        for(int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            Instance_find u = new Instance_find(y);
            InstanceListSingle.add(u);
        }
        int pairs = data.size() / 2;
        int i = 0;
        if (data.size() % 2 == 0) {
            for(i = 0; i < pairs * 2; i+=2) {
                JSONObject y = data.getJSONObject(i);
                JSONObject z = data.getJSONObject(i+1);
                Instance_find_pair u = new Instance_find_pair(y, z);
                InstanceListPair.add(u);
            }
        }
        else {
            for(i = 0; i < pairs * 2; i+=2) {
                JSONObject y = data.getJSONObject(i);
                JSONObject z = data.getJSONObject(i+1);
                Instance_find_pair u = new Instance_find_pair(y, z);
                InstanceListPair.add(u);
            }
            JSONObject y = data.getJSONObject(i);
            JSONObject z = null;
            Instance_find_pair u = new Instance_find_pair(y, z);
            InstanceListPair.add(u);
        }

    }

}