package list;

public class PersonTest {
	public static void main(String[] args) {
		Person p=new Person();
		p.setFood("food123");
		p.setLuck("luck456");
		User user=new User(12,22,"wangsong");
		p.setUser(user);
		String xx=p.toString();
		System.out.println(xx);
	}

}
