package com.java.qitianliang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.java.qitianliang.SQLite.Entity;
import com.java.qitianliang.SQLite.EntityDBManager;
import com.java.qitianliang.SQLite.Title;
import com.java.qitianliang.SQLite.TitleDBManager;
import com.java.qitianliang.databinding.ActivityMainBinding;
import com.java.qitianliang.ui.InstanceListFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;

    // 用于同步浏览记录
    public static String loginUsername = null;

    // 当前选定学科
    public static String currentSubject = "chinese";
    public ArrayList<String> Subject = new ArrayList<>(Arrays.asList("语文", "数学", "英语",
            "政治", "历史", "地理", "物理", "化学", "生物"));
    public ArrayList<String> delSubject = new ArrayList<>();
    public static final String[] subjects
            = { "语文", "数学", "英语", "政治", "历史", "地理", "物理", "化学", "生物" };

    // 实体列表
    public static HashMap<String, ArrayList<String>> instanceListOfAll = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_subjectChange:
                        // 修改学科
                        Intent intent = new Intent(getApplicationContext(), ActivitySubjectManager.class);
                        intent.putExtra("sub", Subject);
                        intent.putExtra("delSub", delSubject);
                        startActivityForResult(intent, 2);
                        currentSubject = transChi2Eng(Subject.get(0));
                        break;
                    case R.id.action_passwordChange:
                        // 信息修改
                        // 必须登录后才能修改已登录账号的信息
                        if(loginUsername != null) {
                            startActivityForResult(new Intent(getApplicationContext(), ActivityInfo.class), 3);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "登录后才能修改用户信息!", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.action_logout:
                        // 保存记录
                        upgradeHistory();
                        loginUsername = null;
                        // Login Activity
                        startActivityForResult(new Intent(getApplicationContext(), ActivityLogin.class), 1);
                        break;
//                    case R.id.clear_text:
//                        //清除浏览记录
//                        if(loginUsername != null) {
//                            EntityDBManager manager = EntityDBManager.getInstance(MainActivity.this, loginUsername);
//                            manager.deleteAllEntity();
//                        }
//                        Toast.makeText(MainActivity.this, "浏览记录已清除!", Toast.LENGTH_LONG).show();
//                        break;
//                    case R.id.test:
//                        intent = new Intent();
//                        intent.setClass(com.java.qitianliang.MainActivity.this, ActivityTest.class);
//                        startActivity(intent);
                    default:
                        break;
                }
                return false;
            }
        });

        // 从文件中读入全部实体序列
        for (int i = 0; i < 9; i++) {
            int raw_file = transChi2FileId(subjects[i]);
            String instance_Key = transChi2Eng(subjects[i]);
            ArrayList<String> instanceListOfSub = new ArrayList<String>();

            //读文件写入数组
            InputStream inputStream = null;
            Reader reader = null;
            BufferedReader bufferedReader = null;
            try {
                //得到资源中的Raw数据流
                inputStream = getResources().openRawResource(raw_file);
                reader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(reader);
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    instanceListOfSub.add(temp);
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
            instanceListOfAll.put(instance_Key, instanceListOfSub);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_instanceList,
                R.id.nav_instanceFind,
                R.id.nav_instanceLink,
                R.id.nav_questionAns,
                R.id.nav_specificTest,
                R.id.nav_knowledgeSum,
                R.id.nav_questionRec,
                R.id.nav_browsingHis,
                R.id.nav_collectingHis)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 图标
        navigationView.setItemIconTintList(null);

        // 学科栏初始化
        TabLayout tabs = binding.appBarMain.tabs;
        for (int i = 0; i < 9; i++)
            tabs.addTab(tabs.newTab().setText(subjects[i]));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String subject_chi = tab.getText().toString();
                currentSubject = transChi2Eng(subject_chi);
                // 获取当前活动的fragment
                // 更新对应fragment的内容
                // 实体列表、专项测试
                int id = navController.getCurrentDestination().getId();
                switch (id) {
                    case R.id.nav_instanceList:
                        navController.popBackStack();
                        navController.navigate(id);
                        break;
                    // other cases

                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 设置菜单点击跳转
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int target_fragment = 0;
                switch (item.getItemId()) {
                    case R.id.item_instanceList :
                        target_fragment = R.id.nav_instanceList;
                        tabs.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_instanceFind :
                        target_fragment = R.id.nav_instanceFind;
                        tabs.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_instanceLink:
                        target_fragment = R.id.nav_instanceLink;
                        tabs.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_questionAns:
                        target_fragment = R.id.nav_questionAns;
                        tabs.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_specificTest:
                        target_fragment = R.id.nav_specificTest;
                        tabs.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_knowledgeSum:
                        target_fragment = R.id.nav_knowledgeSum;
                        tabs.setVisibility(View.GONE);
                        break;
                    case R.id.item_questionRec:
                        target_fragment = R.id.nav_questionRec;
                        tabs.setVisibility(View.GONE);
                        break;
                    case R.id.item_browsingHis:
                        target_fragment = R.id.nav_browsingHis;
                        tabs.setVisibility(View.GONE);
                        break;
                    case R.id.item_collectingHis:
                        target_fragment = R.id.nav_collectingHis;
                        tabs.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                if (!navController.popBackStack(target_fragment, false)) {
                    navController.navigate(target_fragment);
                }
                drawer.closeDrawer(navigationView);
                return true;
            }
        });

        // Login Activity
        startActivityForResult(new Intent(getApplicationContext(), ActivityLogin.class), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 退出前保存到后端
        upgradeHistory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK)
                    // Get Login Username for Collecting List and History Record
                    loginUsername = data.getStringExtra("data_return");
                else
                    loginUsername = null;
                if (loginUsername != null) {
                    ((TextView) findViewById(R.id.usernameView)).setText(loginUsername);
                    // 根据用户加载收藏记录
                    loadHistory();
                }
                else
                    ((TextView) findViewById(R.id.usernameView)).setText("未登录");
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // set subject list
                    ArrayList<String> category = (ArrayList<String>) (data).getSerializableExtra("sub");
                    ArrayList<String> delCategory = (ArrayList<String>) (data).getSerializableExtra("delSub");
                    Subject = category;
                    delSubject = delCategory;
                    TabLayout tabs = binding.appBarMain.tabs;
                    tabs.removeAllTabs();
                    for (int i = 0; i < category.size(); i++)
                        tabs.addTab(tabs.newTab().setText(category.get(i)));
                }
                break;
            case 3:
                // 信息修改
                if (resultCode == RESULT_OK) {
                    String oldUsername = loginUsername;
                    loginUsername = data.getStringExtra("data_return");
                    ((TextView) findViewById(R.id.usernameView)).setText(loginUsername);
                    // reload user data
                    upgradeHistory();
                    changeHistory(oldUsername);
                    loadHistory();
                }
                break;
            default:
        }
    }

    public static String transChi2Eng(String sub_chi) {
        switch (sub_chi) {
            case "语文":
                return new String("chinese");
            case "数学":
                return new String("math");
            case "英语":
                return new String("english");
            case "政治":
                return new String("politics");
            case "历史":
                return new String("history");
            case "地理":
                return new String("geo");
            case "物理":
                return new String("physics");
            case "化学":
                return new String("chemistry");
            case "生物":
                return new String("biology");
            default:
                return null;
        }
    }

    public static int transChi2FileId(String sub_chi) {
        switch (sub_chi) {
            case "语文":
                return R.raw.chinese;
            case "数学":
                return R.raw.math;
            case "英语":
                return R.raw.english;
            case "政治":
                return R.raw.politics;
            case "历史":
                return R.raw.history;
            case "地理":
                return R.raw.geo;
            case "物理":
                return R.raw.physics;
            case "化学":
                return R.raw.chemistry;
            case "生物":
                return R.raw.biology;
            default:
                return 0;
        }
    }

    // 加载历史
    public void loadHistory() {
        String username = MainActivity.loginUsername;
        if (username == null || username.equals("")) return;

        // 加载新登陆的用户前应该把本地记录删除
        EntityDBManager e = EntityDBManager.getInstance(MainActivity.this, MainActivity.loginUsername);
        e.deleteAllEntity();
        TitleDBManager t = TitleDBManager.getInstance(MainActivity.this, MainActivity.loginUsername);
        t.deleteAllTitle();

        // 以下待定
        new Thread() {
            @Override
            public void run() {
                String load = "";
                // request
                try {
                    load = "request=load&username=" + URLEncoder.encode(username, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String request = PostUtil.Post("HistoryServlet", load);

                // load
                String[] allData = request.split("_");
                int num_title = Integer.parseInt(allData[0]);
                int num_entity = Integer.parseInt(allData[1]);

                for (int i = 0; i < num_title; i++) {
                    int title_index = i * 2 + 2;
                    t.insertTitle(new Title(allData[title_index], allData[title_index+1]));
                }
                int offset = 2 * num_title + 2;
                for (int i = 0; i < num_entity; i++) {
                    int entity_index = i * 7 + offset;
                    e.insertEntity(new Entity(allData[entity_index], allData[entity_index+1],
                            allData[entity_index+2], allData[entity_index+3],
                            allData[entity_index+4], allData[entity_index+5], allData[entity_index + 6]));
                }
            }
        }.start();
    }

    //上传历史
    public void upgradeHistory() {
        String username = MainActivity.loginUsername;
        if (username == null || username.equals("")) return;

        // 以下待定
        new Thread() {
            @Override
            public void run() {
                String upgrade = "";
                EntityDBManager manager = EntityDBManager.getInstance(MainActivity.this, MainActivity.loginUsername);
                List<Entity> e = manager.getAllEntity();
                TitleDBManager u = TitleDBManager.getInstance(MainActivity.this, MainActivity.loginUsername);
                List<Title> v = u.getAllTitle();

                // upgrade
                int num_title = v.size();
                int num_entity = e.size();

                // request
                try {
                    upgrade = "request=upgrade&username=" + URLEncoder.encode(username, "UTF-8") +
                            "&titleNum=" + URLEncoder.encode(Integer.toString(num_title), "UTF-8") +
                            "&entityNum=" + URLEncoder.encode(Integer.toString(num_entity), "UTF-8");
                    // 添加title和entity
                    for (int i = 0; i < num_title; i++) {
                        String msg_title = "&title" + Integer.toString(i) + "title=" + URLEncoder.encode(v.get(i).getTitle(), "UTF-8") +
                                "&title" + Integer.toString(i) + "subject=" + URLEncoder.encode(v.get(i).getSubject(), "UTF-8");
                        upgrade += msg_title;
                    }
                    for (int j = 0; j < num_entity; j++) {
                        String msg_entity = "&entity" + Integer.toString(j) + "name=" + URLEncoder.encode(e.get(j).getName(), "UTF-8") +
                                "&entity" + Integer.toString(j) + "subject=" + URLEncoder.encode(e.get(j).getSubject(), "UTF-8") +
                                "&entity" + Integer.toString(j) + "description=" + URLEncoder.encode(e.get(j).getDescription(), "UTF-8") +
                                "&entity" + Integer.toString(j) + "property=" + URLEncoder.encode(e.get(j).getProperty(), "UTF-8") +
                                "&entity" + Integer.toString(j) + "relative=" + URLEncoder.encode(e.get(j).getRelative(), "UTF-8") +
                                "&entity" + Integer.toString(j) + "question=" + URLEncoder.encode(e.get(j).getQuestion(), "UTF-8") +
                                "&entity" + Integer.toString(j) + "image=" + URLEncoder.encode(e.get(j).getImage(), "UTF-8");
                        upgrade += msg_entity;
                    }
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }

                PostUtil.Post("HistoryServlet", upgrade);
            }
        }.start();
    }

    //修改用户信息
    public void changeHistory(String oldUsername) {
        //
        new Thread() {
            @Override
            public void run() {
                String change = "";
                // request
                try {
                    change = "request=change&oldusername=" + URLEncoder.encode(oldUsername, "UTF-8") +
                            "&newusername=" + URLEncoder.encode(loginUsername, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
                String request = PostUtil.Post("HistoryServlet", change);
            }
        }.start();
    }


}