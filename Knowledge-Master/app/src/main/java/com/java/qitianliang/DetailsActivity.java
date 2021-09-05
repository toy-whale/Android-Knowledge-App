package com.java.qitianliang;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.*;
import android.view.*;

import com.alibaba.fastjson.JSON;
import com.java.qitianliang.property.Property;
import com.java.qitianliang.property.PropertyAdapter;
import com.java.qitianliang.question.Question;
import com.java.qitianliang.question.QuestionAdapter;
import com.java.qitianliang.relative.Relative;
import com.java.qitianliang.relative.RelativeAdapter;
import com.java.qitianliang.server.*;
import com.java.qitianliang.noScrollListview.NoScrollListview;
import com.java.qitianliang.SQLite.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java.qitianliang.ui.linkinstance.InstanceAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private RatingBar collect;
    private TextView title;
    private TextView description;
    private List<Question> QuestionList = new ArrayList<Question>();
    private List<Property> PropertyList = new ArrayList<Property>();
    private List<Relative> RelativeList = new ArrayList<Relative>();
    private String ID;
    private String Name;
    private String Course;
    private String Description = "";
    private JSONObject Questions = null;
    private JSONObject Result = null;
    private String is_collect = "false"; //是否收藏
    private
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { //联网成功
                Bundle bundle = msg.getData();
                String result = bundle.getString("result");
                String question = bundle.getString("question");
                Result = JSONObject.parseObject(result);
                Questions = JSONObject.parseObject(question);
            } else if (msg.what == 0) { //联网失败
                Toast.makeText(getApplicationContext(), "网络不太好呢~", Toast.LENGTH_LONG).show();
            }
            initdata();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        Course = intent.getStringExtra("course");
        is_collect = intent.getStringExtra("is_collect");
        //允许主线程访问网络
        //StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        //设置标题
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                if (ConnectChecker.check(getApplicationContext()) == false)
                    msg.what = 0;
                else {
                    msg.what = 1;
                    String ID = "";
                    try {
                        ID = Login.get("14759265980", "Ee123456");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (ID.equals(""))
                        msg.what = 0;
                    else {
                        String result = "";
                        try {
                            result = InfoByInstanceName.get(Course, Name, ID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String question = "";
                        try {
                            question = QuestionListByUriName.get(Name, ID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("result", result);
                        bundle.putString("question", question);
                        msg.setData(bundle);
                    }
                }
                handler.sendMessage(msg);
            }
        }.start();
    }
    //右上选项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details,menu);
        if(is_collect.equals("true")) {
            menu.findItem(R.id.collect).setIcon(android.R.drawable.btn_star_big_on);
        }
        else
            menu.findItem(R.id.collect).setIcon(android.R.drawable.btn_star_big_off);
        return true;
    }
    //处理方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.collect: //收藏
                if(is_collect.equals("false")) {
                    Toast.makeText(this, "已收藏！", Toast.LENGTH_LONG).show();
                    is_collect = "true";
                    item.setIcon(android.R.drawable.btn_star_big_on);
                }
                else {
                    Toast.makeText(this, "取消收藏！", Toast.LENGTH_LONG).show();
                    is_collect = "false";
                    item.setIcon(android.R.drawable.btn_star_big_off);
                }
                break;
            case R.id.share: //分享到新浪微博
                Toast.makeText(this, "分享到新浪微博！", Toast.LENGTH_LONG).show();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private String getID() {
        String id = "";
        try {
            id = Login.get("14759265980", "Ee123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private String findDescription() {
        String D = "无相关描述！";
        JSONArray data = Result.getJSONArray("property");
        for(int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            if(y.getString("label").equals("定义"))
                D = y.getString("object");
        }
        if(D == null) D = "无相关描述！";
        return "      " + D;
    }

    private void initdata() {
        if(true) {  //如果是在线状态
            //获取登录状态码并从接口获取数据
            //ID = getID();
            //Result = getInfo(Course, Name, ID);
            //Questions = getQuestion(Name, ID);
            //Description = findDescription();
            /*EntityDBManager manager = EntityDBManager.getInstance(this);
            manager.deleteEntityByName(Name);
            String name = Name;
            String subject = Course;
            String description = Description;
            String property = Result.getJSONArray("property").toString();
            String relative = Result.getJSONArray("content").toString();
            String question = Questions.getJSONArray("data").toString();
            Entity e = new Entity(name, subject,description, property, relative, question);
            manager.deleteEntityByName(name);
            manager.insertEntity(e);*/
        }
        else {  //从数据库获取数据

        }
        //定义习题
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        JSONArray data;
        if(Questions == null) return;
        setContentView(R.layout.activity_details);
        title = (TextView) findViewById(R.id.title);
        title.setText(Name);
        data = Questions.getJSONArray("data");
        for(int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            String Body = y.getString("Body");
            Body = (i + 1) + "." + Body;
            y.put("Body", Body);
            Question u = new Question(y);
            QuestionList.add(u);
        }
        //定义关系
        data = Result.getJSONArray("content");
        for(int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            Relative u = new Relative(y, Name);
            RelativeList.add(u);
        }
        //定义属性
        data = Result.getJSONArray("property");
        for(int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            Property u = new Property(y);
            PropertyList.add(u);
        }
        //设置关系
        RelativeAdapter relative_adapter = new RelativeAdapter(this, R.layout.relative, RelativeList);
        NoScrollListview relative_listView = (NoScrollListview) findViewById(R.id.relative_list_view);
        relative_listView.setAdapter(relative_adapter);
        //设置属性
        PropertyAdapter property_adapter = new PropertyAdapter(this, R.layout.property, PropertyList);
        NoScrollListview property_listView = (NoScrollListview) findViewById(R.id.property_list_view);
        property_listView.setAdapter(property_adapter);
        //设置相关习题
        QuestionAdapter question_adapter = new QuestionAdapter(this, R.layout.question, QuestionList);
        NoScrollListview question_listView = (NoScrollListview) findViewById(R.id.question_list_view);
        question_listView.setAdapter(question_adapter);

        description = (TextView) findViewById(R.id.description);
        description.setText(findDescription());
    }
}