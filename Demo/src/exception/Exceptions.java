package exception;

public class Exceptions {
	public static String result(int a,int b){
		try{
			a=a/b;
		}catch(Exception e){
			return "0";
		}
		System.out.println("hello");
		return "1";
	}
	public static String isTrue(){
		int a=4;
		if(a==5){
			return "true";
		}
		System.out.println("你好");
		return "false";
	}
	public static void main(String[] args) {
		//System.out.println(result(4,0));
		System.out.println(isTrue());
	}

}
