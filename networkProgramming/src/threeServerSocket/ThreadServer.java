package threeServerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author mercy
 *多线程服务器
 */
public class ThreadServer {
	private int port=8000;
	private ServerSocket serverSocket;
	public ThreadServer() throws IOException{
		serverSocket=new ServerSocket(port);
		System.out.println("服务已启动...");
	}
	public void service(){
		while(true){
			Socket socket=null;
			try{
				socket=serverSocket.accept();
				Thread workThread=new Thread(new Handler(socket));//创建一个工线程
				workThread.start();//启动工作线程
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args) throws IOException {
		new ThreadServer().service();
	}
}
