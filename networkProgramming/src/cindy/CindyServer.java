package cindy;

import net.sf.cindy.Message;
import net.sf.cindy.Session;
import net.sf.cindy.SessionAdapter;
import net.sf.cindy.impl.AutoCloseEventGenerator;
import net.sf.cindy.impl.SimpleServerSocketSession;
//参考 http://blog.csdn.net/kjfcpua/article/details/4890540
public class CindyServer {
	public static void main(String[] args) {
	//建立一个普通的TCP服务   
	SimpleServerSocketSession session = new  SimpleServerSocketSession();
	//设置事件产生器，如果和上面一段程序一起运行，应该共享同一个事件产生器以提高效率   
	session.setEventGenerator(new  AutoCloseEventGenerator());
	//设置要监听的端口   
	session.setListenPort(10003); 
	//添加连接上SocketSession的事件监听器   
	session.addSocketSessionListener(new  SessionHandler());
	//开始服务   
	session.start(true ) ; 
	System.out.println("服务已经启动");
	}
}
