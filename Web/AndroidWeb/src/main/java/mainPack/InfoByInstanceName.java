package mainPack;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class InfoByInstanceName {
	private static String pattern = "^http:(.*)";
	private static String infoByInstanceNameURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName";
	public static String get(String course, String name, String id) throws Exception {
		String s = sendGet(course, name, id);
		System.out.println(s);
		JSONObject result = JSONObject.parseObject(s);
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
			if(object.matches(pattern))
				continue;
			y.put("object", object);
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
			content.add(y);
		}
		item.put("property", property);
		item.put("content", content);
		String answer = item.toString();
		return answer;
	}
	private static String sendGet(String course, String name, String id) throws Exception {
		String result = "";
		try {
		    GetMethod getMethod = null;
		    getMethod = new GetMethod(infoByInstanceNameURL);
		    NameValuePair[] data = {
		            new NameValuePair("course", course),
		            new NameValuePair("name", name),
		            new NameValuePair("id", id)
		    };
		    getMethod.setQueryString(data);
		    org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
		    httpClient.executeMethod(getMethod);
		    result = getMethod.getResponseBodyAsString();
		} catch (Exception e) {}
		return result;
    }
}