package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;


//4003 文件信息通知报文格式
public class C2S4003 extends C2SMsg {

	public C2S4003() {
		super(4003);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.fileName,buffer);
		writeUTF8(cFileInfo.fileMark,buffer);
		writeUTF8(cFileInfo.fileSize,buffer);
		writeUTF8(cFileInfo.reserveDomain4003,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileName:"+cFileInfo.fileName+", ";
		toStr+="fileMark:"+cFileInfo.fileMark+", ";
		toStr+="fileSize:"+cFileInfo.fileSize+", ";
		toStr+="reserveDomain:"+cFileInfo.reserveDomain4003+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}