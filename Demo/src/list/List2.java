package list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class List2 {
	public static void main(String[] args) {
		List<User> us=new ArrayList<User>();
		User u=new User("A",1);
		us.add(u);
		u=new User("C",2);
		us.add(u);
		u=new User("D",5);
		us.add(u);
		u=new User("A",4);
		us.add(u);
		u=new User("D",6);
		us.add(u);
		Map<String,Integer> map1=new HashMap<String,Integer>();
		for(int i=0;i<us.size();i++){
			map1.put(us.get(i).getName(),0);
			
		}
		for(int i=0;i<us.size();i++){
			if(map1.containsKey(us.get(i).getName())){
				map1.put(us.get(i).getName(),us.get(i).getId()+map1.get(us.get(i).getName()));
			}
			
		}
		
		for(Map.Entry<String, Integer> entry : map1.entrySet())    
		{    
		    System.out.println(entry.getKey()+": "+entry.getValue());    
		} 
	}

}
