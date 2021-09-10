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
import com.java.qitianliang.ActivityTest;
import com.java.qitianliang.ConnectChecker;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.SelectItem.*;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.server.LinkInstance;
import com.java.qitianliang.server.Login;
import com.java.qitianliang.server.TestQuestion;
import com.java.qitianliang.ui.TestSearchInstance.*;

import java.util.ArrayList;
import java.util.List;

public class SpecificTestFragment extends Fragment {

    public static SpecificTestFragment newInstance() {
        return new SpecificTestFragment();
    }
    private int mPaddingSize;
    private List<Instance> InstanceList = new ArrayList<Instance>();
    private List<String> Results = new ArrayList<String>();
    private ArrayList<String> hotSearchTestList = new ArrayList<String>();
    private RecyclerView myItem;
    private AutoCompleteTextView mSearch;
    private NoScrollListview instance_listView;
    private TextView cleaner;
    private EditText test_number;
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
                String message = x.getString("msg");
                if(message.equals("1")) {
                    AlertDialog.Builder result = new AlertDialog.Builder(getActivity());
                    result.setTitle("警告");
                    result.setMessage("您选择生成的试题数量超过题库数量，请减少生成试题数量！");
                    result.setPositiveButton("确认",null);
                    result.show();
                }
                else {
                    String question = x.getString("data");
                    JSONArray u = JSONArray.parseArray(question);
                    if(u.size() == 0) {
                        AlertDialog.Builder result = new AlertDialog.Builder(getActivity());
                        result.setTitle("警告");
                        result.setMessage("很抱歉，题库中没有相关试题！");
                        result.setPositiveButton("确认",null);
                        result.show();
                    }
                    else {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), ActivityTest.class);
                        intent.putExtra("question", question);
                        startActivity(intent);
                    }
                }
            } else if (msg.what == 0) { //联网失败
                Toast.makeText(getActivity(), "网络不太好呢~", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.specific_test_fragment, container, false);
        myItem = (RecyclerView) view.findViewById(R.id.select_item_list);
        cleaner = (TextView) view.findViewById(R.id.cleaner);
        instance_listView = (NoScrollListview) view.findViewById(R.id.search_list_view);
        searcher = (Button) view.findViewById(R.id.search_points);
        mSearch = (AutoCompleteTextView) view.findViewById(R.id.searchPoints);
        begin = (Button) view.findViewById(R.id.begin);
        test_number = (EditText) view.findViewById(R.id.test_number);
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
                    Toast.makeText(getActivity(),"请至少选择一个知识点！",Toast.LENGTH_LONG).show();
                    return;
                }
                String number = test_number.getText().toString();
                if(number.equals("")) {
                    Toast.makeText(getActivity(),"请输入试题数量",Toast.LENGTH_LONG).show();
                    return;
                }
                if(Integer.parseInt(number) <= 0) {
                    Toast.makeText(getActivity(),"试题数量必须是正数！",Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        if (ConnectChecker.check(getActivity()) == false)
                            msg.what = 0;
                        else {
                            int number = Integer.parseInt(test_number.getText().toString());
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
                                answer = TestQuestion.get(hotSearchTestList, number, ID);
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
        test_number.setText("");
        mSearch.setText("");
        hotSearchTestList = new ArrayList<String>();
        setPoints();
    }
}