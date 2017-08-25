package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.UnsupportedEncodingException;


//4004 数据报文格式
public class C2S4004 extends C2SMsg {

	public C2S4004() {
		super(4004);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.flag,buffer);
		buffer.writeBytes(cFileInfo.dataBlock);
	}
	
	public String toString() {
		String toStr="";
		toStr+="flag:"+cFileInfo.flag+", ";
		toStr+="dataBlock:"+cFileInfo.dataBlock+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}