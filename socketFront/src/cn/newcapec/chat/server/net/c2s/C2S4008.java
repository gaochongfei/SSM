package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4008Handler;

//4008
public class C2S4008 extends C2SMsg {

	public static C2S4008Handler handler = new C2S4008Handler();

	public C2S4008() {
		super(4008);
	}

	public C2SMsg clone() {
		return new C2S4008();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.resultCode4008 = readUTF8(buffer,2);
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
