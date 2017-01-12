package twoSocketUsage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author mercy
 *超时测试服务端
 */
public class Server {
	private int port=8000;
	private ServerSocket serverSocket;
	public Server() throws IOException{
		serverSocket=new ServerSocket(port);
		System.out.println("服务已启动...");
	}
	public void service(){
		Socket socket=null;
		try {
			socket=serverSocket.accept();
			System.out.println("客户端已连接上....");
			System.out.println("new Connecttion accept" +socket.getInetAddress()+":"+socket.getPort()+",localPort:"+socket.getLocalPort());
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) throws IOException{
		new Server().service();
	}

}
