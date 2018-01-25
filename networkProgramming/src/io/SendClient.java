package io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SendClient {

    private static final String ENCODING = "GBK";

    public static String send(String ip, int port, String sendStr, int timeout) {
        long start = System.currentTimeMillis();
        System.out.println(sendStr.length());
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
                res = new String(b, 0, i);
                System.out.println(res);
                break;
            }
            long end = System.currentTimeMillis();


            return res;
        } catch (Exception e) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("error send message").append(e.getMessage()).append("&errorID=")
            .append(System.currentTimeMillis());
            return strBuilder.toString();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append("error send message").append(e.getMessage()).append("&errorID=")
                    .append(System.currentTimeMillis());
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append("error send message").append(e.getMessage()).append("&errorID=")
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
    
    public static void main(String[] args) {
    	String msg="";
        msg="FFFF76623634010100102700170103IBSS017555\n";
		String x=SendClient.send("127.0.0.1", 10003, msg, 3500);
    	System.out.println("return string:"+x);
	}

}