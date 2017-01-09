package threeServerSocket.threadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import threeServerSocket.Handler;
import threeServerSocket.ThreadServer;

/**
 * @author mercy
 * 利用线程池处理客户端请求
 */
public class EchoServer {
	private int port=8000;
	private ServerSocket serverSocket;
	private ThreadPool threadPool;
	private final int poolSize=5;
	public EchoServer() throws IOException{
		serverSocket=new ServerSocket(port);
		threadPool=new ThreadPool(poolSize);
		System.out.println("服务已启动...");
	}
	public void service(){
		while(true){
			Socket socket=null;
			try{
				socket=serverSocket.accept();
				threadPool.execute(new Handler(socket));//创建一个工线程
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args) throws IOException {
		new ThreadServer().service();
	}

}
