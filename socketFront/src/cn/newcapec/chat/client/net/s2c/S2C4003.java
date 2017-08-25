package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2C4003Handler;
import cn.newcapec.chat.client.net.info.CFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import cn.newcapec.chat.client.net.handler.S2CHandler;


//4003
public class S2C4003 extends S2CMsg {

	public static S2C4003Handler handler = new S2C4003Handler();

	public S2C4003() {
		super(4003);
	}

	public S2C4003 clone() {
		return new S2C4003();
	}
	
	public S2CHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		cFileInfo = new CFileInfo();
		cFileInfo.fileName = readUTF8(buffer,50);
		cFileInfo.fileMark = readUTF8(buffer,256);
		cFileInfo.fileSize = readUTF8(buffer,10);
		cFileInfo.reserveDomain4003 = readUTF8(buffer,256);
	}
	
	public String toString() {
		String toStr="";
		toStr+="fileName:"+cFileInfo.fileName+", ";
		toStr+="fileMark:"+cFileInfo.fileMark+", ";
		toStr+="fileSize:"+cFileInfo.fileSize+", ";
		toStr+="reserveDomain:"+cFileInfo.reserveDomain4003+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

