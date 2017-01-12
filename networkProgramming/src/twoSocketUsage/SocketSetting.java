package twoSocketUsage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author mercy
 * socket 常用的用法
 */
public class SocketSetting {
	Socket socket=null;
	public void setSocket() throws IOException{
		socket=new Socket();
		SocketAddress remotAddr=new InetSocketAddress("localhost", 8000);
		//计算超时时间
		long begain =System.currentTimeMillis();
		socket.connect(remotAddr, 60000);//设置等待时间为一分钟(等待时间过短会超时)
		long end=System.currentTimeMillis();
		System.out.println("连接所花时间:"+(end-begain));
		InetAddress addr=InetAddress.getLocalHost();//获得本机IP地址
		System.out.println(addr);
		//设置客户端地址
		InetAddress remotAdr=InetAddress.getByName("127.0.0.1");//服务器地址
		InetAddress localAdr=InetAddress.getByName("192.168.22.2");
		Socket socket1=new Socket(remotAdr,8000,localAdr,64116);//连接指定的服务器地址
		
	}
	public static void main(String[] args) throws IOException {
		new SocketSetting().setSocket();
	}

}
