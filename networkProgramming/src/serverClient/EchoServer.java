package serverClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private int port=8000;
	private ServerSocket serverSocket;
	public EchoServer() throws IOException{
		serverSocket=new ServerSocket(port);
		System.out.println("服务已启动...");
	}
	public String echo(String msg){
		return "echo:"+msg;
	}
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut=socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	private BufferedReader getReader(Socket socket)throws IOException{
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}
	public void service(){
		Socket socket=null;
		try{
			while(true){
				socket=serverSocket.accept();
				System.out.println("new Connecttion accept" +socket.getInetAddress()+":"+socket.getPort());
				BufferedReader br=getReader(socket);
				PrintWriter pw=getWriter(socket);
				String msg=null;
				while((msg=br.readLine())!=null){
					System.out.println(msg);
					pw.println(echo(msg));
					if(msg.equals("bye")){
						break;
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
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
	public static void main(String[] args)throws IOException{
		new EchoServer().service();
	}

}
