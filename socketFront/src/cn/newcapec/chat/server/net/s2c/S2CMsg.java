package cn.newcapec.chat.server.net.s2c;

import cn.newcapec.chat.server.net.info.SFileInfo;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

/**
 * 服务器发给客户端的数据包
 *
 * @author WEIXING
 * @date 2014-8-24
 */
public abstract class S2CMsg {

	public String syn = "000000000000";// 同步信息
	public int cutFlag = 0;// 压缩标志
	public int enFlag = 0;// 加密标志
	public String mac = "0000000000000000";// MAC
	public String ver = "01";// 版本号
	public int msgid; // 消息类型
	public int len;//长度

	public SFileInfo sFileInfo;

	public S2CMsg(int _msgid) {
		msgid = _msgid;
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
