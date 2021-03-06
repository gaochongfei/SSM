package cn.newcapec.chat.client.net.c2s;

import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.UnsupportedEncodingException;

/**
 * 客户端发给服务器的数据包
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public abstract class C2SMsg implements Cloneable {

	public String syn = "000000000000";// 同步信息
	public int cutFlag = 0;// 压缩标志
	public int enFlag = 0;// 加密标志
	public String mac = "0000000000000000";// MAC
	public String ver = "01";//版本号
	public int msgid;//消息类型
	public int len;//报文长度

	public CFileInfo cFileInfo;

	public C2SMsg(int _msgid) {
		msgid = _msgid;
	}

	public S2CMsg clone()
	{
		return null;
	}

	public abstract void write(ChannelBuffer buffer) throws Exception;

	public void writeUTF8(Object s, ChannelBuffer buffer)
			throws UnsupportedEncodingException {
		if (s == null) {
			return;
		}
		byte[] src = s.toString().getBytes("GB2312");
		buffer.writeBytes(src);
	}
}
