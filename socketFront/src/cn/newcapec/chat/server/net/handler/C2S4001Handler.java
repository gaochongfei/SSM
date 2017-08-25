package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.base.ChannelManager;
import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.c2s.C2S4001;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.info.SFileInfo;
import cn.newcapec.chat.server.net.s2c.S2C4008;
import cn.newcapec.tools.utils.ServerFileUtil;
import org.jboss.netty.channel.Channel;

public class C2S4001Handler implements C2SHandler {
	
	//4001-->4008
	public void handle(C2SMsg m) throws Exception {
		C2S4001 msg = (C2S4001)m;
		Channel channel = msg.e.getChannel();
		//ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		//if(channelInfo == null){
			ChannelInfo channelInfo = new ChannelInfo();
			ChannelManager.getInstance().add(channel,channelInfo);
		//}
		SFileInfo fileInfo = channelInfo.getFileInfo();
		if(fileInfo == null){
			fileInfo = new SFileInfo();
		}
		fileInfo.orgName = msg.sFileInfo.orgName;
		fileInfo.orgCode = msg.sFileInfo.orgCode;
		fileInfo.reserveDomain4001 = msg.sFileInfo.reserveDomain4001;
		if(4001 != msg.msgid){
			fileInfo.resultCode4008 = "02";//无效的消息类型码
		}else if(!"01".equals(msg.ver)){
			fileInfo.resultCode4008 = "03";//无效的消息版本
		}else if(!ServerFileUtil.ifBodyLen(msg.len,msg)){
			fileInfo.resultCode4008 = "04";//无效的报文长度
		}else{
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
