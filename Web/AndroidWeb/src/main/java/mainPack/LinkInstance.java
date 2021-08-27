package mainPack;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
		try {
		    PostMethod postMethod = new PostMethod(linkInstanceURL) ;
		    postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") ;
		    NameValuePair[] data = {
		            new NameValuePair("context", context),
		            new NameValuePair("course", course),
		            new NameValuePair("id", id),
		    };
		    postMethod.setRequestBody(data);
		    org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
		    httpClient.executeMethod(postMethod);
		    result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {}
		return result;
    }
}