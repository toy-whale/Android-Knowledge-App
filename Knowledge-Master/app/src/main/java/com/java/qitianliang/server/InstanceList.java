package com.java.qitianliang.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class InstanceList {
    private static String instanceListURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList";
    public static String get(String course, String searchKey, String id, String sort_type, String word) throws Exception {
        String s = sendGet(course, searchKey, id);
        JSONObject result = JSONObject.parseObject(s);
        JSONArray data = result.getJSONArray("data");
        JSONObject item = new JSONObject();
        JSONArray dataList = new JSONArray();
        List<String> labels = new ArrayList<String>();
        for(int i = 0; i < data.size(); i++) {
            JSONObject x = (JSONObject)data.get(i);
            JSONObject y = new JSONObject();
            String label = x.getString("label");
            if(labels.contains(label))
                continue;
            else
                labels.add(label);
            y.put("label", x.getString("label"));
            y.put("category", x.getString("category"));
            dataList.add(y);
        }
        dataList = Sort.select(dataList, word);
        dataList = Sort.sort(dataList, sort_type);
        item.put("data", dataList);
        String answer = item.toString();
        return answer;
    }
    private static String sendGet(String course, String searchKey, String id) throws Exception {
        String result = "";
        String data = "?" + "course="+course+"&searchKey="+searchKey+"&id="+id;
        HttpURLConnection conn = (HttpURLConnection) new URL(instanceListURL + data).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Charset", "UTF-8");
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