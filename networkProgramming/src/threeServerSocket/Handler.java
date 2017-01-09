package threeServerSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler implements Runnable{
	private Socket socket;
	public Handler(Socket socket){
		this.socket=socket;
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
	@Override
	public void run() {
		System.out.println("服务端执行线程..绑定的客户端port:"+socket.getPort());
		try{
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
		}catch(IOException e){
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

}
