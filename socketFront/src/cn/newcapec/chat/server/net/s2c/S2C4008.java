package cn.newcapec.chat.server.net.s2c;

import org.jboss.netty.buffer.ChannelBuffer;


//4008
public class S2C4008 extends S2CMsg {

	public S2C4008() {
		super(4008);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(sFileInfo.resultCode4008,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="resultCode:"+sFileInfo.resultCode4008+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}

}

