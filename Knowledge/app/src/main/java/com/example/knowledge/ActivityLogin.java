package com.example.knowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goto_register(View view) {
        startActivity(new Intent(getApplicationContext(), ActivityRegister.class));
    }

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.loginUserName);
        EditText password = (EditText) findViewById(R.id.loginPassWord);

        new Thread() {
            @Override
            public void run() {

                String userdata = "";
                try {
                    userdata = "username=" + URLEncoder.encode(username.getText().toString(), "UTF-8") +
                            "&password=" + URLEncoder.encode(password.getText().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String request = PostUtil.Post("LoginServlet", userdata);
                int msg = 0;
                if (request.equals("Login Successful"))
                    msg = 1;
                handLogin.sendEmptyMessage(msg);

            }
        }.start();
    }

    final Handler handLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("data_return", ((EditText) findViewById(R.id.loginUserName)).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
            }
        }
    };

}