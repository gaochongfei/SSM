package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.base.ChannelManager;
import cn.newcapec.chat.client.bean.ChannelInfo;
import cn.newcapec.chat.client.net.NetEventManager;
import cn.newcapec.chat.client.net.c2s.C2S4004;
import cn.newcapec.chat.client.net.event.INetEvent;
import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.chat.client.net.s2c.S2C4005;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import cn.newcapec.tools.utils.ClientFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;

public class S2C4005Handler implements S2CHandler {
	private static Log log = LogFactory.getLog("client");
	
	//4005
	public void handle(S2CMsg m) throws Exception {
		S2C4005 msg = (S2C4005)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		CFileInfo fileInfo = channelInfo.getFileInfo();

		fileInfo.fileSize4005 = msg.cFileInfo.fileSize4005;
		fileInfo.resultCode4005 = msg.cFileInfo.resultCode4005;

		if("00".equals(fileInfo.resultCode4005)){//返回成功时
			int dataLen = fileInfo.restFileSzie;   //剩余没发送的字节长度
			int realLen = StringUtil.getCount(fileInfo.fileSize);//文件总长度，交通部规定，每个数据块长度最大1024
			int dataPacketNum = dataLen / 1024;
			int lastDataPacketLen = dataLen % 1024;
			int start = realLen - dataLen;//起始位置
			int size = 0;
			if(dataPacketNum >= 1){
				size = 1024;
			}else if(lastDataPacketLen > 0){
				size = lastDataPacketLen;
			}
			byte[] dest = new byte[size];
			System.arraycopy(ClientFileUtil.getCFileCont(fileInfo.orgCode.trim(),fileInfo.fileName.trim()),start,dest,start,size);
			fileInfo.restFileSzie = dataLen - size;
			if(fileInfo.restFileSzie != 0){
				fileInfo.flag = "0";
			}else{
				fileInfo.flag = "1";
			}
			fileInfo.dataBlock = dest;
			channelInfo.setFileInfo(fileInfo);

			C2S4004 c2s4004 = new C2S4004();
			c2s4004.cFileInfo = fileInfo;
			channel.write(c2s4004);
		}else{
			log.info("接收送断点通知报文（4005）返回失败，返回码：" + fileInfo.resultCode4005);
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			ChannelManager.getInstance().remove(channel);
			NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_UPLOAD,"客户端上传文件请求完毕");
		}

	}

}
