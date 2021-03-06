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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ActivityRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.show();
    }

    public void register(View view) {

        EditText username = (EditText) findViewById(R.id.regUserName);
        EditText password = (EditText) findViewById(R.id.regPassWord);
        EditText password_confirm = (EditText) findViewById(R.id.regPassWordConfirm);

        // Examine PassWord
        if (!password.getText().toString().equals(password_confirm.getText().toString())) {
            Toast.makeText(getApplicationContext(), "输入密码不一致", Toast.LENGTH_LONG).show();
            return;
        }

        String newUsername = username.getText().toString();
        String newPassword = password.getText().toString();

        new Thread() {
            @Override
            public void run() {
                int msg = 0;
                // request
                String request = PostUtil.Post("RegisterServlet", new String("request=register"));
                if (request.equals("Service Error")) {
                    msg = 3;
                    handRegister.sendEmptyMessage(msg);
                    return;
                }
                // access
                String publicKey = request;
                String userdata = "";
                try {
                    String cipherPassword = RSAKeyManager.encrypt(password.getText().toString(), publicKey);
                    userdata = "request=access&username=" + URLEncoder.encode(newUsername, "UTF-8") +
                            "&password=" + URLEncoder.encode(cipherPassword, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                request = PostUtil.Post("RegisterServlet", userdata);
                if (request.equals("Service Error")) {
                    msg = 3;
                    handRegister.sendEmptyMessage(msg);
                    return;
                }
                if (request.equals("Register Successful"))
                    msg = 1;
                else if (request.equals("Username Already Exists"))
                    msg = 2;
                handRegister.sendEmptyMessage(msg);

            }
        }.start();
    }

    final Handler handRegister = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
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