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
import com.java.qitianliang.databinding.ActivityMainBinding;
import com.java.qitianliang.ui.InstanceListFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;

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
                        //

                        break;
                    case R.id.action_logout:
                        loginUsername = null;
                        // 保存记录?

                        // Login Activity
                        startActivityForResult(new Intent(getApplicationContext(), ActivityLogin.class), 1);
                        break;
                    case R.id.test:
                        intent = new Intent();
                        intent.setClass(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("name","杜甫");
                        intent.putExtra("course","chinese");
                        intent.putExtra("id", id);
                        intent.putExtra("is_collect","true");
                        startActivity(intent);
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 图标
        navigationView.setItemIconTintList(null);

        // 设置菜单点击跳转
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int target_fragment = 0;
                switch (item.getItemId()) {
                    case R.id.item_instanceList :
                        target_fragment = R.id.nav_instanceList;
                        break;
                    case R.id.item_instanceFind :
                        target_fragment = R.id.nav_instanceFind;
                        break;
                    case R.id.item_instanceLink:
                        target_fragment = R.id.nav_instanceLink;
                        break;
                    case R.id.item_questionAns:
                        target_fragment = R.id.nav_questionAns;
                        break;
                    case R.id.item_specificTest:
                        target_fragment = R.id.nav_specificTest;
                        break;
                    case R.id.item_knowledgeSum:
                        target_fragment = R.id.nav_knowledgeSum;
                        break;
                    case R.id.item_questionRec:
                        target_fragment = R.id.nav_questionRec;
                        break;
                    case R.id.item_browsingHis:
                        target_fragment = R.id.nav_browsingHis;
                        break;
                    case R.id.item_collectingHis:
                        target_fragment = R.id.nav_collectingHis;
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
                    case R.id.nav_specificTest:

                        break;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get Login Username for Collecting List and History Record
                    loginUsername = data.getStringExtra("data_return");
                } else
                    loginUsername = null;
                if (loginUsername != null)
                    ((TextView) findViewById(R.id.usernameView)).setText(loginUsername);
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

}