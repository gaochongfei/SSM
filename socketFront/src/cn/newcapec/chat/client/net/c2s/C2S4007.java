package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;


//4007 文件传输结果报文格式
public class C2S4007 extends C2SMsg {

	public C2S4007() {
		super(4007);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.endFlag,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="endFlag:"+cFileInfo.endFlag+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}