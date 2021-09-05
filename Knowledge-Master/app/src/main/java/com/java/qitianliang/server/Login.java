package com.java.qitianliang.server;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class Login {
    static String loginURL = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
    public static String get(String phone, String password) {
        String s;
        try {
            s = sendPost(phone, password);
        } catch (Exception e) {
            s = "";
            return s;
        }
        JSONObject result = JSONObject.parseObject(s);
        String id = result.getString("id");
        return id;
    }
    static String sendPost(String phone, String password) throws Exception {
        String result = "";
        HttpURLConnection conn = (HttpURLConnection) new URL(loginURL).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Charset", "UTF-8");
        OutputStream outputStream = conn.getOutputStream();
        String data = "password="+password+"&phone="+phone;
        outputStream.write(data.getBytes());
        outputStream.flush();
        outputStream.close();
        conn.connect();
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer response = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null)
                response.append(line);
            result = response.toString();
        }
        return result;
    }
}