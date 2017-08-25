package cn.newcapec.chat.server.net.s2c;

import org.jboss.netty.buffer.ChannelBuffer;


//4003
public class S2CTest extends S2CMsg {

	public S2CTest() {
		super(5002);
	}

	public String msg = "";

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(msg,buffer);
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

