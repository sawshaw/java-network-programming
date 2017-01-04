package list;

import java.util.ArrayList;
import java.util.List;

public class List1 {
	public static void main(String[] args) {
		List<Integer> list=new ArrayList<Integer>();
		List<Integer> lists=new ArrayList<Integer>();
		for(int i=1;i<10;i++){
			list.add(i);
		}
		lists.addAll(list);
		List<Integer> list1=new ArrayList<Integer>();
		//lists.addAll(list1);
		System.out.println(lists.size());
		for(int i=0;i<lists.size();i++){
			System.out.println("i:"+i);
			System.out.println(list.get(i));
		}
	}

}
