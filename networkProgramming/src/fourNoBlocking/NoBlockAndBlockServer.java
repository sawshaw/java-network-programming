package fourNoBlocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author mercy
 * 接收客户连接按照阻塞模式，有就注册读就绪和写就绪事件,否则进入阻塞模式,直到接收到了客户连接负责接收数据和发送数据的线程采用非阻塞模式
 * 单独出一个accept方法
 * 
 */
public class NoBlockAndBlockServer {
	private Selector selector=null;
	private ServerSocketChannel serverSocketChannel=null;
	private int port=8000;
	private Charset charset=Charset.forName("GBK");//返回一个字符类型对象
	
	public NoBlockAndBlockServer() throws IOException{
		selector=Selector.open();//创建Selector对象
		serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务已启动...");
	}
	private Object gate=new Object();
	public void accept(){
		for(;;){
			try{
				SocketChannel socketChannel=serverSocketChannel.accept();
				socketChannel.configureBlocking(false);//设置无阻塞模式
				ByteBuffer buffer=ByteBuffer.allocate(6555);//创建一个ByteBuffer对象用于存放数据
				synchronized (gate) {
					selector.wakeup();//唤醒阻塞在select方法上的线程
					socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE,buffer);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	public void service() throws IOException{
		for(;;){
			synchronized (gate) {}
			int n=selector.select();//已经可以进行IO的通道数
			if(n==0){
				continue;
			}
			Set readyKeys=selector.selectedKeys();
			Iterator it=readyKeys.iterator();
			while(it.hasNext()){
				SelectionKey key=null;
				key=(SelectionKey)it.next();
				it.remove();//删除集合中的key
				try{
					if(key.isReadable()){//key的channel是否可读
						receive(key);
					}
					if(key.isWritable()){//key的channel是否可写
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
						ex.printStackTrace();
					}
				}
			}
		}
	}
	/**
	 * @param key
	 * @throws IOException
	 * @author mercy
	 * 根据读取的数据处理完返回给客户端
	 */
	public void send(SelectionKey key) throws IOException{
		ByteBuffer buffer=(ByteBuffer) key.attachment();//检索当前的文件
		SocketChannel socketChannel=(SocketChannel) key.channel();
		buffer.flip();//把极限(limit)设为位置(position)，把位置设为0
		String data=decode(buffer);//解码客户端发过来的数据
		if(data.indexOf("\r\n")==-1){//不包含\r\n直接return
			return ;
		}
		String outputData=data.substring(0, data.indexOf("\n")+1);
		System.out.println("客户端发送的数据:"+outputData); 
		ByteBuffer outputBuffer=encode("echo:"+outputData);//返回给客户端的数据
		while(outputBuffer.hasRemaining()){//buffer里面有数据
			socketChannel.write(outputBuffer);
		}
		ByteBuffer temp=encode(outputData);
		buffer.position(temp.limit());//设置buffer的位置为temp的极限
		buffer.compact();//删除已经处理的字符串
		if(outputData.equals("bye\r\n")){
			key.cancel();
			socketChannel.close();
			System.out.println("关闭与某客户端的连接");
		}
	}
	/**
	 * @param key
	 * @throws IOException
	 * @author mercy
	 * 读取客户端发来的数据
	 */
	public void receive(SelectionKey key) throws IOException{
		ByteBuffer buffer=(ByteBuffer) key.attachment();
		SocketChannel socketChannel=(SocketChannel) key.channel();
		ByteBuffer readBuffer=ByteBuffer.allocate(6555);//创建自定义内存的buffer
		socketChannel.read(readBuffer);
		readBuffer.flip();
		buffer.limit(buffer.capacity());//设置buffer的极限为buffer的容量
		buffer.put(readBuffer);
	}
	public String decode(ByteBuffer buffer){//解码
		CharBuffer charBuffer=charset.decode(buffer);
		return charBuffer.toString();
		
	}
	public ByteBuffer encode(String str){//编码
		return charset.encode(str);
	}
	public static void main(String[] args) throws IOException {
		final NoBlockAndBlockServer server=new NoBlockAndBlockServer();
		Thread accept= new Thread(){
			public void run(){
				server.accept();
			}
		};
		accept.start();
		server.service();
	}
}
