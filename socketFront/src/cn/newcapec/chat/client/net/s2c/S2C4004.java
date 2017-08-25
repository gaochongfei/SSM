package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2C4004Handler;
import cn.newcapec.chat.client.net.info.CFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import cn.newcapec.chat.client.net.handler.S2CHandler;

import java.io.UnsupportedEncodingException;

//4004
public class S2C4004 extends S2CMsg {

	public static S2C4004Handler handler = new S2C4004Handler();

	public S2C4004() {
		super(4004);
	}

	public S2C4004 clone() {
		return new S2C4004();
	}
	
	public S2CHandler getHandler() {
		return handler;
	}

	public void read(ChannelBuffer buffer) throws Exception {
		cFileInfo = new CFileInfo();
		cFileInfo.flag = readUTF8(buffer,1);
		byte[] dst = new byte[len-36-1];
		buffer.readBytes(dst);
		cFileInfo.dataBlock = dst;
	}
	
	public String toString() {
		String toStr="";
		toStr+="flag:"+cFileInfo.flag+", ";
		toStr+="dataBlock:"+cFileInfo.dataBlock+", ";
		if(toStr.lastIndexOf(",") != -1){
			toStr = toStr.substring(0, toStr.lastIndexOf(","));
		}
		return toStr;
	}
}

