package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.handler.C2S4004Handler;

import java.io.UnsupportedEncodingException;

//4004
public class C2S4004 extends C2SMsg {

	public static C2S4004Handler handler = new C2S4004Handler();

	public C2S4004() {
		super(4004);
	}

	public C2SMsg clone() {
		return new C2S4004();
	}
	
	public C2SHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		sFileInfo = new SFileInfo();
		sFileInfo.flag = readUTF8(buffer,1);
		byte[] dst = new byte[len-36-1];
		buffer.readBytes(dst);
		sFileInfo.dataBlock = dst;
	}
	
	public String toString() {
		String toStr="";
		toStr+="flag:"+sFileInfo.flag+", ";
		toStr+="dataBlock:"+sFileInfo.dataBlock+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}
