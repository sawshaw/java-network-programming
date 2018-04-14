package framework.netty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ThreadTest{
	class SendThread implements Runnable {
		public SendThread() {
		}

		public void run() {
			String f="002222222222222ffffffffff00000011111110000000000000000000000000000000";
			f="1111111111111111111275111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111A1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111B1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111C11111111111111111111111111111111111111111111111111111111111111111111111111111D";
			long start=System.currentTimeMillis();
			System.out.println("length:"+f.length());
			String d=new ThreadTest().send("127.0.0.1", 1111, f, 3500);
		    System.out.println("result:"+d+"\n length:"+d.length());
			long end=System.currentTimeMillis();
			System.out.println("COST:"+(end-start));
		}
	}
	 public String send(String ip, int port, String sendStr, int timeout) {
		    String ENCODING = "GBK";
	        if (sendStr == null || "".equals(sendStr)) {
	            return "str is null";
	        }
	        Socket client = null;
	        OutputStream stream = null;
	        InputStream is = null;

	        try {
	            client = new Socket();
	            InetSocketAddress address = new InetSocketAddress(ip, port);
	            client.connect(address);

	            timeout = timeout >= 0 ? timeout : 3500;
	            client.setSoTimeout(timeout);
	            stream = client.getOutputStream();
	            is = client.getInputStream();

	            int len = 0;

	            len = sendStr.getBytes(ENCODING).length;
	            ByteBuffer buf = ByteBuffer.allocate(len);
	            byte[] bytes = sendStr.getBytes(ENCODING);
	            buf.put(bytes);
	            stream.write(buf.array(), 0, len);
	            stream.flush();
	            String res = "";
	            int i = 0;
	            byte[] b = new byte[6555];
	            while ((i = is.read(b)) != -1) {
	                res = new String(b, 0, i,"GBK");
	                break;
	            }
	            return res;
	        } catch (Exception e) {
	            StringBuilder strBuilder = new StringBuilder();
	            strBuilder.append("error send message1").append(e.getMessage()).append("&errorID=")
	            .append(System.currentTimeMillis());
	            return strBuilder.toString();
	        } finally {
	            if (client != null) {
	                try {
	                    client.close();
	                } catch (IOException e) {
	                    StringBuilder strBuilder = new StringBuilder();
	                    strBuilder.append("error send message2").append(e.getMessage()).append("&errorID=")
	                    .append(System.currentTimeMillis());
	                }
	            }

	            if (stream != null) {
	                try {
	                    stream.close();
	                } catch (IOException e) {
	                    StringBuilder strBuilder = new StringBuilder();
	                    strBuilder.append("error send message3").append(e.getMessage()).append("&errorID=")
	                    .append(System.currentTimeMillis());
	                }
	            }
	            if (is != null) {
	                try {
	                    is.close();
	                } catch (IOException e) {
	                    StringBuilder strBuilder = new StringBuilder();
	                    strBuilder.append("error send message").append(e.getMessage()).append("&errorID=")
	                    .append(System.currentTimeMillis());
	                }
	            }
	        }
	    }
	 public void start(){
		 new Thread(new SendThread()).start();
	 }
	 public static void main(String[] args) {
		new ThreadTest().start();
	}

}
