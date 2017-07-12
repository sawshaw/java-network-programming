package test;

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
			int i=10;
			while (i<12) {
				StringBuilder b=new StringBuilder("");
				b.append("FFFF76623634010100102700170103IBSS017555      0000000218001000234022872488");
				i++;
				System.out.println("i:"+i);
				b.append(i);
			b.append("*766236340100200001178400003001785000030217860000302110000004075510100020SZ2000000054121442461020001241324186148310300593PM_DJDHHM||83456517||001#$PM_HYLX||0||001#$BA_MSMAN||海豚||001#$PM_DJQYYB||518000||001#$PM_DJQYMC||深圳市福田区人力资源服务中心||001#$PM_BHHM||83456517||001#$PM_DJQYDZ||福田区福强路深圳文化创意园世纪工艺品文化广场309栋B座1-3层||001#$PM_SFZDXY||XY02||001#$PM_DJKHXX||||001#$BA_MSDEPTNAME||12||001#$PM_DLS||DSL6||001#$PM_YWSLLB||SLLB01||001#$PM_SLDYSLSH||0||001#$PM_JFQ||01||001#$PM_DJHMGS||1||001#$PM_SRFJ||2||001#$PM_JFJG||1||001#$PM_YZ||30||001#$PM_DXFSSL||100||001#$PB_BILLINGTYPE||000000||005#$PB_USERTYPE||100002||005#$PB_USERCHAR||JFSX01||005#$BEGIN_DATE||20170607||005#$END_DATE||||005#$10400014DXMP214688722910700016号百信息服务中心10800010122810070411400006徐冬生115000088291816511600110114+企业名片行业版包月套餐,114+短信名片包月套餐_定价计划,114+企业名片行业版包月套餐赠送3个月套餐外等额话费优惠11700017755KH000293285120\r\n");
			System.out.println("Send:"+b.toString());
			String x=new ThreadTest().send("127.0.0.1", 10002, b.toString(), 3500);
			System.out.println("result:"+x);
			}
		}
	}
	 public String send(String ip, int port, String sendStr, int timeout) {
		    String ENCODING = "GBK";
	        long start = System.currentTimeMillis();
	        System.out.println("character's length:"+sendStr.length());
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
	 public void start(){
		 new Thread(new SendThread()).start();
	 }
	 public static void main(String[] args) {
		new ThreadTest().start();
	}

}
