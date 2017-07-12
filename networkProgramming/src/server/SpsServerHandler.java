package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eshore.ismp.hbinterface.service.BizCommonService;

public class SpsServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(SpsServerHandler.class);  
    private BizCommonService bizCommonService;
    public SpsServerHandler(){}
   
    public SpsServerHandler(BizCommonService bizCommonService){
    	this.bizCommonService=bizCommonService;
    }
    @Override  
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {  
        logger.info("SERVER接收到消息 msg:{}",msg);  
        long start = System.currentTimeMillis();
        /**
         * step 1 : 转换接收信息为对象
         * 改用异步，不需要转换对象
         */
        //EcrmOrder order=bizCommonService.convertRequest(String.valueOf(msg));
        /**
         * step 2 : 将请求对象发送到工单模块
         */
        //boolean result =bizCommonService.sendToOrderBySPS(order);
       // boolean result = bizCommonService.sendOperToOrderAysn(String.valueOf(msg));
        boolean result = bizCommonService.sendOperToCacheAysn(String.valueOf(msg));
    	
    	/**
    	 * step 3 : 创建响应报文
    	 */
		String res = bizCommonService.createResponseStr(String.valueOf(msg),result);
		long end = System.currentTimeMillis();
        logger.debug("SpsServer request:{} res:{} time cost:{}ms",String.valueOf(msg),res,(end-start));
        ctx.channel().writeAndFlush(res);  
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
