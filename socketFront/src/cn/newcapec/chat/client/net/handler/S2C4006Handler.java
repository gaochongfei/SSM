package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.base.ChannelManager;
import cn.newcapec.chat.client.bean.ChannelInfo;
import cn.newcapec.chat.client.net.c2s.C2S4008;
import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.chat.client.net.s2c.S2C4006;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;

public class S2C4006Handler implements S2CHandler {
	private static Log log = LogFactory.getLog("client");
	
	//4006
	public void handle(S2CMsg m) throws Exception {
		S2C4006 msg = (S2C4006)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		CFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.fileCount = msg.cFileInfo.fileCount;//总共的文件个数
		fileInfo.resultCode4006 = msg.cFileInfo.resultCode4006;
		fileInfo.restFileCount =  Integer.parseInt(fileInfo.fileCount);//剩余的文件个数

		if ("00".equals(fileInfo.resultCode4006)){//成功

			fileInfo.resultCode4008 = "00";
			channelInfo.setFileInfo(fileInfo);

			if(!"0000".equals(fileInfo.fileCount)){//需下载文件个数不为0时
				C2S4008 c2s4008 = new C2S4008();
				c2s4008.cFileInfo = fileInfo;
				channel.write(c2s4008);
			}else{
				log.info("<<<<没有要下载的文件");
				channel.disconnect();
			}
		}else{
			log.info("接收文件数通知报文（4006）返回失败，返回码：" + fileInfo.resultCode4006);
		}
	}

}
