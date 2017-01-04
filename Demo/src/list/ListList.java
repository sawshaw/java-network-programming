package list;

import java.util.ArrayList;
import java.util.List;

public class ListList {
	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		List<String> list1=new ArrayList<String>();
		list.add("aa");list.add("cc");list.add("bb");
		list.addAll(list1);
		System.out.println(list.size());
		for(String i:list){
			System.out.println(i);
		}
	}

}
