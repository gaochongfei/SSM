package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;


//4002 文件下载报文格式
public class C2S4002 extends C2SMsg {

	public C2S4002() {
		super(4002);
	}

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(cFileInfo.orgName,buffer);
		writeUTF8(cFileInfo.orgCode,buffer);
		writeUTF8(cFileInfo.accountDate,buffer);
		writeUTF8(cFileInfo.reserveDomain4002,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="orgName:"+cFileInfo.orgName+", ";
		toStr+="orgCode:"+cFileInfo.orgCode+", ";
		toStr+="accountDate:"+cFileInfo.accountDate+", ";
		toStr+="reserveDomain:"+cFileInfo.reserveDomain4002+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}