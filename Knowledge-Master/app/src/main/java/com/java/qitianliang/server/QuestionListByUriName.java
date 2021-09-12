package com.java.qitianliang.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class QuestionListByUriName {
    private static String questionListByUriNameURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName";
    private static String patternA = "(.*)[A][\56](.*)[B][\56]";
    private static String patternB = "(.*)[B][\56](.*)[C][\56]";
    private static String patternC = "(.*)[C][\56](.*)[D][\56]";
    private static String patternD = "(.*)[D][\56](.*)$";
    private static Pattern ra = Pattern.compile(patternA);
    private static Pattern rb = Pattern.compile(patternB);
    private static Pattern rc = Pattern.compile(patternC);
    private static Pattern rd = Pattern.compile(patternD);

    public static String get(String uriName, String id) throws Exception {
        String s = sendGet(uriName, id);;
        JSONObject result = JSONObject.parseObject(s);
        JSONArray data = result.getJSONArray("data");
        JSONArray answerList = new JSONArray();
        for(int i = 0; i < data.size(); i++) {
            JSONObject x = (JSONObject)data.get(i);
            JSONObject item = new JSONObject();
            String qBody = x.getString("qBody");
            System.out.println(qBody);
            String qAnswer = x.getString("qAnswer");
            if(qAnswer != null && qAnswer.length() > 2) {
                if(qAnswer.charAt(0) == '答' && qAnswer.charAt(1) == '案')
                    qAnswer = qAnswer.substring(2);
            }
            Matcher ma = ra.matcher(qBody);
            Matcher mb = rb.matcher(qBody);
            Matcher mc = rc.matcher(qBody);
            Matcher md = rd.matcher(qBody);
            String A = "";
            String B = "";
            String C = "";
            String D = "";
            String Body = "";
            while (ma.find()) {
                Body = ma.group(1);
                A = ma.group(2);
            }
            while (mb.find())
                B = mb.group(2);
            while (mc.find())
                C = mc.group(2);
            while (md.find())
                D = md.group(2);
            if(A == "" || B == "" || C == "" || D == "" || Body == "")
                continue;
            item.put("qBody", qBody);
            item.put("Body", Body);
            item.put("A", A);
            item.put("B", B);
            item.put("C", C);
            item.put("D", D);
            item.put("qAnswer", qAnswer);
            if (!qAnswer.equals("A") && !qAnswer.equals("B") && !qAnswer.equals("C") && !qAnswer.equals("D"))
                continue;
            answerList.add(item);
        }
        JSONObject item = new JSONObject();
        item.put("data", answerList);
        String answer = item.toString();
        return answer;
    }
    private static String sendGet(String uriName, String id) throws Exception {
        String result = "";
        String data = "?" + "uriName="+uriName+"&id="+id;
        HttpURLConnection conn = (HttpURLConnection) new URL(questionListByUriNameURL + data).openConnection();
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