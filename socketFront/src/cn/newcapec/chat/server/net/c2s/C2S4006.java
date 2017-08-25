package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4006Handler;

//4006
public class C2S4006 extends C2SMsg {

	public static C2S4006Handler handler = new C2S4006Handler();

	public C2S4006() {
		super(4006);
	}

	public C2SMsg clone() {
		return new C2S4006();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.fileCount = readUTF8(buffer,4);
		sFileInfo.resultCode4006 = readUTF8(buffer,2);
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
