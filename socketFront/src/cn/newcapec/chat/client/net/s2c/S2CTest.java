package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2CHandler;
import cn.newcapec.chat.client.net.handler.S2CTestHandler;
import org.jboss.netty.buffer.ChannelBuffer;


//4003
public class S2CTest extends S2CMsg {

	public static S2CTestHandler handler = new S2CTestHandler();

	public S2CTest() {
		super(5002);
	}

	public S2CTest clone() {
		return new S2CTest();
	}
	
	public S2CHandler getHandler() {
		return handler;
	}

	public String msg;

	public void read(ChannelBuffer buffer) throws Exception {
		msg = readUTF8(buffer,2);
	}
	
	public String toString() {
		String toStr="";
		toStr+="msg:"+msg+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

