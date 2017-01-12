package eightUdpSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class EchoServer {
	private int port=8000;
	private DatagramSocket socket;//发送数据报的socket
	private EchoServer() throws SocketException{
		socket=new DatagramSocket(port); //与本地的固UDP定端口绑定
		System.out.println("UDP服务端已启动...");
	}
	private String echo(String msg){
		return "echo:"+msg;
	}
	public void service(){
		while(true){
			try{
				DatagramPacket packet=new DatagramPacket(new byte[512], 512);//构造接收包的长度
				socket.receive(packet);//接收来自任意client的数据报
				String msg=new String(packet.getData(),0,packet.getLength());//解码并组合成msg
				System.out.println(packet.getAddress()+":"+packet.getPort()+">"+msg);//服务端输出数据
				packet.setData(echo(msg).getBytes());//字节数组类型
				socket.send(packet);//给客户端返回数据
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws SocketException {
		new EchoServer().service();
	}

}
