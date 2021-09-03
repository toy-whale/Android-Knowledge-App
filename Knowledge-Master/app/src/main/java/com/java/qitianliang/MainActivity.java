package com.java.qitianliang;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.java.qitianliang.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    // 用于同步浏览记录
    private String loginUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 呼出菜单栏
                drawer.openDrawer(navigationView);
            }
        });

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

        // Get Login Username for Collecting List and History Record
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    loginUsername = data.getStringExtra("data_return");
                }
                break;
            default:
        }
        if (loginUsername != null)
        ((TextView) findViewById(R.id.usernameView)).setText(loginUsername);
    }
}