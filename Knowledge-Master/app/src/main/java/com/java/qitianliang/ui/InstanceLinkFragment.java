package com.java.qitianliang.ui;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.ConnectChecker;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.question.QuestionAdapter;
import com.java.qitianliang.relative.Relative;
import com.java.qitianliang.server.*;
import com.java.qitianliang.ui.linkinstance.Instance;
import com.java.qitianliang.ui.linkinstance.InstanceAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class InstanceLinkFragment extends Fragment {
    private List<Instance> InstanceList = new ArrayList<Instance>();

    public static InstanceLinkFragment newInstance() {
        return new InstanceLinkFragment();
    }

    private Button Post;
    private EditText Link_Text;
    private TextView link_number;
    private NoScrollListview instance_listView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { //联网成功
                Bundle bundle = msg.getData();
                String answer = bundle.getString("answer");
                JSONObject x = JSONObject.parseObject(answer);
                initInstances(x);
                InstanceAdapter instance_adapter = new InstanceAdapter(getActivity(), R.layout.instance_link_item, InstanceList);
                instance_listView.setAdapter(instance_adapter);
                if(InstanceList.size() == 0)
                    link_number.setText("共搜索到" + InstanceList.size() + "个实体。");
                else
                    link_number.setText("共搜索到" + InstanceList.size() + "个实体：");
                link_number.setVisibility(View.VISIBLE);
            } else if (msg.what == 0) { //联网失败
                Toast.makeText(getActivity(), "网络不太好呢~", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instance_link_fragment, container, false);
        Post = (Button) view.findViewById(R.id.ask_link);
        Link_Text = (EditText) view.findViewById(R.id.link_Text);
        link_number = (TextView) view.findViewById(R.id.link_number);
        instance_listView = (NoScrollListview) view.findViewById(R.id.link_list_view);
        //点击提交事件
        Post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InstanceList = new ArrayList<Instance>();
                String text = Link_Text.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(getActivity(), "文本不能为空", Toast.LENGTH_LONG).show();
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
                                answer = LinkInstance.get(MainActivity.currentSubject, text, ID);
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

    void initInstances(JSONObject x) {
        JSONArray data = x.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            int flag = 1;
            for(int j = 0; j < i; j++) {
                JSONObject v = data.getJSONObject(j);
                if(y.getString("entity").equals(v.getString("entity")))
                    flag = 0;
            }
            Instance u = new Instance(y);
            if(flag == 1)
                InstanceList.add(u);
        }
    }
}