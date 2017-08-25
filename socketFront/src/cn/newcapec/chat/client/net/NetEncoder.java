package cn.newcapec.chat.client.net;

import cn.newcapec.chat.client.net.c2s.C2SMsg;
import cn.newcapec.tools.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.io.UnsupportedEncodingException;

/**
 * 编码器
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetEncoder extends OneToOneEncoder {

	private static Log log = LogFactory.getLog("client");

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object obj) throws Exception {

		C2SMsg msg = (C2SMsg) obj;
//		ChannelBuffer b = ChannelBuffers.dynamicBuffer();
//		msg.write(b);
//		byte[] source = b.array();
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		if(msg.msgid == 4004){
			msg.len = 36 + msg.cFileInfo.flag.length() + msg.cFileInfo.dataBlock.length;
		}else{
			msg.len = 36 + StringUtil.toValueLen(msg.toString());
		}
		writeUTF8(StringUtil.leftAddSpace(String.valueOf(msg.len),4,"0"),buffer);
		writeUTF8(msg.syn,buffer);
		writeUTF8(msg.cutFlag,buffer);
		writeUTF8(msg.enFlag,buffer);
		writeUTF8(msg.ver,buffer);
		writeUTF8(msg.msgid,buffer);

//		buffer.writeBytes(source);
		msg.write(buffer);

		writeUTF8(msg.mac,buffer);

		log.info("发送:{ " + msg.getClass().getSimpleName() + " " + "msgid:" + msg.msgid +", "+ "msgfile:" + msg.cFileInfo.fileName +", "+
					"len:"+msg.len+", "+"syn:"+msg.syn+", "+"cutFlag:"+msg.cutFlag+", "+"enFlag:"+msg.enFlag+", "+"ver:"+msg.ver+", "
				 + msg.toString() +", "+"mac:"+msg.mac+"}");

		return buffer;
	}

	public void writeUTF8(Object s, ChannelBuffer buffer)
			throws UnsupportedEncodingException {
		if (s == null) {
			return;
		}
		byte[] src = s.toString().getBytes("GB2312");
		buffer.writeBytes(src);
	}

}
