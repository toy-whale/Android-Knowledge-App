package com.java.qitianliang.ui;

import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.ConnectChecker;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.server.LinkInstance;
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
    Spinner filter_option;
    Spinner display_option;
    AutoCompleteTextView search;
    AutoCompleteTextView keywords;
    String username = MainActivity.loginUsername;
    Button POST;
    TextView result;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { //联网成功
                Bundle bundle = msg.getData();
                String answer = bundle.getString("answer");
                JSONObject x = JSONObject.parseObject(answer);
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
            } else if (msg.what == 0) { //联网失败
                Toast.makeText(getActivity(), "网络不太好呢~", Toast.LENGTH_LONG).show();
            }
        }
    };

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

    // filter
    public int FILTER_OPTION = 0;

    public static InstanceFindFragment newInstance() {
        return new InstanceFindFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instance_find_fragment, container, false);

        search = view.findViewById(R.id.searchInstanceByWords);
        search.setText("");
        keywords = view.findViewById(R.id.searchInstanceByKeys);
        keywords.setText("");
        initsearch();
        sort_option = view.findViewById(R.id.sortOptions_find);
        filter_option = view.findViewById(R.id.filterOptions_find);
        display_option = view.findViewById(R.id.displayOptions_find);
        result = view.findViewById(R.id.find_result);

        instance_listView = view.findViewById(R.id.find_list_view);
        instance_adapter = new FindAdapter(getActivity(), R.layout.instance_find_item, InstanceListSingle);
        instance_pair_adapter = new FindPairAdapter(getActivity(), R.layout.instance_find_item_grid, InstanceListPair);

        filter_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 是否调出输入框
                switch (i) {
                    case 0:
                        FILTER_OPTION = 0;
                        keywords.setVisibility(View.GONE);
                        break;
                    case 1:
                        FILTER_OPTION = 1;
                        keywords.setVisibility(View.GONE);
                        break;
                    case 2:
                        FILTER_OPTION = 2;
                        keywords.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        POST = view.findViewById(R.id.search_btn);
        POST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveHistory("history");
                String query = search.getText().toString();
                if(query == null || query == "") return;
                new Thread() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        if (ConnectChecker.check(getActivity()) == false)
                            msg.what = 0;
                        else {
                            msg.what = 1;
                            String ID = "";
                            try {
                                ID = Login.get("14759265980", "Ee123456");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (ID == null || ID.equals(""))
                                msg.what = 0;
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
                            String answer = null;
                            try {
                                answer = InstanceList.get(MainActivity.currentSubject, query, ID, sort_type, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //if(answer == null)
                            //    msg.what = 0;
                            Bundle bundle = new Bundle();
                            bundle.putString("answer", answer);
                            msg.setData(bundle);
                        }
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });

        return view;
    }

    void initInstances(JSONObject x) {
        JSONArray data = x.getJSONArray("data");
        List<JSONObject> dataList = new ArrayList<>();
        dataList.clear();
        String searchInput = search.getText().toString();
        String keyInput = keywords.getText().toString();
        for(int i = 0; i < data.size(); i++) {
            JSONObject tmp = data.getJSONObject(i);
            Instance_find tmp0 = new Instance_find(tmp);
            boolean skip = false;
            // 过滤
            switch (FILTER_OPTION) {
                case 1:
                    if (!tmp0.getLabel().equals(searchInput))
                        skip = true;
                    break;
                case 2:
                    if (keyInput == null || keyInput.equals(""))
                        // 按模糊搜索处理
                        break;
                    if (!tmp0.getCategory().contains(keyInput))
                        skip = true;
                    break;
                default:
                    break;
            }
            if (!skip)
                dataList.add(tmp);
        }
        for(int i = 0; i < dataList.size(); i++) {
            Instance_find u = new Instance_find(dataList.get(i));
            InstanceListSingle.add(u);
        }
        int pairs = dataList.size() / 2;
        int i = 0;
        if (dataList.size() % 2 == 0) {
            for(i = 0; i < pairs * 2; i+=2) {
                Instance_find_pair u = new Instance_find_pair(dataList.get(i), dataList.get(i+1));
                InstanceListPair.add(u);
            }
        }
        else {
            for(i = 0; i < pairs * 2; i+=2) {
                Instance_find_pair u = new Instance_find_pair(dataList.get(i), dataList.get(i+1));
                InstanceListPair.add(u);
            }
            JSONObject z = null;
            Instance_find_pair u = new Instance_find_pair(dataList.get(i), z);
            InstanceListPair.add(u);
        }
    }

    private void initsearch() {
        String[] hisArrays = null;
        if(username != null) {
            SharedPreferences sp = getActivity().getSharedPreferences(username, Context.MODE_PRIVATE);
            String longhistory = sp.getString("history", null);
            if(longhistory != null)
                hisArrays = longhistory.split(",");
        }
        else
            hisArrays = null;
        ArrayAdapter<String> adapter;
        //只保留最近的5条的记录
        if(hisArrays != null) {
            if(hisArrays.length > 5){
                String[] newArrays = new String[5];
                System.arraycopy(hisArrays, 0, newArrays, 0, 5);
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.search_history, newArrays);
            }
            else
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.search_history, hisArrays);
            search.setAdapter(adapter);
        }
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                    if (hasFocus)
                        view.showDropDown();
            }
        });
    }
    private void saveHistory(String field) {
        if(username == null) return;
        String text = search.getText().toString();
        if(text == null || text == "") return;
        SharedPreferences sp = getActivity().getSharedPreferences(username, Context.MODE_PRIVATE);
        String longhistory = sp.getString(field, "");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        search.setText("");
    }
}
