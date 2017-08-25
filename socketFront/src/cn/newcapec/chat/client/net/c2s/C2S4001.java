package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;

//4001 文件请求报文格式
public class C2S4001 extends C2SMsg {

	public C2S4001() {
		super(4001);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.orgName,buffer);
		writeUTF8(cFileInfo.orgCode,buffer);
		writeUTF8(cFileInfo.reserveDomain4001,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="orgName:"+cFileInfo.orgName+", ";
		toStr+="orgCode:"+cFileInfo.orgCode+", ";
		toStr+="reserveDomain:"+cFileInfo.reserveDomain4001+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}