package list;

import java.util.ArrayList;
import java.util.List;

public class SetUserList {
	public static void main(String[] args) {
		List<User> ulist=new ArrayList<User>();
		for(int i=0;i<10;i++){
			User user=new User();
			user.setAge(i);
			user.setId(i+1);
			ulist.add(user);
		}
		System.out.println(ulist.get(0).getAge()+ulist.get(0).getName()+ulist.get(0).getId());
		for(User user1:ulist){
			user1.setName(user1.getAge()+"name");
		}
		for(int i=0;i<ulist.size();i++){
			System.out.println(ulist.get(i).getName());
		}
		System.out.println(ulist.get(2).getName());
	}

}
