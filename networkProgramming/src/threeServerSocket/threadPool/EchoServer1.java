package threeServerSocket.threadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import threeServerSocket.Handler;
import threeServerSocket.ThreadServer;

/**
 * @author mercy
 * 利用jdk自带线程池处理客户端请求
 */
public class EchoServer1 {
	private int port=8000;
	private ServerSocket serverSocket;
	private ExecutorService servicePool;//线程池
	private final int poolSize=5;
	public EchoServer1() throws IOException{
		serverSocket=new ServerSocket(port);
		//返回当前电脑系统的CPU数目,CPU越多,线程池工作的数目也越多
		servicePool=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*poolSize);
		System.out.println("服务已启动...");
	}
	public void service(){
		while(true){
			Socket socket=null;
			try{
				socket=serverSocket.accept();
				servicePool.execute(new Handler(socket));//创建一个工线程
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args) throws IOException {
		new ThreadServer().service();
	}

}