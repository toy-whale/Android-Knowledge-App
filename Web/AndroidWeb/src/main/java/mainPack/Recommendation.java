package mainPack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Recommendation {
	static final int MAX = 70643;
	static final String fileName = "./lib/all";
	static String[] course = {"biology", "chemistry", "chinese", "geo", "english", "math", "history", "physics", "politics"};
	public static String get(String[] S, String[] Q, String id) throws Exception {
		int lens = 0;
		int lenq = 0;
		int tot = 0;
		if(S != null) lens = S.length;
		if(Q != null) lenq = Q.length;
		JSONArray qset = new JSONArray();
		List<String> set = new ArrayList<String>();
		for(int i = 0; i < Math.max(lens, lenq); i++) { //5个最近浏览实体
			if(i < lens && set.size() < 10) {
				if(!set.contains(S[i]))
					set.add(S[i]);
			}
			if(i < lenq && set.size() < 10) {
				List<String> x = getPoints(Q[i], id);
				for(int j = 0; j < x.size(); j++) {
					String u = x.get(j);
					if(!set.contains(u))
						set.add(u);
				}
			}
		}
		for(int i = 0; i < set.size(); i++) {
			String u = set.get(i);
			JSONArray x = getQuestions(u, 5, id);
			for(int j = 0; j < x.size(); j++) {
				JSONObject y = x.getJSONObject(j);
				qset.add(y);
				tot++;
				if(tot == 15) break;
			}
			if(tot == 15) break;
		}
		Random r = new Random();
		while(tot < 20) { //随机抽题
			int e = r.nextInt(MAX);
			String name = readLine(e);
			if(name == null) System.out.println("null!");
			JSONArray x = getQuestions(name, 4, id);
			for(int j = 0; j < x.size(); j++) {
				JSONObject y = x.getJSONObject(j);
				if(!qset.contains(y)) {
					qset.add(y);
					tot++;
				}
			}
		}
		JSONObject item = new JSONObject();
		qset = shuffle(qset);
		item.put("data", qset);
		String answer = item.toString();
		return answer;
	}
	public static List<String> getPoints(String s, String id) throws Exception {
		List<String> answer = new ArrayList<String>();
		String[] result = new String[9];
		JSONObject[] item = new JSONObject[9];
		JSONArray[] data = new JSONArray[9];
		int mx = 0, p = -1;
		for(int i = 0; i < 9; i++) {
			result[i] = LinkInstance.get(course[i], s, id);
			item[i] = JSONObject.parseObject(result[i]);
			data[i] = item[i].getJSONArray("data");
		}
		for(int i = 0; i < 9; i++)
			if(data[i] != null && data[i].size() > mx) {
				p = i;
				mx = data[i].size();
			}
		if(p == -1)
			return answer;
		for(int j = 0; j < mx; j++) {
			JSONObject x = data[p].getJSONObject(j);
			String e = x.getString("entity");
			if(!answer.contains(e))
				answer.add(e);
		}
		return answer;
	}
	public static JSONArray getQuestions(String name, int m, String id) throws Exception {
		JSONArray x = new JSONArray();
		if(name == null) {
			System.out.println("null!");
			return x;
		}
		String result = QuestionListByUriName.get(name, id);
		JSONObject u = JSONObject.parseObject(result);
		if(!u.containsKey("data"))
			return x;
		JSONArray data = u.getJSONArray("data");
		if(m > data.size()) m = data.size();
		if(data.size() == 0)
			return x;
		int index[] = getRandom(data.size(), m);
		for(int i = 0; i < m; i++) {
			JSONObject y = data.getJSONObject(index[i]);
			x.add(y);
		}
		return x;
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
	static String readLine(int number) throws IOException {
		@SuppressWarnings("resource")
		int u = (number - 1) / 5000 + 1;
		String path = fileName  + u + ".txt";
		number = number - u * 5000 + 5000;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
		String line = reader.readLine();
		int num = 0;
		while(line != null) {
			if(number == ++num)
				return line;
			line = reader.readLine();
		}
		reader.close();
		return "";
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
}