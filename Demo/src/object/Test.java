package object;

import org.springframework.beans.BeanUtils;

public class Test {
	public static void main(String[] args) {
		User user=new User();
		user.setId(15);
		user.setName("wangsong");
		Student s=new Student();
		BeanUtils.copyProperties(user,s);
		System.out.println(s.getId()+"\n"+s.getName());
	}

}
