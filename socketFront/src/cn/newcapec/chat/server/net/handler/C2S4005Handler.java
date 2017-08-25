package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.base.ChannelManager;
import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.c2s.C2S4005;
import cn.newcapec.chat.server.net.info.SFileInfo;
import cn.newcapec.chat.server.net.s2c.S2C4004;
import cn.newcapec.chat.server.net.s2c.S2C4008;
import cn.newcapec.tools.utils.ServerFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;

public class C2S4005Handler implements C2SHandler {
	private static Log log = LogFactory.getLog("server");
	
	//4005
	public void handle(C2SMsg m) throws Exception {
		C2S4005 msg = (C2S4005)m;

		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		channelInfo.setPremsgid(4004);
		SFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.fileSize4005 = msg.sFileInfo.fileSize4005;
		fileInfo.resultCode4005 = msg.sFileInfo.resultCode4005;

		if(!"00".equals(fileInfo.resultCode4005)){//返回不成共时
			log.info("接收送断点通知报文（4005）返回失败，返回码：" + fileInfo.resultCode4005);

			Thread.sleep(1000);//等待1s
			channel.disconnect();
			ChannelManager.getInstance().remove(channel);
		}else{
			fileInfo.resultCode4008 = "00";

			int dataLen = fileInfo.restFileSize;   //剩余没发送的字节长度
			int realLen = StringUtil.getCount(fileInfo.fileSize);//文件总长度，交通部规定，每个数据块长度最大1024
			int dataPacketNum = dataLen / 1024;
			int lastDataPacketLen = dataLen % 1024;
			int start = realLen - dataLen;//起始位置
			int size = 0;
			byte[] dest = null;
			if(dataPacketNum >= 1){
				size = 1024;
				dest = new byte[size];
				System.arraycopy(ServerFileUtil.getSFileCont(fileInfo.orgCode.trim(),fileInfo.fileName.trim()),start,dest,start,size);
			}else if(lastDataPacketLen > 0){
				size = lastDataPacketLen;
				dest = new byte[size];
				System.arraycopy(ServerFileUtil.getSFileCont(fileInfo.orgCode.trim(),fileInfo.fileName.trim()),start,dest,start,size);
			}
			fileInfo.restFileSize = dataLen - size;
			if(fileInfo.restFileSize != 0){
				fileInfo.flag = "0";
			}else{
				fileInfo.flag = "1";
			}
			fileInfo.dataBlock = dest;
			channelInfo.setFileInfo(fileInfo);

			S2C4008 s2c4008 = new S2C4008();
			s2c4008.sFileInfo = fileInfo;
			channel.write(s2c4008);

			S2C4004 s2c4004 = new S2C4004();
			s2c4004.sFileInfo = fileInfo;
			channel.write(s2c4004);
		}

	}

}
