package mainPack;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;


public class Relatedsubject {
	private static String relatedsubjectURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject";
	public static String get(String course, String subjectName, String id) throws Exception {
		String s = sendPost(course, subjectName, id);
		//JSONObject result = JSONObject.parseObject(s);
		//String id = result.getString("id");
		return s;
	}
	private static String sendPost(String course, String subjectName, String id) throws Exception {
		String result = "";
		try {
		    PostMethod postMethod = new PostMethod(relatedsubjectURL) ;
		    postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") ;
		    NameValuePair[] data = {
		            new NameValuePair("course", course),
		            new NameValuePair("subjectName", subjectName),
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