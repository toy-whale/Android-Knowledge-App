package com.java.qitianliang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.question.Question;
import com.java.qitianliang.question.QuestionAdapter;
import com.java.qitianliang.question.TestAdapter;
import com.java.qitianliang.server.Login;
import com.java.qitianliang.server.TestQuestion;
import com.java.qitianliang.TreeElement.*;

import java.util.ArrayList;
import java.util.List;

public class ActivitySum extends AppCompatActivity {
    private ArrayList<Element> elements;
    private ArrayList<Element> elementsData;
    private String answer;
    private String title;
    private JSONObject points;
    private JSONObject subject;
    private JSONObject value;
    private TextView T;
    private int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        elements = new ArrayList<Element>();
        elementsData = new ArrayList<Element>();
        id = 0;
        Intent intent = getIntent();
        answer = intent.getStringExtra("answer");
        title = intent.getStringExtra("title");
        points = JSONObject.parseObject(answer);
        subject = JSONObject.parseObject(points.getString("subject"));
        value = JSONObject.parseObject(points.getString("value"));
        T = (TextView) findViewById(R.id.point_title);
        T.setText(title);
        if(subject != null && subject.keySet().size() > 0) initsubject();
        if(value != null && value.keySet().size() > 0) initvalue();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.show();
        //init();
        NoScrollListview treeview = (NoScrollListview) findViewById(R.id.tree);
        TreeViewAdapter treeViewAdapter = new TreeViewAdapter(
                elements, elementsData, inflater);
        TreeViewItemClickListener treeViewItemClickListener = new TreeViewItemClickListener(treeViewAdapter);
        treeview.setAdapter(treeViewAdapter);
        treeview.setOnItemClickListener(treeViewItemClickListener);
    }

    private void initsubject() {
        Element e = new Element(title + "为主语的相关知识", 0, id++, Element.NO_PARENT, true, false);
        elements.add(e);
        elementsData.add(e);
        for(String key : subject.keySet()) {
            if(key.equals("")) continue;
            Element u = new Element(key, 1, id++, e.getId(), true, false);
            elementsData.add(u);
            JSONArray y = subject.getJSONArray(key);
            for(int j = 0; j < y.size(); j++) {
                String point = y.getString(j);
                Element v = new Element(point, 2, id++, u.getId(), false, false);
                elementsData.add(v);
            }
        }
    }
    private void initvalue() {
        Element e = new Element(title + "为宾语的相关知识", 0, id++, Element.NO_PARENT, true, false);
        elements.add(e);
        elementsData.add(e);
        for(String key : value.keySet()) {
            if(key.equals("")) continue;
            Element u = new Element(key, 1, id++, e.getId(), true, false);
            elementsData.add(u);
            JSONArray y = value.getJSONArray(key);
            for(int j = 0; j < y.size(); j++) {
                String point = y.getString(j);
                Element v = new Element(point, 2, id++, u.getId(), false, false);
                elementsData.add(v);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
