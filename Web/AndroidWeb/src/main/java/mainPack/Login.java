package mainPack;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;

public class Login {
	static String loginURL = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
	public static String get(String phone, String password) throws Exception {
		String s = sendPost(phone, password);
		JSONObject result = JSONObject.parseObject(s);
		String id = result.getString("id");
		return id;
	}
	static String sendPost(String phone, String password) throws Exception {
		String result = "";
		try {
		    PostMethod postMethod = new PostMethod(loginURL) ;
		    postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") ;
		    NameValuePair[] data = {
		            new NameValuePair("password", password),
		            new NameValuePair("phone", phone)
		    };
		    postMethod.setRequestBody(data);
		    org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
		    httpClient.executeMethod(postMethod);
		    result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {}
		return result;
    }
}
