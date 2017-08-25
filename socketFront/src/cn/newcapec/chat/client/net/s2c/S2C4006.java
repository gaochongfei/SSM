package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2C4006Handler;
import cn.newcapec.chat.client.net.info.CFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import cn.newcapec.chat.client.net.handler.S2CHandler;

//4006
public class S2C4006 extends S2CMsg {

	public static S2C4006Handler handler = new S2C4006Handler();

	public S2C4006() {
		super(4006);
	}

	public S2C4006 clone() {
		return new S2C4006();
	}
	
	public S2CHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		cFileInfo = new CFileInfo();
		cFileInfo.fileCount = readUTF8(buffer,4);
		cFileInfo.resultCode4006 = readUTF8(buffer,2);
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

