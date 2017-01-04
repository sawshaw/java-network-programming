package file;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {
	public static void main(String[] args) {
		String file="classpath:aa.properties";
		Properties prop = new Properties();
		String value="";
		try{
			InputStream is = new FileInputStream(file);
			prop.load(is);
			value=prop.getProperty("user");
		}catch(Exception e){
			System.out.println("解析错误...");
		}
		System.out.println(value);
	}

}
