package mainPack;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Sort {
	public static JSONArray sort(JSONArray x, String type) {
		List<JSONObject> Jsons = new ArrayList<JSONObject>();
		JSONArray y = new JSONArray();
		for (int i = 0; i < x.size(); i++)
			Jsons.add(x.getJSONObject(i));
		if(type.equals("1")) {
			Collections.sort(Jsons, new Comparator<JSONObject>() {
	            private static final String KEY = "label";
	            @Override
	            public int compare(JSONObject a, JSONObject b) {
	                String valA = a.getString(KEY);
	                String valB = b.getString(KEY);
	                return  valA.length() - valB.length();
	            }
	        });
		}
		else if(type.equals("2")) {
			@SuppressWarnings("rawtypes")
			Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
			Collections.sort(Jsons, new Comparator<JSONObject>() {
	            private static final String KEY = "label";
	            @SuppressWarnings("unchecked")
				@Override
	            public int compare(JSONObject a, JSONObject b) {
	                String valA = a.getString(KEY);
	                String valB = b.getString(KEY);
	                return  cmp.compare(valA, valB);
	            }
	        });
		}
		else
			return x;
		
		for (int i = 0; i < x.size(); i++)
			y.add((JSONObject)Jsons.get(i));
		return y;
	}
	public static JSONArray select(JSONArray x, String word) {
		JSONArray y = new JSONArray();
		if(word == null)
			return x;
		String [] arr = word.split("\\s+");
		for(int i = 0; i < x.size(); i++) {
			JSONObject item = x.getJSONObject(i);
			boolean flag = true;
			for(String e : arr) {
				if(!item.getString("label").contains(e))
					flag = false;
			}
			if(flag == true)
				y.add(item);
		}
		return y;
	}
}