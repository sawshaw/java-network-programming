package fourNoBlocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author mercy
 * 非阻塞式IO客户端
 */
public class NoBlockClient {
	private SocketChannel socketChannel=null;
	private ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
	private Charset charset=Charset.forName("GBK");
	private Selector selector;
	private int port=8000;
	public NoBlockClient() throws IOException{
		socketChannel=SocketChannel.open();
		InetAddress add=InetAddress.getLocalHost();
		InetSocketAddress isa=new InetSocketAddress(add, port);
		socketChannel.connect(isa);//采用阻塞模式连接服务器
		socketChannel.configureBlocking(false);//采用非阻塞模式
		System.out.println("连接服务器成功....");
		selector=Selector.open();
	}
	/**
	 * 
	 * @author mercy
	 * 接收用户从控制台输入的数据
	 */
	public void receiveFromUser(){
		try{
			//用户输入的数据放入buffer中
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			String msg=null;
			while((msg=br.readLine())!=null){
				synchronized (sendBuffer) {
					sendBuffer.put(encode(msg+"\r\n"));
				}
				if(msg.equals("bye")){
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param key
	 * @author mercy
	 * 发送sendBuffer中的数据
	 * @throws IOException 
	 */
	public void send(SelectionKey key) throws IOException{
		SocketChannel socketChannel=(SocketChannel) key.channel();
		synchronized (sendBuffer) {
			sendBuffer.flip();//将极限(limit)设为位置(position)，将位置设为0
			socketChannel.write(sendBuffer);
			sendBuffer.compact();//删除已经发送的数据
		}
	}
	/**
	 * @param key
	 * @throws IOException
	 * @author mercy
	 * 接收服务端发送的数据,如果buffer中有数据则打印,然后从buffer中删除
	 */
	public void receive(SelectionKey key) throws IOException{
		SocketChannel socketChannel=(SocketChannel) key.channel();
		socketChannel.read(receiveBuffer);
		receiveBuffer.flip();
		String receiveData=decode(receiveBuffer);
		if(receiveData.indexOf("\n")==-1){
			return;
		}
		String outputData=receiveData.substring(0, receiveData.indexOf("\n")+1);
		System.out.println(outputData);
		if(outputData.equals("echo:bye\r\n")){
			key.cancel();
			socketChannel.close();
			System.out.println("连接服务器关闭...");
			selector.close();
			System.exit(0);
		}
		ByteBuffer temp=encode(outputData);
		receiveBuffer.position(temp.limit());
		receiveBuffer.compact();//删除已经打印的数据
	}
	
	public String decode(ByteBuffer buffer){//解码
		CharBuffer charBuffer=charset.decode(buffer);
		return charBuffer.toString();
		
	}
	public ByteBuffer encode(String str){//编码
		return charset.encode(str);
	}
	public void talk()throws IOException{
		socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
		while(selector.select()>0){
			Set readyKeys=selector.selectedKeys();
			Iterator it=readyKeys.iterator();
			while(it.hasNext()){
				SelectionKey key=null;
				try{
					key=(SelectionKey) it.next();
					it.remove();
					if(key.isReadable()){
						receive(key);
					}
					if(key.isWritable()){
						send(key);
					}
				}catch(IOException e){
					e.printStackTrace();
					try{
						if(key!=null){
							key.cancel();
							key.channel().close();
						}
					}catch(Exception ex){
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static void main(String[] args) throws IOException {
		final NoBlockClient client=new NoBlockClient();
		Thread receiver=new Thread(){
			public void run(){
				client.receiveFromUser();//接收用户输入的数据
			}
		};
		receiver.start();
		client.talk();
	}

}
