package com.java.qitianliang.server;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoByInstanceName {
    private static String pattern = "^http:(.*)";
    private static String infoByInstanceNameURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName";
    public static String get(String course, String name, String id) throws Exception {
        String s = sendGet(course, name, id);
        JSONObject result = JSONObject.parseObject(s);
        if(result == null)
            return null;
        JSONObject data = result.getJSONObject("data");
        JSONArray property_ = data.getJSONArray("property");
        JSONArray content_ = data.getJSONArray("content");
        JSONObject item = new JSONObject();
        item.put("name", data.getString("label"));
        JSONArray property = new JSONArray();
        JSONArray content = new JSONArray();
        for(int i = 0; i < property_.size(); i++) {
            JSONObject x = (JSONObject)property_.get(i);
            JSONObject y = new JSONObject();
            String object = "";
            y.put("label", x.getString("predicateLabel"));
            if(x.containsKey("objectLabel"))
                object = x.getString("objectLabel");
            else
                object = x.getString("object");
            //if(object.matches(pattern))
            //    continue;
            y.put("object", object);
            if(!property.contains(y))
                property.add(y);
        }
        for(int i = 0; i < content_.size(); i++) {
            JSONObject x = (JSONObject)content_.get(i);
            JSONObject y = new JSONObject();
            y.put("label", x.getString("predicate_label"));
            if(x.containsKey("subject_label")) {
                y.put("object", x.getString("subject_label"));
                y.put("flag", "0");
            }
            else {
                y.put("object", x.getString("object_label"));
                y.put("flag", "1");
            }
            if(y.toString().contains("CKEM"))
                continue;
            if(!content.contains(y))
                content.add(y);
        }
        item.put("property", property);
        item.put("content", content);
        String answer = item.toString();
        return answer;
    }
    public static String sendGet(String course, String name, String id) throws Exception {
        String result = "";
        String data = "?" + "course="+course+"&name="+name+"&id="+id;
        HttpURLConnection conn = (HttpURLConnection) new URL(infoByInstanceNameURL + data).openConnection();
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
