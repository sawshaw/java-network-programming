package fourNoBlocking;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;


/**
 * @author mercy
 * 阻塞式NIO客户端
 */
public class NIOBlockEchoClient {
	private SocketChannel socketChannel=null;
	private int port=8000;
	public NIOBlockEchoClient()throws IOException{
		socketChannel=SocketChannel.open();
		InetAddress ad=InetAddress.getLocalHost();
		InetSocketAddress isa=new InetSocketAddress(ad,port);
		socketChannel.connect(isa);//连接服务
		//获取绑定后的端口号和绑定前的端口号
	}
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut=socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	private BufferedReader getReader(Socket socket)throws IOException{
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}
	
	public void talk() throws IOException{
		try{
			BufferedReader br=getReader(socketChannel.socket());
			PrintWriter pw=getWriter(socketChannel.socket());
			BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
			String msg=null;
			while((msg=reader.readLine())!=null){
				pw.println(msg);
				System.out.println(br.readLine());
				if(msg.equals("bye")){
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			socketChannel.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException{
		new NIOBlockEchoClient().talk();
	}


}
