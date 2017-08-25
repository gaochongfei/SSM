package cn.newcapec.chat.server.net.c2s;

import cn.newcapec.chat.server.net.handler.C2SHandler;
import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.MessageEvent;

import java.io.UnsupportedEncodingException;

/**
 * 客户端发给服务器的数据包
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public abstract class C2SMsg implements Cloneable {

	public int msgid;
	public String syn;// 同步信息
	public int cutFlag;// 压缩标志
	public int enFlag;// 加密标志
	public String mac;// MAC
	public String ver;//版本号
	public int len ;//长度
	public int premsgid;//上一个消息类型

	public SFileInfo sFileInfo;

	public MessageEvent e;

	public C2SMsg(int _msgid) {
		msgid = _msgid;
	}

	public abstract C2SHandler getHandler();

	public abstract void read(ChannelBuffer buffer) throws Exception;

	public abstract C2SMsg clone();

	public String readUTF8(ChannelBuffer buffer,int len)
			throws UnsupportedEncodingException {
		byte[] dst = new byte[len];
		buffer.readBytes(dst);
		return new String(dst, "GB2312");
	}

	public String toString(){
		return "";
	}

}
