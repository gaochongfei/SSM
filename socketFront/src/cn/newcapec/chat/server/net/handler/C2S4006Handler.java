package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.base.ChannelManager;
import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.c2s.C2S4006;
import cn.newcapec.chat.server.net.info.SFileInfo;
import cn.newcapec.chat.server.net.s2c.S2C4008;
import cn.newcapec.tools.utils.ServerFileUtil;
import org.jboss.netty.channel.Channel;

public class C2S4006Handler implements C2SHandler {
	
	//4006
	public void handle(C2SMsg m) throws Exception {
		C2S4006 msg = (C2S4006)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		SFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.fileCount = msg.sFileInfo.fileCount;//总共的文件个数
		fileInfo.resultCode4006 = msg.sFileInfo.resultCode4006;
		fileInfo.restFileCount = Integer.parseInt(fileInfo.fileCount);//剩余的文件个数
		if(4006 != msg.msgid){
			fileInfo.resultCode4008 = "02";//无效的消息类型码
		}else if(!"01".equals(msg.ver)){
			fileInfo.resultCode4008 = "03";//无效的消息版本
		}else if(!ServerFileUtil.ifBodyLen(msg.len,msg)) {
			fileInfo.resultCode4008 = "04";//无效的报文长度
		}else {
			fileInfo.resultCode4008 = "00";
		}
		channelInfo.setFileInfo(fileInfo);
		S2C4008 s2c4008 = new S2C4008();
		s2c4008.sFileInfo = fileInfo;
		channel.write(s2c4008);

		if( !"00".equals(fileInfo.resultCode4008)) {//验证不通过，需要关闭连接
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			ChannelManager.getInstance().remove(channel);
		}
	}

}
