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

public class Relatedsubject {
    private static String pattern = "^http:(.*)";
    private static String relatedsubjectURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject";
    public static String get(String course, String subjectName, String id) throws Exception {
        String s = sendPost(course, subjectName, id);
        s = s.replaceAll("<br>", "");
        //s = s.replaceAll("\n","");
        JSONObject result = JSONObject.parseObject(s);
        JSONArray data = result.getJSONArray("data");
        if(data == null) data = new JSONArray();
        JSONObject sList = new JSONObject();
        JSONObject vList = new JSONObject();
        for(int i = 0; i < data.size(); i++) {
            JSONObject x = data.getJSONObject(i);
            if(x.getString("subject") != null && x.getString("subject").equals(subjectName)) {
                String p = x.getString("predicate");
                if(p != null && sList.get(p) == null) {
                    JSONArray u = new JSONArray();
                    if(x.getString("value").matches(pattern))
                        continue;
                    u.add(x.getString("value"));
                    sList.put(p, u);
                }
                else if(p != null && sList.get(p) != null) {
                    JSONArray u = sList.getJSONArray(p);
                    if(x.getString("value").matches(pattern))
                        continue;
                    u.add(x.getString("value"));
                    sList.put(p, u);
                }
            }
            else if(x.getString("subject") != null && x.getString("value").equals(subjectName)) {
                String p = x.getString("predicate");
                if(p != null && vList.get(p) == null) {
                    JSONArray u = new JSONArray();
                    if(x.getString("value").matches(pattern))
                        continue;
                    u.add(x.getString("subject"));
                    vList.put(p, u);
                }
                else if(p != null && vList.get(p) != null) {
                    JSONArray u = vList.getJSONArray(p);
                    if(x.getString("value").matches(pattern))
                        continue;
                    u.add(x.getString("subject"));
                    vList.put(p, u);
                }
            }
            else
                continue;
        }
        JSONObject item = new JSONObject();
        item.put("subject", sList);
        item.put("value", vList);
        String answer = item.toString();
        return answer;
    }
    private static String sendPost(String course, String subjectName, String id) throws Exception {
        String result = "";
        HttpURLConnection conn = (HttpURLConnection) new URL(relatedsubjectURL).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Charset", "UTF-8");
        OutputStream outputStream = conn.getOutputStream();
        String data = "course="+course+"&subjectName="+subjectName+"&id="+id;
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