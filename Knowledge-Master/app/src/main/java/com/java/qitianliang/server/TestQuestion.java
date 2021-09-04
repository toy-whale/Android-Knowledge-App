package com.java.qitianliang.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestQuestion {
    public static String get(String[] points, int number, String id) throws Exception {
        int len = points.length;
        int cnt = 0;
        JSONObject item = new JSONObject();
        JSONArray set = new JSONArray();
        JSONArray questions = new JSONArray();
        JSONArray others = new JSONArray();
        String data[] = new String[len];
        for(String x : points) { //获取所有相关试题
            data[cnt] = QuestionListByUriName.get(x, id);
            JSONObject y = JSONObject.parseObject(data[cnt]);
            JSONArray list = y.getJSONArray("data");
            //list = select(list, x);
            cnt++;
            for(int i = 0; i < list.size(); i++) {
                JSONObject e = list.getJSONObject(i);
                if(set.contains(e))
                    continue;
                else
                    set.add(e);
            }
        }
        if(number <= 0) { //数量输入有误
            item.put("msg", "1");
            String answer = item.toString();
            return answer;
        }
        if(number > set.size()) { //数量超过题库数量
            item.put("msg", "2");
            String answer = item.toString();
            return answer;
        }
        int average = number / len; //每个知识点平均抽取试题
        for(int j = 0; j < len;j++) {
            if(average == 0) break;
            JSONObject y = JSONObject.parseObject(data[j]);
            JSONArray list = y.getJSONArray("data");
            //list = select(list, points[j]);
            cnt++;
            if(list.size() <= average) {
                for(int i = 0; i < list.size();i++) {
                    JSONObject e = list.getJSONObject(i);
                    if(questions.contains(e))
                        continue;
                    else
                        questions.add(e);
                }
            }
            else {
                int arr[] = getRandom(list.size(), average);
                for(int i = 0; i < average; i++) {
                    JSONObject e = list.getJSONObject(arr[i]);
                    if(questions.contains(e))
                        continue;
                    else
                        questions.add(e);
                }
            }
        }
        for(int i = 0;i < set.size(); i++) { //剩余试题
            JSONObject e = set.getJSONObject(i);
            if(others.contains(e) || questions.contains(e))
                continue;
            else
                others.add(e);
        }
        cnt = number - questions.size();
        int arr[] = getRandom(others.size(), cnt);
        for(int i = 0; i < cnt; i++) {
            JSONObject e = others.getJSONObject(arr[i]);
            questions.add(e);
        }
        questions = shuffle(questions);
        item.put("msg","0");
        item.put("data", questions);
        String answer = item.toString();
        return answer;
    }
    public static int[] getRandom(int length, int size) {
        int index[] = new int[size];
        int flag[] = new int[length];
        int cnt = 0;
        Random r = new Random();
        while(cnt < size) {
            int e = r.nextInt(length);
            if(flag[e] == 0) {
                index[cnt] = e;
                cnt++;
                flag[e] = 1;
            }
        }
        return index;
    }
    public static JSONArray shuffle(JSONArray x) { //随机打乱
        List<JSONObject> Jsons = new ArrayList<JSONObject>();
        JSONArray y = new JSONArray();
        for (int i = 0; i < x.size(); i++)
            Jsons.add(x.getJSONObject(i));
        Collections.shuffle(Jsons);
        for (int i = 0; i < x.size(); i++)
            y.add((JSONObject)Jsons.get(i));
        return y;
    }
    public static JSONArray select(JSONArray s, String k) {
        JSONArray y = new JSONArray();
        for (int i = 0; i < s.size(); i++) {
            JSONObject e = s.getJSONObject(i);
            if(!e.getString("Body").contains(k))
                continue;
            y.add(e);
        }
        return y;
    }
}