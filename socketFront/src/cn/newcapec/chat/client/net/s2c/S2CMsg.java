package cn.newcapec.chat.client.net.s2c;

import cn.newcapec.chat.client.net.handler.S2CHandler;
import cn.newcapec.chat.client.net.info.CFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.MessageEvent;

import java.io.UnsupportedEncodingException;

/**
 * 服务器发给客户端的数据包
 *
 * @author WEIXING
 * @date 2014-8-24
 */
public abstract class S2CMsg {

	public String syn;// 同步信息
	public int cutFlag;// 压缩标志
	public int enFlag;// 加密标志
	public String mac;// MAC
	public String ver;//版本号
	public int msgid;//消息类型
	public int len;//长度

	public CFileInfo cFileInfo;

	public MessageEvent e;

	public S2CMsg(int _msgid) {
		msgid = _msgid;
	}

	public abstract S2CHandler getHandler();

	public abstract void read(ChannelBuffer buffer) throws Exception;

	public abstract S2CMsg clone();

	public String readUTF8(ChannelBuffer buffer,int len)
			throws UnsupportedEncodingException {
		byte[] dst = new byte[len];
		buffer.readBytes(dst);
		return new String(dst, "GB2312");
	}

	public String toString() {
		return "";
	}

}
