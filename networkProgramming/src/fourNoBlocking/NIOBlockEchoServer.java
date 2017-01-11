package fourNoBlocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author mercy
 * 用nio创建阻塞式IO服务端(当ServerSocketChannel和SocketChannel采用阻塞式IO时为了处理多个连接必须使用多线程)
 */
public class NIOBlockEchoServer {
	private int port=8000;
	private ServerSocketChannel serverSocketChannel;
	private ExecutorService servicePool;//线程池
	private final int poolSize=5;
	public NIOBlockEchoServer() throws IOException{
		//返回当前电脑系统的CPU数目,CPU越多,线程池工作的数目也越多
		servicePool=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*poolSize);
		//创建对象
		serverSocketChannel=ServerSocketChannel.open();
		//设置在同一主机上关闭服务器端程序再启动该程序时候可以顺利绑定相同端口
		serverSocketChannel.socket().setReuseAddress(true);
		//使得服务器进程与本地端口绑定
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务已启动...");
	}
	public void service(){
		while(true){
			SocketChannel socketChannel=null;
			try{
				socketChannel=serverSocketChannel.accept();
				servicePool.execute(new Handler(socketChannel));//创建一个工作线程
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args) throws IOException {
		new NIOBlockEchoServer().service();
	}

}
