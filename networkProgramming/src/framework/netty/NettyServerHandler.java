package framework.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);  
    public NettyServerHandler(){}
    @Override  
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  
        logger.info("SERVER接收到消息msg:{}",msg);  
        long start = System.currentTimeMillis();
		 
		long end = System.currentTimeMillis();
        String res="hi,i'am server";
        ctx.channel().writeAndFlush(res);  
      //  ctx.close();
    }  
      
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx,  
            Throwable cause) throws Exception {  
        logger.warn("Unexpected exception from downstream.", cause);  
        ctx.close();  
    }

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("client closed:"+ctx.channel().hashCode());
		super.channelInactive(ctx);
	}  
    
    
}	
