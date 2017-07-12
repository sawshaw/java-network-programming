package server;

import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SpsServer1 {
	 private static final Logger logger = Logger.getLogger(SpsServer1.class);  
	    private static int PORT = 20002;  
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
	    	//String PORTs=ConfigLoadUtil.getValue("toSpsServerPort");
	    	//PORT=Integer.parseInt(PORTs);
	    	//logger.info("PORT IS:"+PORT);
	        ServerBootstrap b = new ServerBootstrap();  
	        b.group(bossGroup, workerGroup);  
	        b.channel(NioServerSocketChannel.class);  
	        b.childHandler(new ChannelInitializer<SocketChannel>() {  
	            @Override  
	            public void initChannel(SocketChannel ch) throws Exception {  
	                ChannelPipeline pipeline = ch.pipeline();  
	               /* pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));  
	                pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));  */
	                pipeline.addLast("decoder", new StringDecoder(Charset.forName("GBK")));  
	                pipeline.addLast("encoder", new StringEncoder(Charset.forName("GBK")));
	                pipeline.addLast(new SpsServerHandler(bizCommonService));  
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
	    		context.start();
	    		BizCommonService bizCommonService = (BizCommonService) context.getBean("bizCommonService");
	    		SpsServer1.run(bizCommonService);  
	    	}catch(Exception e){
	    		logger.error("start sps interface server error:",e);
	    		System.exit(-1);
	    	}
	    }  
}
