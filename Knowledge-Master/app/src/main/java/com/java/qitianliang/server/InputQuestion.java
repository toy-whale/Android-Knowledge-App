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

public class InputQuestion {
    private static String inputQuestionURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
    public static String get(String course, String inputQuestion, String id) throws Exception {
        String s = sendPost(course, inputQuestion, id);
        JSONObject result = JSONObject.parseObject(s);
        JSONArray data = result.getJSONArray("data");
        JSONObject item = (JSONObject)data.get(0);
        String answer = item.getString("value");
        return answer;
    }
    private static String sendPost(String course, String inputQuestion, String id) throws Exception {
        String result = "";
        HttpURLConnection conn = (HttpURLConnection) new URL(inputQuestionURL).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Charset", "UTF-8");
        OutputStream outputStream = conn.getOutputStream();
        String data = "course="+course+"&inputQuestion="+inputQuestion+"&id="+id;
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
