package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2STestHandler;
import org.jboss.netty.buffer.ChannelBuffer;

//4001
public class C2STest extends C2SMsg {

	public static C2STestHandler handler = new C2STestHandler();

	public C2STest() {
		super(5001);
	}

	public C2SMsg clone() {
		return new C2STest();
	}
	
	public C2SHandler getHandler() {
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
