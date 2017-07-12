package eightUdpSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
 
//此线程类 用于处理接收到连接状态的 客户端的收发处理
//若多个客户端连接则会创建多个实例 
class ServerThread extends Thread {
 
    private Socket m_Client = null;
    private InputStream in = null;
    private OutputStream out = null;
    private BizCommonService bizCommonService;
    private String ENCODING = "GBK";
 
    public ServerThread(Socket _client,BizCommonService bizCommonService) throws Exception {
        this.m_Client = _client;
        this.in = m_Client.getInputStream(); // 获取出入流
        this.out = m_Client.getOutputStream();// 获取输出流
        this.bizCommonService=bizCommonService;
    }
 
    public void run() {
        byte[] recvBuf = new byte[2048];
        byte[] sendBuf = new byte[2048];
        int len = 0;
 
        while (true) {
            // 先判断客户端连接状态
            try {
                m_Client.sendUrgentData(0);
            } catch (IOException e) {
                break;// 失去连接
            }
 
            // 输入流从网络上读取数据
            try {
                len = in.read(recvBuf);
                System.out.println("len:"+len);
                if(len==-1){
                	//没收到数据断开连接
                	break;
                }else{
                	System.out.println("recv:" + new String(recvBuf, 0, len,ENCODING));
                }
            } catch (IOException e) {
                break;// 接收异常
            }
            //收到的数据
            String recv="";
			try {
				recv = new String(recvBuf, 0, len,ENCODING);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
            boolean result = bizCommonService.sendOperToCacheAysn(recv);
        	/**
        	 * step 3 : 创建响应报文
        	 */
    		String res = bizCommonService.createResponseStr(recv,result);
            sendBuf = res.getBytes();
            try {
                out.write(sendBuf);
                System.out.println("send:" + res);
            } catch (IOException e) {
                break;// 发送异常
            }
        }
 
        System.out.println("客户端失去连接");
        try {
            in.close();
            out.close();
            m_Client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
// 此线程类用于接收服务端socket监听到的客户端连接 并创建线程对于此客户端的处理
class ClientRequestProc extends Thread {
 
    private ServerSocket m_Server = null;
    private BizCommonService bizCommonService;
 
    public ClientRequestProc(ServerSocket _server,BizCommonService bizCommonService) {
        m_Server = _server;
       this. bizCommonService=bizCommonService;
    }
 
    public void run() {
        while (true) {
            try {
                Socket client = m_Server.accept();
                System.out.println("有客户端连接");
                try {
                    ServerThread proc = new ServerThread(client,bizCommonService);
                    proc.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("启动客户端处理线程");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class SpsServer {
	public static void main(String[] args) {
		final Logger log = LoggerFactory.getLogger(SpsServer.class);  
		try{
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { "applicationContext.xml" });
			context.start();
			BizCommonService bizCommonService = (BizCommonService) context.getBean("bizCommonService");
			SpsServer.serverStart(bizCommonService);  
		}catch(Exception e){
			log.error("start sps interface server error:",e);
			System.exit(-1);
		}
	}
	public static void serverStart(BizCommonService bizCommonService){
		ServerSocket server = null;
		int PORT=10002;
		//int PORT=30087;//prod
		//String PORTs=(String)CustomizedPropertyConfigurer.getContextProperty("toSpsServerPort");
    	//PORT=Integer.parseInt(PORTs);
	    try {
	        server = new ServerSocket(PORT);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    String ip=server.getInetAddress().getHostAddress();
	    System.out.println(ip+":"+PORT+"服务器开始监听...\n");
	    ClientRequestProc clientRp = new ClientRequestProc(server,bizCommonService);
	    clientRp.start();
	    try {
	        clientRp.join();// 等待子线程退出(阻塞函数 防止主线程先结束)
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
}