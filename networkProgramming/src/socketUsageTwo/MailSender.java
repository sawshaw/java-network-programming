package socketUsageTwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author mercy
 * 用socket发邮件
 */
public class MailSender {
	private String smtpServer="smtp.189.cn";
	private int port=25;
	public static void main(String[] args) {
		Message msg=new Message("18026276684@189.cn","18925121155@189.cn","hello","hi,i miss u");
		new MailSender().sendMail(msg);
		
	}
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut=socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	private BufferedReader getReader(Socket socket)throws IOException{
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}
	public void sendMail(Message msg){
		Socket socket=null;
		try{
			socket=new Socket(smtpServer,port);
			BufferedReader br=getReader(socket);
			PrintWriter pw= getWriter(socket);
			String localhost=InetAddress.getLocalHost().getHostName();//获取主机名
			sendAndReceive(null, br, pw);//仅仅是为了接收服务器的响应数据
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
	private void sendAndReceive(String str,BufferedReader br,PrintWriter pw)throws IOException{
		if(str!=null){
			System.out.println("Client>"+str);
			pw.println(str);
		}
		String response;
		if((response=br.readLine())!=null){
			System.out.println("Server>"+response);
		}
	}
	static class Message{
		String from;
		String to;
		String subject;
		String content;
		String data;//邮件内容包括标题和正文
		public Message(String from,String to,String subject,String content){
			this.from=from;
			this.to=to;
			this.subject=subject;
			this.content=content;
			this.data="Subject:"+subject+"\r\nContent:"+content;
		}
		
		
	}

}
