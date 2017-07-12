package serverClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
	private String 	host="localhost";
	private int port=10002;
	private Socket socket;
	public EchoClient()throws IOException{
		socket=new Socket(host,port);
		//获取绑定后的端口号和绑定前的端口号
		System.out.println("new Connecttion accept" +socket.getInetAddress()+":"+socket.getPort()+",localPort:"+socket.getLocalPort());
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
			BufferedReader br=getReader(socket);
			PrintWriter pw=getWriter(socket);
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
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException{
		new EchoClient().talk();
	}

}
