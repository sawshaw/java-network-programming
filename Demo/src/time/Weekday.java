package time;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Weekday {
	public static void main(String[] args) {
		//long time=System.currentTimeMillis();
		Date date=new Date();
		long time=date.getTime();
		Timestamp ts=new Timestamp(time);
		System.out.println(ts);
		int hour=ts.getHours();
		System.out.println(hour);
		System.out.println();
		if(hour>=8&&hour<=18){
			System.out.println("xxxx");
		}else{
			System.out.println("oooo");
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			System.out.println("oooo");
		}else{
			System.out.println("xxxx");
		}
	}

}
