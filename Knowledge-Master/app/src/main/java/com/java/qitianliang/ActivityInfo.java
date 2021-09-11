package com.java.qitianliang;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.java.qitianliang.SQLite.Title;
import com.java.qitianliang.SQLite.TitleDBManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ActivityInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.show();
    }

    public void cancel(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        this.finish();
    }

    public void updateInfo(View view) {

        EditText username = (EditText) findViewById(R.id.infoUserName);
        EditText password = (EditText) findViewById(R.id.infoPassWord);
        EditText password_confirm = (EditText) findViewById(R.id.infoPassWordConfirm);

        // Examine PassWord
        if (!password.getText().toString().equals(password_confirm.getText().toString())) {
            Toast.makeText(getApplicationContext(), "输入密码不一致", Toast.LENGTH_LONG).show();
            return;
        }

        String oldUsername = MainActivity.loginUsername;
        String newUsername = username.getText().toString();
        String newPassword = password.getText().toString();

        // 以下待定
        new Thread() {
            @Override
            public void run() {
                int msg = 0;
                // request
                String request = PostUtil.Post("InfoServlet", new String("request=update"));
                if (request.equals("Service Error")) {
                    msg = 3;
                    handInfo.sendEmptyMessage(msg);
                    return;
                }
                // access
                String publicKey = request;
                String userdata = "";
                try {
                    String cipherPassword = RSAKeyManager.encrypt(password.getText().toString(), publicKey);
                    userdata = "request=access&oldusername=" + URLEncoder.encode(oldUsername, "UTF-8") +
                            "&newusername=" + URLEncoder.encode(newUsername, "UTF-8") +
                            "&newpassword=" + URLEncoder.encode(cipherPassword, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                request = PostUtil.Post("InfoServlet", userdata);
                if (request.equals("Service Error")) {
                    msg = 3;
                    handInfo.sendEmptyMessage(msg);
                    return;
                }
                if (request.equals("Info Change Successful"))
                    msg = 1;
                else if (request.equals("Username Already Exists"))
                    msg = 2;
                handInfo.sendEmptyMessage(msg);

            }
        }.start();
    }

    final Handler handInfo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getApplicationContext(), "信息修改失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("data_return", ((EditText) findViewById(R.id.infoUserName)).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            } else if (msg.what == 3) {
                Toast.makeText(getApplicationContext(), "服务器连接异常!", Toast.LENGTH_LONG).show();
            }
        }
    };
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