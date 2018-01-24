package framework.mina;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MinaServerHandler extends IoHandlerAdapter {
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
	    throws Exception {
	   cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
	    throws Exception {
		String string=message.toString();
        if (string.trim().equalsIgnoreCase("quit")) {
            session.close(true);
            return;
        }
        System.out.println("recevied message:"+string);
        String reply=" hi, i am server";
        session.write(reply);
        System.out.println("message have been written");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
	    throws Exception {
	   System.out.println("IDLE " + session.getIdleCount(status));
	}
	}