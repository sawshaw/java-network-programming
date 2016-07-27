package list;

import java.util.ArrayList;
import java.util.List;

public class ListNull {
	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		if(list!=null&&!list.isEmpty()){
			System.out.println("不为空");
			System.out.println(list.get(0));
		}else{
			System.out.println("空");
		}
	}

}
