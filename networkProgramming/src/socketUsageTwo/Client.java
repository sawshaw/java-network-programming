package socketUsageTwo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author mercy
 *超时测试客户端
 */
public class Client {
	private String 	host="localhost";
	private int port=8000;
	private Socket socket;
	public Client()throws IOException{
		socket=new Socket();
		SocketAddress remotAddr=new InetSocketAddress(host, port);
		socket.connect(remotAddr, 1000);//设置等待时间为一秒钟
		//获取绑定后的端口号和绑定前的端口号
		System.out.println("new Connecttion accept" +socket.getInetAddress()+":"+socket.getPort()+",localPort:"+socket.getLocalPort());
	}
	public void test(){
		System.out.println("连接服务端测试...");
	}
	public static void main(String[] args) throws IOException{
		new Client().test();
	}

}
