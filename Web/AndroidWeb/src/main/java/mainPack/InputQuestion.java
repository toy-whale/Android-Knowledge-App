package mainPack;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
		try {
		    PostMethod postMethod = new PostMethod(inputQuestionURL) ;
		    postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") ;
		    NameValuePair[] data = {
		            new NameValuePair("course", course),
		            new NameValuePair("inputQuestion", inputQuestion),
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