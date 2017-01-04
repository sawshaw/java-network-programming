package exe;

public class UpCase {
	public static String upcase(String s){
		String x=s.toUpperCase();
		return x;
	}
	public static String lowcase(String s){
		String x=s.toLowerCase();
		return x;
	}
	public static void main(String[] args) {
		String s="wweddd";
		String xx=upcase(s);
		System.out.println(xx);
		String s1="WWDWDE";
		String xx1=lowcase(s1);
		System.out.println(xx1);
	}

}
