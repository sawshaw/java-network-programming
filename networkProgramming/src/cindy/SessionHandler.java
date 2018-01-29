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
