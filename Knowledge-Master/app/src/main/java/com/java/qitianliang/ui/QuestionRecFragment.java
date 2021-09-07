package com.java.qitianliang.ui;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.MainActivity;
import com.java.qitianliang.R;
import com.java.qitianliang.SQLite.Entity;
import com.java.qitianliang.SQLite.EntityDBManager;
import com.java.qitianliang.SQLite.Title;
import com.java.qitianliang.SQLite.TitleDBManager;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.question.Question;
import com.java.qitianliang.question.QuestionAdapter;
import com.java.qitianliang.server.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionRecFragment extends Fragment {

    public static QuestionRecFragment newInstance() {
        return new QuestionRecFragment();
    }
    private Button POST;
    private static JSONArray qset = new JSONArray();
    private List<Question> QuestionList = new ArrayList<Question>();
    static final int MAX = 1194;
    private static int tot = 0;
    static private List<String> Bank =  new ArrayList<String>();
    private ListView rec_listView;
    private static String username = MainActivity.loginUsername;
    private SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_rec_fragment, container, false);
        rec_listView = (ListView)view.findViewById(R.id.rec_list_view);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refreshing);
        InputStream inputStream = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        rec_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);
                if(firstVisibleItem ==0 && (firstView == null || firstView.getTop() == 0)) {
                    /*上滑到listView的顶部时，下拉刷新组件可见*/
                    refresh.setEnabled(true);
                } else {
                    /*不是listView的顶部时，下拉刷新组件不可见*/
                    refresh.setEnabled(false);
                }
            }
        });
        try {
            //得到资源中的Raw数据流
            inputStream = getResources().openRawResource(R.raw.bank);
            reader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(reader);
            String temp;
            while ((temp = bufferedReader.readLine()) != null && temp != "") {
                Bank.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        tot = 0;
        qset = new JSONArray();
        get();
        initQuestion(qset);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tot = 0;
                qset = new JSONArray();
                get();
                initQuestion(qset);
                refresh.setRefreshing(false);
            }
        });
        PrintAll();
        return view;
    }
    void initQuestion(JSONArray data) {
        QuestionList = new ArrayList<Question>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            String Body = y.getString("Body");
            Body = (i + 1) + "." + Body;
            y.put("Body", Body);
            Question u = new Question(y);
            QuestionList.add(u);
        }
        QuestionAdapter question_adapter = new QuestionAdapter(getActivity(), R.layout.question, QuestionList);
        //NoScrollListview rec_listView = (NoScrollListview) getActivity().findViewById(R.id.rec_list_view);
        rec_listView.setAdapter(question_adapter);
    }

    public static int[] getRandom(int length, int size) {
        int index[] = new int[size];
        int flag[] = new int[length];
        int cnt = 0;
        Random r = new Random();
        while (cnt < size) {
            int e = r.nextInt(length);
            if (flag[e] == 0) {
                index[cnt] = e;
                cnt++;
                flag[e] = 1;
            }
        }
        return index;
    }

    public static JSONArray shuffle(JSONArray x) { //随机打乱
        List<JSONObject> Jsons = new ArrayList<JSONObject>();
        JSONArray y = new JSONArray();
        for (int i = 0; i < x.size(); i++)
            Jsons.add(x.getJSONObject(i));
        Collections.shuffle(Jsons);
        for (int i = 0; i < x.size(); i++)
            y.add((JSONObject)Jsons.get(i));
        return y;
    }

    public void get() {
        EntityDBManager manager = EntityDBManager.getInstance(getActivity(), username);
        List<Entity> e = manager.getAllEntity();
        int u = Math.min(10, e.size());
        for(int i = u - 1; i >= 0; i--) {
            Entity item = e.get(i);
            JSONArray v = JSONArray.parseArray(item.getQuestion());
            if(v == null) continue;
            int num = v.size();
            num = num * 1 / 2;
            if(num > 2) num = 2;
            int index[] = getRandom(v.size(), num);
            for(int j = 0; j < num; j++) {
                JSONObject y = v.getJSONObject(index[j]);
                if(!qset.contains((y))) {
                    qset.add(y);
                    tot++;
                    if(tot == 7)
                        break;
                }
            }
            if(tot == 7)
                break;
        }
        RandomGet();
        qset = shuffle(qset);
    }
    public static void RandomGet() {
        Random r = new Random();
        while(tot < 10) { //随机抽题
            int e = r.nextInt(MAX);
            String x = Bank.get(e);
            JSONObject y = JSONObject.parseObject(x);
            if(!qset.contains(y)) {
                qset.add(y);
                tot++;
            }
        }
    }

    void PrintAll() {
        EntityDBManager manager = EntityDBManager.getInstance(getActivity(), username);
        List<Entity> e = manager.getAllEntity();
        System.out.println();
        for (int i = 0; i < e.size(); i++) {
            Entity y = e.get(i);
            System.out.println(i + 1 + ":");
            System.out.println(y.getName());
            System.out.println(y.getSubject());
            System.out.println(y.getDescription());
            System.out.println(y.getProperty());
            System.out.println(y.getRelative());
            System.out.println(y.getQuestion());
        }
        System.out.println("浏览记录输出结束!\n");
        TitleDBManager u = TitleDBManager.getInstance(getActivity(), username);
        List<Title> v = u.getAllTitle();
        for (int i = 0; i < v.size(); i++) {
            Title y = v.get(i);
            System.out.println(i + 1 + ": ");
            System.out.println(y.getTitle());
        }
        System.out.println("收藏记录输出结束!\n");
    }
}