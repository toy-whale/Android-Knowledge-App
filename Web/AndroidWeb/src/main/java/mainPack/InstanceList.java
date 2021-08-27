package mainPack;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class InstanceList {
	private static String instanceListURL = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList";
	public static String get(String course, String searchKey, String id) throws Exception {
		String s = sendGet(course, searchKey, id);
		JSONObject result = JSONObject.parseObject(s);
		JSONArray data = result.getJSONArray("data");
		JSONObject item = new JSONObject();
		JSONArray dataList = new JSONArray();
		List<String> labels = new ArrayList<String>();
		for(int i = 0; i < data.size(); i++) {
			JSONObject x = (JSONObject)data.get(i);
			JSONObject y = new JSONObject();
			String label = x.getString("label");
			if(labels.contains(label))
				continue;
			else
				labels.add(label);
			y.put("label", x.getString("label"));
			y.put("category", x.getString("category"));
			dataList.add(y);
		}
		item.put("data", dataList);
		String answer = item.toString();
		return answer;
	}
	private static String sendGet(String course, String searchKey, String id) throws Exception {
		String result = "";
		try {
		    GetMethod getMethod = null;
		    getMethod = new GetMethod(instanceListURL);
		    NameValuePair[] data = {
		            new NameValuePair("course", course),
		            new NameValuePair("searchKey", searchKey),
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