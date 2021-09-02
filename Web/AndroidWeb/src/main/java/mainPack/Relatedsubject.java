package mainPack;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
package mainPack;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class Relatedsubject {
	private static String relatedsubjectURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject";
	public static String get(String course, String subjectName, String id) throws Exception {
		String s = sendPost(course, subjectName, id);
		s = s.replaceAll("<br>", "");
		s = s.replaceAll("\n","");
		JSONObject result = JSONObject.parseObject(s);
		JSONArray data = result.getJSONArray("data");
		JSONObject sList = new JSONObject();
		JSONObject vList = new JSONObject();
		for(int i = 0; i < data.size(); i++) {
			JSONObject x = data.getJSONObject(i);
			if(x.getString("subject") != null && x.getString("subject").equals(subjectName)) {
				String p = x.getString("predicate");
				if(p != null && sList.get(p) == null) {
					JSONArray u = new JSONArray();
					u.add(x.getString("value"));
					sList.put(p, u);
				}
				else if(p != null && sList.get(p) != null) {
					JSONArray u = sList.getJSONArray(p);
					u.add(x.getString("value"));
					sList.put(p, u);
				}
			}
			else if(x.getString("subject") != null && x.getString("value").equals(subjectName)) {
				String p = x.getString("predicate");
				if(p != null && vList.get(p) == null) {
					JSONArray u = new JSONArray();
					u.add(x.getString("subject"));
					vList.put(p, u);
				}
				else if(p != null && vList.get(p) != null) {
					JSONArray u = vList.getJSONArray(p);
					u.add(x.getString("subject"));
					vList.put(p, u);
				}
			}
			else
				continue;
		}
		String r = QuestionListByUriName.get(subjectName, id);
		JSONObject rjson = JSONObject.parseObject(r);
		JSONArray qset = rjson.getJSONArray("data");
		if(qset != null && qset.size() > 0)
			sList.put("相关习题", qset);
		JSONObject item = new JSONObject();
		item.put("subject", sList);
		item.put("value", vList);
		String answer = item.toString();
		return answer;
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