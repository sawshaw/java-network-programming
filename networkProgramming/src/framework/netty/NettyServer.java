package framework.netty;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eshore.ismp.hbinterface.service.BizCommonService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {
	 private static final Logger logger = Logger.getLogger(NettyServer.class);  
	    private static  int PORT = 10003;  
	    /**用于分配处理业务线程的线程组个数 */  
	    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2; //默认  
	    /** 业务出现线程大小*/  
	    protected static final int BIZTHREADSIZE = 4;  
	        /* 
	     * NioEventLoopGroup实际上就是个线程池, 
	     * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件, 
	     * 每一个NioEventLoop负责处理m个Channel, 
	     * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel 
	     */  
	    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);  
	    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);  
	      
	    protected static void run(final BizCommonService bizCommonService) throws Exception { 
	    	//String PORTs=ConfigLoadUtil.getValue("toCrmServerPort");
	    	//PORT=Integer.parseInt(PORTs);
	    	logger.info("PORT IS:"+PORT);
	    	//System.out.println("start main 7......PORT="+PORT);
	        ServerBootstrap b = new ServerBootstrap();  
	        b.group(bossGroup, workerGroup);  
	        b.channel(NioServerSocketChannel.class);  
	        b.childHandler(new ChannelInitializer<SocketChannel>() {  
	            @Override  
	            public void initChannel(SocketChannel ch) throws Exception {  
	                ChannelPipeline pipeline = ch.pipeline();  
	              /*  pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));  
	                pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));  */
	                pipeline.addLast("decoder", new StringDecoder(Charset.forName("GBK")));  
	                pipeline.addLast("encoder", new StringEncoder(Charset.forName("GBK")));
	                pipeline.addLast(new NettyServerHandler(bizCommonService));  
	            }  
	        });  
	        b.bind(PORT).sync();  
	        logger.info("TCP服务器已启动");  
	    }  
	      
	    protected static void shutdown() {  
	        workerGroup.shutdownGracefully();  
	        bossGroup.shutdownGracefully();  
	    }  
	  
	    public static void main(String[] args) throws Exception {  
	    	try{
	    		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
	    				new String[] { "applicationContext.xml" });
	    		BizCommonService bizCommonService = (BizCommonService) context.getBean("bizCommonService");
	    		context.start();
	    		NettyServer.run(bizCommonService);   
	    	}catch(Exception e){
	    		logger.error("start crm interface error:",e);
	    		System.exit(-1);
	    	}
	    }  
}
