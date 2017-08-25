package cn.newcapec.chat.server.net.info;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.UnsupportedEncodingException;

public class INetInfo {

	public void read(ChannelBuffer buffer) throws Exception {
		read(buffer, true);
	}

	public void read(ChannelBuffer buffer, boolean read_super) throws Exception {

	}

	public void write(ChannelBuffer buffer) throws Exception {
		write(buffer, true);
	}

	public void write(ChannelBuffer buffer, boolean write_super)
			throws Exception {
	}

	public void writeUTF8(String s, ChannelBuffer buffer)
			throws UnsupportedEncodingException {
		if (s == null) {
			return;
		}
		byte[] src = s.getBytes("UTF-8");
		buffer.writeBytes(src);
	}

	public String readUTF8(ChannelBuffer buffer,int len)
			throws UnsupportedEncodingException {
		byte[] dst = new byte[len];
		buffer.readBytes(dst);
		return new String(dst, "UTF-8");
	}
}
