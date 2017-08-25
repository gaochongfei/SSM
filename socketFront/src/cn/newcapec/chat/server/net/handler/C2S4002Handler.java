package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.base.ChannelManager;
import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.c2s.C2S4002;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.info.SFileInfo;
import cn.newcapec.chat.server.net.s2c.S2C4006;
import cn.newcapec.tools.utils.ServerFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import org.jboss.netty.channel.Channel;

public class C2S4002Handler implements C2SHandler {
	
	//4002
	public void handle(C2SMsg m) throws Exception {
		C2S4002 msg = (C2S4002)m;
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
		int fileCount = 0;
		if(4002 != msg.msgid){
			fileInfo.resultCode4006 = "02";//无效的消息类型码
		}else if(!"01".equals(msg.ver)){
			fileInfo.resultCode4006 = "03";//无效的消息版本
		}else if(!ServerFileUtil.ifBodyLen(msg.len,msg)){
			fileInfo.resultCode4006 = "04";//无效的保文长度
		}else{
			fileInfo.orgName = msg.sFileInfo.orgName;
			fileInfo.orgCode = msg.sFileInfo.orgCode;
			fileInfo.accountDate = msg.sFileInfo.accountDate;
			fileInfo.reserveDomain4002 = msg.sFileInfo.reserveDomain4002;
			fileCount = ServerFileUtil.getSFileCount(fileInfo.orgCode.trim());//获取服务端该机构可下载的文件个数
			fileInfo.fileCount = StringUtil.leftAddSpace(String.valueOf(fileCount),4,"0");
			fileInfo.restFileCount = fileCount;
			fileInfo.resultCode4006 = "00";
		}

		channelInfo.setFileInfo(fileInfo);
		S2C4006 s2c4006 = new S2C4006();
		s2c4006.sFileInfo = fileInfo;
		channel.write(s2c4006);

		if(fileCount == 0 || !"00".equals(fileInfo.resultCode4006)) {//需要下发的文件为0,则中心关闭连接，结束下载。
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			ChannelManager.getInstance().remove(channel);
		}
	}

}
