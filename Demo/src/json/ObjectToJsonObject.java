package json;

import com.alibaba.fastjson.JSONObject;

import object.User;

public class ObjectToJsonObject {
	public static void main(String[] args) {
		User user=new User();
		user.setId(12);user.setName("zhongguo");
		JSONObject object=(JSONObject) JSONObject.toJSON(user);
		System.out.println(object);
	}

}
