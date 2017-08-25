package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;


//4008 应答报文格式
public class C2S4008 extends C2SMsg {

	public C2S4008() {
		super(4008);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.resultCode4008,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="resultCode:"+cFileInfo.resultCode4008+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}