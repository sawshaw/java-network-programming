package tenReflectAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectTest {
	public Object copy(Object obj) throws Exception{
		//获得对象的类型
		Class classType=obj.getClass();
		System.out.println("Class:"+classType.getName());
		//通过默认的构造方法创建一个新对象
		Object objectCopy=classType.getConstructor(new Class[]{}).newInstance(new Object[]{});
		//获得对象的所有属性
		Field fileds[]=classType.getDeclaredFields();
		for(Field f:fileds){
			String fieldName=f.getName();
			System.out.println("fieldName==="+fieldName);
			String firstLetter=fieldName.substring(0, 1).toUpperCase();
			System.out.println("firstLetter:"+firstLetter);
			//获得属性的getxxx方法名字
			String getMethodName="get"+firstLetter+fieldName.substring(1);
			System.out.println("getMethodName:"+getMethodName);
			//获得属性的setxxx方法名字
			String setMethodName="set"+firstLetter+fieldName.substring(1);
			System.out.println("setMethodName:"+getMethodName);
			//获得和属性对应的getxxxsetxxx方法
			Method getMethod=classType.getMethod(getMethodName,new Class[]{});
			System.out.println("getMethod:"+getMethod);
			Method setMethod=classType.getMethod(setMethodName,new Class[]{f.getType()});
			System.out.println("setMethod:"+setMethod);
			//调用原对象的getxx方法
			Object value=getMethod.invoke(obj, new Object[]{});
			System.out.println("value:"+value);
			//调用复制对象的setxx方法
			setMethod.invoke(objectCopy,new Object[]{value});
		}
		return objectCopy;
	}
	public static void main(String[] args) throws Exception {
		Customer c=new Customer(1, "Tom", 23);
		Customer cp=(Customer) new ReflectTest().copy(c);
		System.out.println("CopyInformation:"+cp.getName()+"--"+cp.getAge());
	}

}
