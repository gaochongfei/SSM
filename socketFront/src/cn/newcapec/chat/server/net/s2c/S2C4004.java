package cn.newcapec.chat.server.net.s2c;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.UnsupportedEncodingException;


//4004
public class S2C4004 extends S2CMsg {

	public S2C4004() {
		super(4004);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(sFileInfo.flag,buffer);
		buffer.writeBytes(sFileInfo.dataBlock);
	}
	
	public String toString() {
		String toStr="";
		toStr+="flag:"+sFileInfo.flag+", ";
		toStr+="dataBlock:"+sFileInfo.dataBlock+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

