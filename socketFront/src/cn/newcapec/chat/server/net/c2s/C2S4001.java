package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4001Handler;

//4001
public class C2S4001 extends C2SMsg {

	public static C2S4001Handler handler = new C2S4001Handler();

	public C2S4001() {
		super(4001);
	}

	public C2SMsg clone() {
		return new C2S4001();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.orgName = readUTF8(buffer,40);
		sFileInfo.orgCode = readUTF8(buffer,11);
		sFileInfo.reserveDomain4001 = readUTF8(buffer,256);
	}
	
	public String toString() {
		String toStr="";
		toStr+="orgName:"+sFileInfo.orgName+", ";
		toStr+="orgCode:"+sFileInfo.orgCode+", ";
		toStr+="reserveDomain:"+sFileInfo.reserveDomain4001+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}
