package string;

public class ContainString {
	public static void main(String[] args) {
		String x="123xxt345";
		String y="xxt";
		if(x.contains(y)){
			System.out.println("包含");
		}else{
			System.out.println("不包含");
		}
	}

}
