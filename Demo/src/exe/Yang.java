package exe;

/**
 * @author mercy
 *一只羊，4年后每年生一只，求N年生多少只?
 */
public class Yang {
	public static int getYang(int n){
		int sum=0;
		if(n<3){
			return 0;
		}else{
			int m=n/3;
			for(int i=1;i<=m;i++){
				sum+=n-(3*i);
			}
		}
		return sum;
		
		
	}
	
	public static void main(String[] args) {
		int n=10;
		System.out.println(getYang(n));
	}

}
