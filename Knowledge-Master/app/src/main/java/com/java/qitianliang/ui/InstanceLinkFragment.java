package com.java.qitianliang.ui;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view = inflater.inflate(R.layout.instance_link_fragment, container, false);
        Button Post = (Button) view.findViewById(R.id.ask_link);
        EditText Link_Text = (EditText) view.findViewById(R.id.link_Text);
        TextView link_number = (TextView) view.findViewById(R.id.link_number);
        //点击提交事件
        Post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InstanceList = new ArrayList<Instance>();
                String text = Link_Text.getText().toString();
                if(text.equals("")) {
                    Toast.makeText(getActivity(), "文本不能为空", Toast.LENGTH_LONG).show();
                    return;
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
                    if (MainActivity.currentSubject == null)
                        answer = LinkInstance.get("chinese", text, ID);
                    else
                        answer = LinkInstance.get(MainActivity.currentSubject, text, ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(answer != null) {
                    x = JSONObject.parseObject(answer);
                }
                if(x == null) {
                    link_number.setText("共搜索到0个实体");
                    return;
                }
                initInstances(x);
                InstanceAdapter instance_adapter = new InstanceAdapter(getActivity(), R.layout.instance_link_item, InstanceList);
                NoScrollListview instance_listView = (NoScrollListview) view.findViewById(R.id.link_list_view);
                instance_listView.setAdapter(instance_adapter);
                link_number.setText("共搜索到" + InstanceList.size() + "个实体：");
                link_number.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
    void initInstances(JSONObject x) {
        JSONArray data = x.getJSONArray("data");
        for(int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            Instance u = new Instance(y);
            InstanceList.add(u);
        }
    }
}