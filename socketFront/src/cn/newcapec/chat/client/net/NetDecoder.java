package cn.newcapec.chat.client.net;

import cn.newcapec.chat.client.net.s2c.S2CMsg;
import cn.newcapec.tools.utils.StringUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;


/**
 * 解码器
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if(buffer.readableBytes() < 12){
			return null;
		}
		buffer.markReaderIndex();
		int len;
		try {
			len = Integer.parseInt(StringUtil.readUTF8(buffer,4));
		}catch(Exception e){
			return null;
		}
		if(buffer.readableBytes() < len){
			buffer.resetReaderIndex();
			return null;
		}
		if(len < 0 || len > 1024 * 1024){
			closeChannel(channel);
			return null;
		}
		String syn = StringUtil.readUTF8(buffer,12);
		int cutFlag = Integer.parseInt(StringUtil.readUTF8(buffer,1));
		int enFlag = Integer.parseInt(StringUtil.readUTF8(buffer,1));
		String ver = StringUtil.readUTF8(buffer,2);
		int msgid = Integer.parseInt(StringUtil.readUTF8(buffer,4));
		byte[] data = new byte[len-12-1-1-2-4-16];
		buffer.readBytes(data);
		String mac = StringUtil.readUTF8(buffer,16);
		S2CMsg msg = MsgFactory.create(msgid);
		if(msg == null){
			closeChannel(channel);
			return null;
		}
		ChannelBuffer cb = ChannelBuffers.dynamicBuffer();
		cb.writeBytes(data);
		cb.readerIndex(0);
		msg.len = len;
		msg.read(cb);
		msg.syn = syn;
		msg.cutFlag = cutFlag;
		msg.cutFlag = enFlag;
		msg.ver = ver;
		msg.mac = mac;
		return msg;
	}
	
	private void closeChannel(Channel channel){
		channel.setAttachment(null);
		channel.close();
	}

}
