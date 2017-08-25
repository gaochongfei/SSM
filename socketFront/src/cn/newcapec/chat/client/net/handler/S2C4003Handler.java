package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.base.ChannelManager;
import cn.newcapec.chat.client.base.ConfigManager;
import cn.newcapec.chat.client.bean.ChannelInfo;
import cn.newcapec.chat.client.net.c2s.C2S4005;
import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.chat.client.net.s2c.S2C4003;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import org.jboss.netty.channel.Channel;

public class S2C4003Handler implements S2CHandler {
	
	//4003--4005
	public void handle(S2CMsg m) throws Exception {
		S2C4003 msg = (S2C4003)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		int premsgid = channelInfo.getPremsgid();
		if(premsgid == 0){
			channelInfo.setPremsgid(4005);
			ChannelManager.getInstance().add(channel, channelInfo);
		}
		CFileInfo fileInfo = channelInfo.getFileInfo();
		String  centerCode = ConfigManager.instance.getString("center_code");

		fileInfo.fileName = msg.cFileInfo.fileName;
		fileInfo.fileSize4005 = "0";
		if(fileInfo.fileName.trim().length()== 33 && centerCode.contains(fileInfo.fileName.substring(14,22))){//接收代码不一致
			fileInfo.fileMark = msg.cFileInfo.fileMark;
			fileInfo.fileSize = msg.cFileInfo.fileSize;
			fileInfo.reserveDomain4003 = msg.cFileInfo.reserveDomain4003;
			fileInfo.resultCode4005 = "00";
		}else{
			fileInfo.resultCode4005 = "07";//文件名不合法
		}
		channelInfo.setFileInfo(fileInfo);

		C2S4005 c2s4005 = new C2S4005();
		c2s4005.cFileInfo = fileInfo;
		channel.write(c2s4005);
	}

}
