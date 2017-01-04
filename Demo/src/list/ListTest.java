package list;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
	public static void main(String[] args) {
		List<User> users=new ArrayList<User>();
		User user2=new User();
		user2.setAge(1);
		for(int i=0;i<10;i++){
			User user=new User();
			user.setAge(i);
			user.setName("name"+i);
			users.add(user);
		}
		System.out.println(users.toString());
		for(User user1:users){
			System.out.println(user1.getAge()+"====="+user1.getName());
		}
		
	}

}
