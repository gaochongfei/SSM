package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4003Handler;

//4003
public class C2S4003 extends C2SMsg {

	public static C2S4003Handler handler = new C2S4003Handler();

	public C2S4003() {
		super(4003);
	}

	public C2SMsg clone() {
		return new C2S4003();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.fileName = readUTF8(buffer,50);
		sFileInfo.fileMark = readUTF8(buffer,256);
		sFileInfo.fileSize = readUTF8(buffer,10);
		sFileInfo.reserveDomain4003 = readUTF8(buffer,256);
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
