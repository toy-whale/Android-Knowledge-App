package mainPack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
package mainPack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class QuestionListByUriName {
	private static String questionListByUriNameURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName";
	private static String patternA = "(.*)[A][\56](.*)[B]";
	private static String patternB = "(.*)[B][\56](.*)[C]";
	private static String patternC = "(.*)[C][\56](.*)[D]";
	private static String patternD = "(.*)[D][\56](.*)$";
	private static Pattern ra = Pattern.compile(patternA);
	private static Pattern rb = Pattern.compile(patternB);
	private static Pattern rc = Pattern.compile(patternC);
	private static Pattern rd = Pattern.compile(patternD);
	
	public static String get(String uriName, String id) throws Exception {
		String s = sendGet(uriName, id);
		JSONObject result = JSONObject.parseObject(s);
		JSONArray data = result.getJSONArray("data");
		JSONArray answerList = new JSONArray();
		if(data == null) {
			JSONObject x = new JSONObject();
			x.put("data", answerList);
			String answer = x.toString();
			return answer;
		}
		for(int i = 0; i < data.size(); i++) {
			JSONObject x = (JSONObject)data.get(i);
			JSONObject item = new JSONObject();
			String qBody = x.getString("qBody");
			String qAnswer = x.getString("qAnswer");
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
			item.put("qBody", qBody);
			if(A == "" || B == "" || C == "" || D == "" || Body == "")
				continue;
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
		try {
		    GetMethod getMethod = null;
		    getMethod = new GetMethod(questionListByUriNameURL);
		    NameValuePair[] data = {
		            new NameValuePair("uriName", uriName),
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