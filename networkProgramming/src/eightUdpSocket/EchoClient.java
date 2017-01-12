package eightUdpSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoClient {
	private String remotHost="localhost";
	private int remotPort=8000;
	private DatagramSocket socket;//发送数据报的socket
	private EchoClient() throws SocketException{
		socket=new DatagramSocket(); //与本地任意一个UDP端口绑定
		System.out.println("UDP客户端已启动...");
	}
	public void talk(){
		try {
			InetAddress remotIp=InetAddress.getByName(remotHost);
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			String msg=null;
			while((msg=br.readLine())!=null){
				byte[] outputData=msg.getBytes();
				DatagramPacket outputPacket=new DatagramPacket(outputData,outputData.length,remotIp,remotPort);
				socket.send(outputPacket);//往服务端发数据报
				DatagramPacket inputPacket=new DatagramPacket(new byte[512], 512);
				socket.receive(inputPacket);//接收服务端的返回的数据报
				System.out.println(new String(inputPacket.getData(),0,inputPacket.getLength()));
				if(msg.equals("bye")){
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			socket.close();
		}
	}
	public static void main(String[] args) throws IOException {
		new EchoClient().talk();
	}

}
