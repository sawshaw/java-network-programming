package framework.mina;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MinaServerHandler1 extends IoHandlerAdapter {
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
	    throws Exception {
	   cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
	    throws Exception {
		IoBuffer buffer=(IoBuffer)message;  
        ByteBuffer bf= buffer.buf();  
        byte[] tempBuffer=new byte[bf.limit()];  
        bf.get(tempBuffer);  
        //GBK字符转换
        String str=new String(tempBuffer,"GBK");  
        System.out.println("receive:"+str+"\nreceive's length:"+str.length());  
        String reply="hi, i am server你好";
        //session.write(reply);
        ByteArrayOutputStream outputPacket = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputPacket);
        byte[] bytes = reply.getBytes("GBK");
        int len= reply.getBytes("GBK").length;
        System.out.println("reply's length:"+len);
        dos.write(bytes,0,len);
        //dos.writeUTF(reply);
        dos.flush();
        System.out.println(outputPacket.toByteArray().length);
        IoBuffer b = IoBuffer.allocate(6000);
        System.out.println("length:"+outputPacket.toByteArray().length);
        System.out.println(b.limit());
        b.put(outputPacket.toByteArray());
        b.flip();
        System.out.println(b.limit());
        session.write(b); 
	}
	public  ByteBuffer getByteBuffer(String str) {

		return ByteBuffer.wrap(str.getBytes());

		}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
	    throws Exception {
	   System.out.println("IDLE " + session.getIdleCount(status));
	}
	}