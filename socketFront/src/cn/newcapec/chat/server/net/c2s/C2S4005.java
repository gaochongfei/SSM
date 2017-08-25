package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4005Handler;

//4005
public class C2S4005 extends C2SMsg {

	public static C2S4005Handler handler = new C2S4005Handler();

	public C2S4005() {
		super(4005);
	}

	public C2SMsg clone() {
		return new C2S4005();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.fileSize = readUTF8(buffer,10);
		sFileInfo.resultCode4005 = readUTF8(buffer,2);
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
