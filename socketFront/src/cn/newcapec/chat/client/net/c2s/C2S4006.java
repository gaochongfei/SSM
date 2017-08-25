package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;


//4006 文件数通知报文格式
public class C2S4006 extends C2SMsg {

	public C2S4006() {
		super(4006);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.fileCount,buffer);
		writeUTF8(cFileInfo.resultCode4006,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileCount:"+cFileInfo.fileCount+", ";
		toStr+="resultCode:"+cFileInfo.resultCode4006+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}