package cn.newcapec.chat.server.net.s2c;

import org.jboss.netty.buffer.ChannelBuffer;


//4006
public class S2C4006 extends S2CMsg {

	public S2C4006() {
		super(4006);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(sFileInfo.fileCount,buffer);
		writeUTF8(sFileInfo.resultCode4006,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileCount:"+sFileInfo.fileCount+", ";
		toStr+="resultCode:"+sFileInfo.resultCode4006+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

