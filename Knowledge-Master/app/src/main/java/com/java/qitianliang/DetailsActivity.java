package com.java.qitianliang;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    private TextView item;
    private ImageView image;
    private List<Question> QuestionList = new ArrayList<Question>();
    private List<Property> PropertyList = new ArrayList<Property>();
    private List<Relative> RelativeList = new ArrayList<Relative>();
    private String Name;
    private String Course;
    private String username = MainActivity.loginUsername;
    private boolean is_find = false;
    private JSONObject Questions = null;
    private JSONObject Result = null;
    private Bitmap imageMap = null;
    private static String http = "^http:(.*)";
    private String is_collect = "false"; //是否收藏
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { //联网成功
                Bundle bundle = msg.getData();
                String result = bundle.getString("result");
                String question = bundle.getString("question");
                Result = JSONObject.parseObject(result);
                Questions = JSONObject.parseObject(question);
                is_collect = "false";
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
        if (username != null && findInDB("c"))
            is_collect = "true";
        else
            is_collect = "false";

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.show();
        //查找历史记录
        //if (username != null)
        is_find = findInDB("h");
        if (is_find == true) {
            initdata();
            return;
        }
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
                    if (ID == null || ID.equals(""))
                        msg.what = 0;
                    else {
                        String result = "";
                        try {
                            result = InfoByInstanceName.get(Course, Name, ID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(result != null) {
                            JSONObject Re = JSONObject.parseObject(result);
                            JSONArray u = Re.getJSONArray("property");
                            for (int i = 0; i < u.size(); i++) {
                                JSONObject x = u.getJSONObject(i);
                                if (x.getString("label") != null && x.getString("label").equals("图片")) {
                                    try {
                                        imageMap = GetImage.get(x.getString("object"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
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
        getMenuInflater().inflate(R.menu.menu_details, menu);
        if (is_collect.equals("true")) {
            menu.findItem(R.id.collect).setIcon(android.R.drawable.btn_star_big_on);
        } else
            menu.findItem(R.id.collect).setIcon(android.R.drawable.btn_star_big_off);
        return true;
    }

    //处理方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.collect: //收藏
                if (is_collect.equals("false")) {
                    Toast.makeText(this, "已收藏！", Toast.LENGTH_LONG).show();
                    is_collect = "true";
                    item.setIcon(android.R.drawable.btn_star_big_on);
                    //添加收藏，只收藏名字
                    if(username != null) {
                        TitleDBManager manager = TitleDBManager.getInstance(this, username);
                        Title e = new Title(Name, Course);
                        manager.insertTitle(e);
                    }
                    //PrintAll();
                } else {
                    Toast.makeText(this, "取消收藏！", Toast.LENGTH_LONG).show();
                    is_collect = "false";
                    item.setIcon(android.R.drawable.btn_star_big_off);
                    //添加收藏，只收藏名字
                    if(username != null) {
                        TitleDBManager manager = TitleDBManager.getInstance(this, username);
                        manager.deleteTitleByUri(Name, Course);
                    }
                    //PrintAll();
                }
                break;
            case R.id.share: //分享到新浪微博
                Toast.makeText(this, "分享到新浪微博！", Toast.LENGTH_LONG).show();
                //ShareUtil.shareText(this,"发送详情页！","test");
                break;
            case android.R.id.home:
                imageMap = null;
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private boolean findInDB(String type) {
        if(type.equals("c")) { //收藏
            TitleDBManager manager =  TitleDBManager.getInstance(this, username);
            //manager.deleteAllTitle();
            Title title = manager.getTitleByUri(Name, Course);
            if (title != null)
                return true;
        }
        else { //历史
            EntityDBManager manager = EntityDBManager.getInstance(this, username);
            //manager.deleteAllEntity();
            Entity entity = manager.getEntityByUri(Name, Course);
            if (entity != null)
                return true;
        }
        return false;
    }

    private String findDescription() {
        String D = "无相关描述！";
        JSONArray data = Result.getJSONArray("property");
        for (int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            if (y.getString("label").equals("定义"))
                D = y.getString("object");
        }
        if (D == null) D = "无相关描述！";
        return "      " + D;
    }

    private void initdata() {
        if (Questions == null && is_find == false) return;
        setContentView(R.layout.activity_details);
        image = (ImageView) findViewById(R.id.entity_image);
        title = (TextView) findViewById(R.id.title);
        item = (TextView) findViewById(R.id.item);
        description = (TextView) findViewById(R.id.description);
        title.setText(Name);
        if (is_find == true) {  //如果历史记录中有当前项
            EntityDBManager manager = EntityDBManager.getInstance(this, username);
            Entity entity = manager.getEntityByUri(Name, Course);
            description.setText(entity.getDescription());
            JSONArray data;
            data = JSONArray.parseArray(entity.getRelative());
            initRelative(data);
            data = JSONArray.parseArray(entity.getProperty());
            initProperty(data);
            data = JSONArray.parseArray(entity.getQuestion());
            initQuestion(data);
            manager.deleteEntityByUri(Name, Course);
            manager.insertEntity(entity);
            String I = entity.getImage();
            Bitmap Map = StringToBitmap(I);
            if(!I.equals("")) {
                image.setImageBitmap(Map);
                image.setVisibility(View.VISIBLE);
                title.setGravity(Gravity.LEFT);
                item.setGravity(Gravity.LEFT);
            }
        } else {  //从网络获取数据
            if(imageMap != null) {
                image.setImageBitmap(imageMap);
                image.setVisibility(View.VISIBLE);
                title.setGravity(Gravity.LEFT);
                item.setGravity(Gravity.LEFT);
            }
            if(username != null) { //更新数据库
                EntityDBManager manager = EntityDBManager.getInstance(this, username);
                Entity entity = manager.getEntityByUri(Name, Course);
                if (entity != null)
                    manager.deleteEntityByUri(Name, Course);
                String name = Name;
                String subject = Course;
                String D = findDescription();
                String property = Result.getJSONArray("property").toString();
                String relative = Result.getJSONArray("content").toString();
                String question = Questions.getJSONArray("data").toString();
                String I = BitmapToString(imageMap);
                if(I == null)
                    I = "";
                Entity e = new Entity(name, subject, D, property, relative, question, I);
                manager.insertEntity(e);
            }
            initRelative(Result.getJSONArray("content"));
            initProperty(Result.getJSONArray("property"));
            initQuestion(Questions.getJSONArray("data"));
            description.setText(findDescription());
        }
        PrintAll();
    }

    void initRelative(JSONArray data) {
        for (int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            if(y.getString("object") != null && y.getString("object").matches(http))
                continue;
            Relative u = new Relative(y, Name);
            RelativeList.add(u);
        }
        RelativeAdapter relative_adapter = new RelativeAdapter(this, R.layout.relative, RelativeList);
        NoScrollListview relative_listView = (NoScrollListview) findViewById(R.id.relative_list_view);
        relative_listView.setAdapter(relative_adapter);
    }

    void initProperty(JSONArray data) {
        for (int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            if(y.getString("object") != null && y.getString("object").matches(http))
                continue;
            Property u = new Property(y);
            PropertyList.add(u);
        }
        PropertyAdapter property_adapter = new PropertyAdapter(this, R.layout.property, PropertyList);
        NoScrollListview property_listView = (NoScrollListview) findViewById(R.id.property_list_view);
        property_listView.setAdapter(property_adapter);
    }

    void initQuestion(JSONArray data) {
        for (int i = 0; i < data.size(); i++) {
            JSONObject y = data.getJSONObject(i);
            String Body = y.getString("Body");
            Body = (i + 1) + "." + Body;
            y.put("Body", Body);
            Question u = new Question(y);
            QuestionList.add(u);
        }
        QuestionAdapter question_adapter = new QuestionAdapter(this, R.layout.question, QuestionList);
        NoScrollListview question_listView = (NoScrollListview) findViewById(R.id.question_list_view);
        question_listView.setAdapter(question_adapter);
    }

    public Bitmap StringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String BitmapToString(Bitmap bitmap) {
        String string = null;
        if (bitmap == null) return string;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[]bytes = bStream.toByteArray();
        string=Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    void PrintAll() {
        EntityDBManager manager = EntityDBManager.getInstance(this, username);
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
        TitleDBManager u = TitleDBManager.getInstance(this, username);
        List<Title> v = u.getAllTitle();
        for (int i = 0; i < v.size(); i++) {
            Title y = v.get(i);
            System.out.println(i + 1 + ": ");
            System.out.println(y.getTitle());
        }
        System.out.println("收藏记录输出结束!\n");
    }
}