package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2C4007Handler;
import cn.newcapec.chat.client.net.info.CFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import cn.newcapec.chat.client.net.handler.S2CHandler;

//4007
public class S2C4007 extends S2CMsg {

	public static S2C4007Handler handler = new S2C4007Handler();

	public S2C4007() {
		super(4007);
	}

	public S2C4007 clone() {
		return new S2C4007();
	}
	
	public S2CHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		cFileInfo = new CFileInfo();
		cFileInfo.endFlag = readUTF8(buffer,8);
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

