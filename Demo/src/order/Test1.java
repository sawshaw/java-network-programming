package order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test1 {
	public static void main(String[] args) {
		String xx="[{'attrId':1,'attrValue':'圆通快递','cityId':200,'operType':'001','productSpecId':'GD9900131','servNbr':'GZYDWQ20160727151335'},{'attrId':2,'attrValue':'1','cityId':200,'operType':'001','productSpecId':'GD9900131','servNbr':'GZYDWQ20160727151335'},{'attrId':3,'attrValue':'1','cityId':200,'operType':'001','productSpecId':'GD9900131','servNbr':'GZYDWQ20160727151335'}]";
		JSONArray array=JSONArray.parseArray(xx);
		for(int i=0;i<array.size();i++){
			System.out.println(array.get(i));
			JSONObject param=(JSONObject) JSONObject.parse(array.get(i).toString());
			System.out.println(param.get("attrId"));
		}
		
	}
	

}
