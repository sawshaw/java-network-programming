package test;

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
 * NIO创建非阻塞式服务端
 * 一个主线程能接收客户的连接，接收客户的请求，向客户发回响应数据
 * ByteBuffer是字节缓存区,存放的是字节，要转换成字符串需要编解码
 */
public class NoBlockServer {
	private Selector selector=null;
	private ServerSocketChannel serverSocketChannel=null;
	private int port=10002;
	//private Charset charset=Charset.forName("GBK");//返回一个字符类型对象
	
	public NoBlockServer() throws IOException{
		selector=Selector.open();//创建Selector对象
		serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.configureBlocking(false);//设置无阻塞模式
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务已启动...");
	}
	
	public void service() throws IOException{
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//用给定的选择器注册channel(接收就绪channel)，并返回一个key
		while(selector.select()>0){//轮循检查可用的channel个数
			//每个request会封装一个channel，将所有的channel注册在一个Selector上，然后selector开始不断的轮询每个request的可读状态，如果可读，则直接返回可读状态的channel。
			Set readyKeys=selector.selectedKeys();//循环遍历已经注册好的通道
			Iterator it=readyKeys.iterator();
			while(it.hasNext()){
				SelectionKey key=null;
				try{
					key=(SelectionKey)it.next();
					it.remove();//删除集合中的key
					if(key.isAcceptable()){//是否可以接收客户端的socket连接
						ServerSocketChannel ssc=(ServerSocketChannel)key.channel();//返回key注册的通道
						SocketChannel socketChannel=ssc.accept();
						System.out.println("接收到的客户端连接,来自："+socketChannel.socket().getInetAddress()+":"+socketChannel.socket().getPort());
						socketChannel.configureBlocking(false);//设置无阻塞模式
						ByteBuffer buffer=ByteBuffer.allocate(6000);//创建一个ByteBuffer对象用于存放数据(数据存放缓冲区)
						socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE,buffer);//注册事件,Selector会监控事件是否发生
					}
					if(key.isReadable()){//key的channel是否可读
						receive(key);
					}
					if(key.isWritable()){//key的channel是否可写
						send(key);
					}
				}catch(IOException e){
					System.out.println("exception....");
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
		ByteBuffer buffer=(ByteBuffer) key.attachment();//获取与当前通道一起使用的buffer
		SocketChannel socketChannel=(SocketChannel) key.channel();//获取key创建的通道
		buffer.flip();//把极限(limit)设为位置(position)，把位置设为0,将写模式切换到读模式
		String data=decode(buffer);//解码客户端发过来的数据
		if(data.length()==0){//不包含\r\n直接return
			return ;
		}
		String outputData=data;//.substring(0, data.indexOf("\n")+1);
		System.out.println("客户端发送的数据:"+outputData);
		//String reply="FFFF02141433570200012400050301IBSS01662       001023CS0214143357*0189086510002001100301420170214143500004003099005007success";
		ByteBuffer outputBuffer=encode("echo:"+outputData);//返回给客户端的数据
		//ByteBuffer outputBuffer=encode("echo:"+reply);//返回给客户端的数据
		while(outputBuffer.hasRemaining()){//buffer里面有数据
			//System.out.println("=="+decode(outputBuffer));
			socketChannel.write(outputBuffer);
		}
		ByteBuffer temp=encode(outputData);
		buffer.position(temp.limit());//设置buffer的位置：temp的极限
		buffer.compact();//删除已经处理的字符串(删除缓冲区内从0到当前位置position的内容)
		//一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。
		//clear()方法会清空整个缓冲区。
		//compact()方法只会清除已经读过的数据，任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
		if(outputData.length()==0){
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
		ByteBuffer buffer=(ByteBuffer) key.attachment();//获取与当前通道一起使用的buffer
		SocketChannel socketChannel=(SocketChannel) key.channel();//获取key创建的通道
		ByteBuffer readBuffer=ByteBuffer.allocate(6000);//创建自定义内存的buffer(存放读到的数据)
		socketChannel.read(readBuffer);//将channel数据放入到readBuffer
		readBuffer.flip();//limit=position,position=0 将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入到buffer的所有数据，从position开始读，读到limit。
		buffer.limit(buffer.capacity());//设置buffer的极限为buffer的容量
		buffer.put(readBuffer);//复制到缓存区
	}
	public String decode(ByteBuffer buffer){//解码
		CharBuffer charBuffer=Charset.forName("GBK").decode(buffer);
		return charBuffer.toString();
		
	}
	public ByteBuffer encode(String str){//编码
		return Charset.forName("GBK").encode(str);
	}
	public static void main(String[] args) throws IOException {
		new NoBlockServer().service();
	}
}
