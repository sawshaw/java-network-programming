package socketUsageTwo;

import java.io.IOException;
import java.net.Socket;

/**
 * @author mercy
 *判断端口是否被服务器监听，若socket对象创建成功，表明端口已被服务端程序监听占用
 */
public class PortScan {
	public void scan(String host){
		Socket socket=null;
		for(int port=0;port<1024;port++){
			try{
				socket=new Socket(host,port);
				System.out.println("There is a server on port "+port);
			}catch(IOException e){
				System.out.println("can't connect to port "+port);
			}finally{
				try{
					if(socket!=null){
						socket.close();
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		String host="localhost";
		if(args.length>0){    //可以在控制台自定义输入host地址
			host=args[0];
		}
		new PortScan().scan(host);
	}
}
