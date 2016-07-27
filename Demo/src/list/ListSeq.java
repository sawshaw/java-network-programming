package list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListSeq {
	public static void main(String[] args) {
		List<User> list1=new ArrayList<User>();
		for(int i=0;i<5;i++){
			User user=new User();
			user.setAge(i);
			user.setId(i);
			user.setName("age"+i);
			list1.add(user);
		}
		Collections.sort(list1, new Comparator<User>() {// 排序
			@Override
			public int compare(User o1, User o2) {
				if (o1.getAge()==null) {
					o1.setAge(1000);
				}
				if (o2.getAge()== null) {
					o2.setAge(1000);
				}
				//return o2.getAge().compareTo(o1.getAge());//倒序
				return o1.getAge().compareTo(o2.getAge());//顺序
			}
		});
		System.out.println(list1.get(1).getAge());//顺序是1,倒序是3
	}

}
