package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4002Handler;

//4002
public class C2S4002 extends C2SMsg {

	public static C2S4002Handler handler = new C2S4002Handler();

	public C2S4002() {
		super(4002);
	}

	public C2SMsg clone() {
		return new C2S4002();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.orgName = readUTF8(buffer,40);
		sFileInfo.orgCode = readUTF8(buffer,11);
		sFileInfo.accountDate = readUTF8(buffer,8);
		sFileInfo.reserveDomain4002 = readUTF8(buffer,256);
	}
	
	public String toString() {
		String toStr="";
		toStr+="orgName:"+sFileInfo.orgName+", ";
		toStr+="orgCode:"+sFileInfo.orgCode+", ";
		toStr+="accountDate:"+sFileInfo.accountDate+", ";
		toStr+="reserveDomain:"+sFileInfo.reserveDomain4002+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}
