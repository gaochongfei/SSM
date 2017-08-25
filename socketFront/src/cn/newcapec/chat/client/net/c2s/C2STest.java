package cn.newcapec.chat.client.net.c2s;

import org.jboss.netty.buffer.ChannelBuffer;

//4001 文件请求报文格式
public class C2STest extends C2SMsg {

	public C2STest() {
		super(5001);
	}

	public String msg = "";//请求机构简称

	public void write(ChannelBuffer buffer) throws Exception {
		writeUTF8(msg,buffer);
	}
	
	public String toString() {
		String toStr="";
		toStr+="msg:"+msg+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}