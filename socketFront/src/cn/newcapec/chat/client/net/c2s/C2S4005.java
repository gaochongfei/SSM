package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;


//4005 断点通知报文格式
public class C2S4005 extends C2SMsg {

	public C2S4005() {
		super(4005);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.fileSize,buffer);
		writeUTF8(cFileInfo.resultCode4005,buffer);
		writeUTF8(mac,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileSize:"+cFileInfo.fileSize+", ";
		toStr+="resultCode:"+cFileInfo.resultCode4005+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}