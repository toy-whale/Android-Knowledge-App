package com.java.qitianliang.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.ActivitySum;
import com.java.qitianliang.ActivityTest;
import com.java.qitianliang.ConnectChecker;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.SelectItem.*;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.server.LinkInstance;
import com.java.qitianliang.server.Login;
import com.java.qitianliang.server.Relatedsubject;
import com.java.qitianliang.server.TestQuestion;
import com.java.qitianliang.ui.TestSearchInstance.*;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeSumFragment extends Fragment {
    public static KnowledgeSumFragment newInstance() {
        return new KnowledgeSumFragment();
    }
    private int mPaddingSize;
    private List<Instance> InstanceList = new ArrayList<Instance>();
    private List<String> Results = new ArrayList<String>();
    private ArrayList<String> hotSearchTestList = new ArrayList<String>();
    private RecyclerView myItem;
    private AutoCompleteTextView mSearch;
    private NoScrollListview instance_listView;
    private TextView cleaner;
    private Paint myItemPaint;
    private Button searcher;
    private Button begin;
    private SelectItemAdapter mSelectItemAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { //联网成功
                Bundle bundle = msg.getData();
                String answer = bundle.getString("answer");
                JSONObject x = JSONObject.parseObject(answer);
                if(x == null) return;
                if(hotSearchTestList.size() == 0) {
                    Toast.makeText(getActivity(), "请选择知识点！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), ActivitySum.class);
                intent.putExtra("answer", answer);
                intent.putExtra("title",hotSearchTestList.get(0));
                startActivity(intent);
            } else if (msg.what == 0) { //联网失败
                Toast.makeText(getActivity(), "网络不太好呢~", Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.knowledge_sum_fragment, container, false);
        myItem = (RecyclerView) view.findViewById(R.id.select_item);
        cleaner = (TextView) view.findViewById(R.id.cleaner2);
        instance_listView = (NoScrollListview) view.findViewById(R.id.search_list_view2);
        searcher = (Button) view.findViewById(R.id.search_sum_points);
        mSearch = (AutoCompleteTextView) view.findViewById(R.id.searchSumPoints);
        begin = (Button) view.findViewById(R.id.begin_sum);
        cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hotSearchTestList = new ArrayList<String>();
                setPoints();
            }
        });
        searcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Key = mSearch.getText().toString();
                SearchItems(MainActivity.currentSubject, Key);
                initInstances();
            }
        });
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hotSearchTestList.size() == 0) {
                    Toast.makeText(getActivity(), "请选择知识点！", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                            String answer = null;
                            try {
                                answer = Relatedsubject.get(MainActivity.currentSubject, hotSearchTestList.get(0), ID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
    void setPoints() {
        myItem.setHasFixedSize(true);
        myItemPaint = new Paint();
        myItemPaint.setTextSize(40);
        mPaddingSize = 115;
        final int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), width);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int textWidth = (int) myItemPaint.measureText(hotSearchTestList.get(position)) + mPaddingSize;
                return textWidth > width ? width : textWidth;
            }
        });
        myItem.setLayoutManager(gridLayoutManager);
        mSelectItemAdapter = new SelectItemAdapter(hotSearchTestList);
        myItem.setAdapter(mSelectItemAdapter);
    }

    void initInstances() {
        InstanceList = new ArrayList<Instance>();
        for (int i = 0; i < Results.size(); i++) {
            String u = Results.get(i);
            int flag = 1;
            for (int j = 0; j < i; j++) {
                String v = Results.get(j);
                if (u.equals(v))
                    flag = 0;
            }
            if (flag == 1) {
                Instance x = new Instance(u);
                InstanceList.add(x);
            }
        }
        InstanceAdapter instance_adapter = new InstanceAdapter(getActivity(), R.layout.instance_link_item, InstanceList);
        instance_listView.setAdapter(instance_adapter);
        instance_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Instance e = InstanceList.get(i);
                if(hotSearchTestList.contains(e.getLabel())) {
                    Toast.makeText(getActivity(), "此知识点已添加！", Toast.LENGTH_LONG).show();
                }
                else if(hotSearchTestList.size() > 0) {
                    Toast.makeText(getActivity(), "最多只能添加一个知识点！", Toast.LENGTH_LONG).show();
                }
                else {
                    hotSearchTestList.add(e.getLabel());
                    setPoints();
                }
            }
        });
    }

    void SearchItems(String Course, String Key) {
        Results = new ArrayList<String>();
        if(Key.equals("") || Key == null) return;
        ArrayList<String> Bank = MainActivity.instanceListOfAll.get(Course);
        for(int i = 0;i < Bank.size();i++) {
            String Entity = Bank.get(i);
            if(Entity.contains(Key))
                Results.add(Bank.get(i));
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mSearch.setText("");
        hotSearchTestList = new ArrayList<String>();
        setPoints();
    }
}