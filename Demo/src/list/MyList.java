package list;

import java.util.ArrayList;
import java.util.List;

public class MyList {
	public static void main(String[] args) {
		List<String> list1=new ArrayList<String>();
		List<String> list2=new ArrayList<String>();
		list1.add("a");list1.add("b");list1.add("c");
		list2.add("1");list2.add("2");list2.add("3");list2.add("4");
		List<List<String>> ls=new ArrayList<List<String>>();
		ls.add(list1);
		ls.add(list2);
		for(int i=0;i<ls.size();i++){
			for(int j=0;j<3;j++){
				System.out.print(ls.get(i).get(j));
			}
			System.out.println("\n");
		}
	}
}