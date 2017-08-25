package cn.newcapec.chat.server.net.s2c;

import org.jboss.netty.buffer.ChannelBuffer;


//4003
public class S2C4003 extends S2CMsg {

	public S2C4003() {
		super(4003);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(sFileInfo.fileName,buffer);
		writeUTF8(sFileInfo.fileMark,buffer);
		writeUTF8(sFileInfo.fileSize,buffer);
		writeUTF8(sFileInfo.reserveDomain4003,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileName:"+sFileInfo.fileName+", ";
		toStr+="fileMark:"+sFileInfo.fileMark+", ";
		toStr+="fileSize:"+sFileInfo.fileSize+", ";
		toStr+="reserveDomain:"+sFileInfo.reserveDomain4003+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

