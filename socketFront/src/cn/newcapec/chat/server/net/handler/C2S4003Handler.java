package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.base.ChannelManager;
import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.c2s.C2S4003;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.info.SFileInfo;
import cn.newcapec.chat.server.net.s2c.S2C4005;
import cn.newcapec.tools.utils.ServerFileUtil;
import org.jboss.netty.channel.Channel;

public class C2S4003Handler implements C2SHandler {
	
	//4003
	public void handle(C2SMsg m) throws Exception {
		C2S4003 msg = (C2S4003)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		SFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.fileName = msg.sFileInfo.fileName;
		fileInfo.fileMark = msg.sFileInfo.fileMark;
		fileInfo.fileSize = msg.sFileInfo.fileSize;
		fileInfo.reserveDomain4003 = msg.sFileInfo.reserveDomain4003;
		fileInfo.fileSize4005 = "0";
		if(4003 != msg.msgid){
			fileInfo.resultCode4005 = "02";//无效的消息类型码
		}else if(!"01".equals(msg.ver)){
			fileInfo.resultCode4005 = "03";//无效的消息版本
		}else if(!ServerFileUtil.ifBodyLen(msg.len,msg)){
			fileInfo.resultCode4005 = "04";//无效的报文长度
		}else if(fileInfo.fileName.trim().length()!= 33) {//文件名不合法
			fileInfo.resultCode4005 = "07";
		}else{
			fileInfo.fileSize4005 = fileInfo.fileSize;
			fileInfo.resultCode4005 = "00";
		}
		channelInfo.setFileInfo(fileInfo);
		S2C4005 s2c4005 = new S2C4005();
		s2c4005.sFileInfo = fileInfo;
		channel.write(s2c4005);

		if( !"00".equals(fileInfo.resultCode4005)) {//验证不通过，需要关闭连接
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			ChannelManager.getInstance().remove(channel);
		}
	}

}
