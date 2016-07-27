package time;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTest {
	public static void main(String[] args) {
		Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        System.out.println(nowTime+"\n");
        Timestamp xx = Timestamp.valueOf(nowTime);
        System.out.println(xx+"\n");
        String xxx="xxxxx"+1;
        System.out.println(xxx);
	}

}
