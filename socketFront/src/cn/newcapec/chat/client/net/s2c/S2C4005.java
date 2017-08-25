package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2C4005Handler;
import cn.newcapec.chat.client.net.info.CFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import cn.newcapec.chat.client.net.handler.S2CHandler;

//4005
public class S2C4005 extends S2CMsg {

	public static S2C4005Handler handler = new S2C4005Handler();

	public S2C4005() {
		super(4005);
	}

	public S2C4005 clone() {
		return new S2C4005();
	}
	
	public S2CHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		cFileInfo = new CFileInfo();
		cFileInfo.fileSize = readUTF8(buffer,10);
		cFileInfo.resultCode4005 = readUTF8(buffer,2);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileSize:"+cFileInfo.fileSize+", ";
		toStr+="resultCode:"+cFileInfo.resultCode4005+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

