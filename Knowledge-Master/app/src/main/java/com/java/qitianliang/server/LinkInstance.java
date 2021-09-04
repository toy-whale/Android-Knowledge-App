package com.java.qitianliang.server;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LinkInstance {
    private static String linkInstanceURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
    public static String get(String course, String context, String id) throws Exception {
        String s = sendPost(context, course, id);
        JSONObject result = JSONObject.parseObject(s);
        JSONObject data = result.getJSONObject("data");
        JSONArray results = data.getJSONArray("results");
        JSONArray List = new JSONArray();
        for(int i = 0; i < results.size(); i++) {
            JSONObject x = (JSONObject)results.get(i);
            JSONObject y = new JSONObject();
            y.put("type", x.getString("entity_type"));
            y.put("entity", x.getString("entity"));
            List.add(y);
        }
        JSONObject item = new JSONObject();
        item.put("data", List);
        String answer = item.toString();
        return answer;
    }
    private static String sendPost(String context, String course, String id) throws Exception {
        String result = "";
        HttpURLConnection conn = (HttpURLConnection) new URL(linkInstanceURL).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Charset", "UTF-8");
        OutputStream outputStream = conn.getOutputStream();
        String data = "context="+context+"&course="+course+"&id="+id;
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