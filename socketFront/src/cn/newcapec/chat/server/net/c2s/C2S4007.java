package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4007Handler;

//4007
public class C2S4007 extends C2SMsg {

	public static C2S4007Handler handler = new C2S4007Handler();

	public C2S4007() {
		super(4007);
	}

	public C2SMsg clone() {
		return new C2S4007();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.endFlag = readUTF8(buffer,8);
	}
	
	public String toString() {
		String toStr="";
		toStr+="endFlag:"+sFileInfo.endFlag+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}
