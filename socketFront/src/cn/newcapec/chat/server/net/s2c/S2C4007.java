package cn.newcapec.chat.server.net.s2c;

import org.jboss.netty.buffer.ChannelBuffer;


//4007
public class S2C4007 extends S2CMsg {

	public S2C4007() {
		super(4007);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(sFileInfo.endFlag,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="endFlag:"+sFileInfo.endFlag+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

