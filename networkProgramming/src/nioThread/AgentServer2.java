package nioThread;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eshore.ismp.common.util.EcrmOrderSplitUtil;
import com.eshore.ismp.common.util.MbossReceipt;
import com.eshore.ismp.common.util.ReceiptResult;
import com.eshore.ismp.hbinterface.service.BizCommonService;

/**
 * @author mercy
 *接收ICRM工单和接收业务平台数据回单
 */
public class AgentServer2{
	private static final Logger logger = LoggerFactory.getLogger(AgentServer2.class);  
	private Selector selector=null;
	private ServerSocketChannel serverSocketChannel=null;
	private int port=10001;
	//private Charset charset=Charset.forName("GBK");//返回一个字符类型对象
	public AgentServer2() throws IOException{
		selector=Selector.open();//创建Selector对象
		serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.configureBlocking(false);//设置无阻塞模式
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		logger.info("服务已启动...");
	}
	
	public void service(BizCommonService bizCommonService) throws IOException{
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//用给定的选择器注册channel，并返回一个key
		while(selector.select()>0){
			Set readyKeys=selector.selectedKeys();
			Iterator it=readyKeys.iterator();
			while(it.hasNext()){
				SelectionKey key=null;
				try{
					key=(SelectionKey)it.next();
					it.remove();//删除集合中的key
					if(key.isAcceptable()){//是否可以接收客户端的socket连接
						ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
						SocketChannel socketChannel=ssc.accept();
						//logger.info("client from："+socketChannel.socket().getInetAddress()+":"+socketChannel.socket().getPort());
						socketChannel.configureBlocking(false);//设置无阻塞模式
						ByteBuffer buffer=ByteBuffer.allocate(6000);//创建一个ByteBuffer对象用于存放数据(数据存放缓冲区)
						socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE,buffer);//注册事件,Selector会监控事件是否发生
					}
					if(key.isReadable()){//key的channel是否可读
						receive(key);
					}
					if(key.isWritable()){//key的channel是否可写
						send(key,bizCommonService);
					}
				}catch(IOException e){  
					logger.info("agent test exception....");
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
	public void send(SelectionKey key,BizCommonService bizCommonService) throws IOException{
		ByteBuffer buffer=(ByteBuffer) key.attachment();
		SocketChannel socketChannel=(SocketChannel) key.channel();
		buffer.flip();
		String data=decode(buffer);//解码客户端发过来的数据
		if(data.length()==0){
			return ;
		}
		String outputData=data;//.substring(0, data.indexOf("\n")+1);
		logger.info("receive data:"+outputData);
		//String reply="FFFF02141433570200012400050301IBSS01662       001023CS0214143357*0189086510002001100301420170214143500004003099005007success";
		//ByteBuffer outputBuffer=encode("echo:"+reply);//返回给客户端的数据
		String res =null;
		res = createResponseStr(String.valueOf(outputData));
		logger.info("res:{}",res);
		ByteBuffer outputBuffer=encode(res);//返回给客户端的数据
		while(outputBuffer.hasRemaining()){
			//System.out.println("=="+decode(outputBuffer));
			socketChannel.write(outputBuffer);
		}
		ByteBuffer temp=encode(outputData);
		buffer.position(temp.limit());
		buffer.compact();
		if(outputData.length()==0){
			key.cancel();
			socketChannel.close();
			logger.info("关闭与某客户端的连接");
		}
	}
	public static final String MBOSS_PLAN_TYPE = "000";
	public static final String HB_SYSTEM_ID ="03";
	public static final String ICRM_SYSTEM_ID ="04";
	public static final String CRM_SYSTEM_ID ="01";
	public static final String SIZE_VALUE="5";
	public  String createResponseStr(String msg){
		ReceiptResult re = null;
    	try{
			EcrmOrderSplitUtil crmOrder= new EcrmOrderSplitUtil();
			crmOrder.setSrcString(msg.replaceAll("\n", ""));
			//crmOrder.splitOrderString();
			
			String orderType = crmOrder.getValue(MBOSS_PLAN_TYPE);
			
			if(orderType!=null && "30".equals(orderType)){
				re = MbossReceipt.getReturnByteBuffer(msg,MbossReceipt.PLAN_RECEIVED,HB_SYSTEM_ID,ICRM_SYSTEM_ID,
						 MbossReceipt.RECEIVED_OK, "success");
			}else{
				re = MbossReceipt.getReturnByteBuffer(msg,MbossReceipt.PLAN_RECEIVED, HB_SYSTEM_ID,CRM_SYSTEM_ID,
						MbossReceipt.RECEIVED_OK, "success");
			}
    	}catch(Exception e){
    		logger.error("解析报文出错！",e);
    		re = MbossReceipt.getReturnByteBuffer(msg,MbossReceipt.PLAN_RECEIVED, HB_SYSTEM_ID,CRM_SYSTEM_ID,
					"101", "fail");
    	}
		return re.getResult();
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
		ByteBuffer readBuffer=ByteBuffer.allocate(6000);//创建自定义内存的buffer(存放读到的数据)
		socketChannel.read(readBuffer);
		readBuffer.flip();
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
		final Logger log = LoggerFactory.getLogger(AgentServer2.class);  
		try{
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { "applicationContext.xml" });
			context.start();
			BizCommonService bizCommonService = (BizCommonService) context.getBean("bizCommonService");
			new AgentServer2().service(bizCommonService);  
		}catch(Exception e){
			log.error("start agent interface server error:",e);
			System.exit(-1);
		}
	}
}

