package framework.mina;

public class MinaTest {
	public static void main(String[] args) {
		MinaClient c=new MinaClient();
		c.connect();
		c.sendMsg2Server("-----00009999-----");
	}

}
