package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2C4008Handler;
import cn.newcapec.chat.client.net.info.CFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import cn.newcapec.chat.client.net.handler.S2CHandler;

//4008
public class S2C4008 extends S2CMsg {

	public static S2C4008Handler handler = new S2C4008Handler();

	public S2C4008() {
		super(4008);
	}

	public S2C4008 clone() {
		return new S2C4008();
	}
	
	public S2CHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		cFileInfo = new CFileInfo();
		cFileInfo.resultCode4008 = readUTF8(buffer,2);
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

