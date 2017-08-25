package cn.newcapec.chat.server.net.s2c;

import org.jboss.netty.buffer.ChannelBuffer;


//4005
public class S2C4005 extends S2CMsg {

	public S2C4005() {
		super(4005);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(sFileInfo.fileSize,buffer);
		writeUTF8(sFileInfo.resultCode4005,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileSize:"+sFileInfo.fileSize+", ";
		toStr+="resultCode:"+sFileInfo.resultCode4005+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

