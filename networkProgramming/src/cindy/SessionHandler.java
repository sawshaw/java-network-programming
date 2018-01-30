package cindy;

import java.nio.ByteBuffer;

import net.sf.cindy.Message;
import net.sf.cindy.Session;
import net.sf.cindy.SessionAdapter;
import net.sf.cindy.impl.ByteArrayMessage;

public class SessionHandler extends SessionAdapter{
	public void sessionClosed(Session paramSession) throws Exception {
		 //System.out.println("session closed");
	}

	public void sessionEstablished(Session paramSession) throws Exception {
		 //System.out.println("session established");;  
	}

	public void sessionIdle(Session paramSession) throws Exception {
	}

	public void sessionTimeout(Session paramSession) throws Exception {
	}

	public void messageReceived(Session session, Message message) throws Exception {
		System.out.println("message received:"+message);
		ByteArrayMessage message1 = new  ByteArrayMessage();;  
        message1.setContent(("received message:"+message).getBytes()); 
        session.write(message1);
        //中文乱码问题
       /* ByteBuffer bb=message.toByteBuffer()[0];
		byte[] b = new byte[bb.remaining()];
		bb.get(b, 0, b.length);
	    String str=new String(b,"GBK"); 
		System.out.println("message received:"+str);
		ByteArrayMessage message1 = new  ByteArrayMessage();;  
        message1.setContent(("message received:"+str).getBytes("GBK")); 
        session.write(message1);*/

       // ByteArrayMessage message2 = new  ByteArrayMessage();
        //message2.setContent("message2" .getBytes());  
        //session.blockWrite(message2);  
	}

	public void messageSent(Session session, Message message) throws Exception {
		 //System.out.println("messgae send:"+message);
	}

	public void exceptionCaught(Session paramSession, Throwable paramThrowable) throws Exception {
	}

}
