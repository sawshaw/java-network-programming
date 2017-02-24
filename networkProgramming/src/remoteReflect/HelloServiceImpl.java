package remoteReflect;

import java.util.Date;

/**
 * @author mercy
 * 
 */
public class HelloServiceImpl implements HelloService{

	@Override
	public String echo(String msg) {
		return "echo:"+msg;
	}

	@Override
	public Date getTime() {
		return new Date();
	}

}
