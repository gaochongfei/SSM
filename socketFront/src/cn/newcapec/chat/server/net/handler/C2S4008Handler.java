package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.base.ChannelManager;
import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.c2s.C2S4008;
import cn.newcapec.chat.server.net.info.SFileInfo;
import cn.newcapec.chat.server.net.s2c.S2C4003;
import cn.newcapec.chat.server.net.s2c.S2C4004;
import cn.newcapec.chat.server.net.s2c.S2C4007;
import cn.newcapec.tools.utils.ServerFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import org.jboss.netty.channel.Channel;

public class C2S4008Handler implements C2SHandler {

	//4008
	public void handle(C2SMsg m) throws Exception {

		C2S4008 msg = (C2S4008)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		SFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.resultCode4008 = msg.sFileInfo.resultCode4008;
		if("00".equals(fileInfo.resultCode4008)){
			int premsgid = channelInfo.getPremsgid();//先前的消息类型
			if(premsgid == 0 || premsgid == 4007){
				if(premsgid == 4007){
					//将成功传送的文件移至备份目录
					ServerFileUtil.copySFile(fileInfo.fileName.trim().substring(14,22),fileInfo.fileName.trim());
				}
				if(fileInfo.restFileCount != 0){//证明还有文件，继续下发
					channelInfo.setPremsgid(4003);
					fileInfo.fileName = StringUtil.rightAddSpace(ServerFileUtil.getSFileFirst(fileInfo.orgCode.trim()),50," ");
					fileInfo.fileMark = StringUtil.rightAddSpace("",256," ");
					int fileSize = ServerFileUtil.getSFileLen(fileInfo.orgCode.trim()+"/"+fileInfo.fileName.trim());
					fileInfo.fileSize = StringUtil.leftAddSpace(String.valueOf(fileSize),10,"0");
					fileInfo.restFileSize = fileSize;
					fileInfo.reserveDomain4003 = StringUtil.rightAddSpace("",256," ");
					S2C4003 s2c4003 = new S2C4003();
					s2c4003.sFileInfo = fileInfo;
					channel.write(s2c4003);
				}
			}else if(premsgid == 4004){
				if(fileInfo.flag == "1"){
					channelInfo.setPremsgid(4007);
					fileInfo.endFlag = "**TEOF**";
					fileInfo.restFileCount--;//成功传输一个，文件数-1
					S2C4007 s2c4007 = new S2C4007();
					s2c4007.sFileInfo = fileInfo;
					channel.write(s2c4007);
				}else{
					int dataLen = fileInfo.restFileSize;   //剩余没发送的字节长度
					int realLen =StringUtil.getCount(fileInfo.fileSize);//文件总长度，交通部规定，每个数据块长度最大1024
					int dataPacketNum = dataLen / 1024;
					int lastDataPacketLen = dataLen % 1024;
					int start = realLen - dataLen;//起始位置
					int size = 0;
					byte[] dest = null;
					if(dataPacketNum >= 1){
						size = 1024;
						dest = new byte[size];
						System.arraycopy(ServerFileUtil.getSFileCont(fileInfo.orgCode.trim(),fileInfo.fileName.trim()),start,dest,0,size);
					}else if(lastDataPacketLen > 0){
						size = lastDataPacketLen;
						dest = new byte[size];
						System.arraycopy(ServerFileUtil.getSFileCont(fileInfo.orgCode.trim(),fileInfo.fileName.trim()),start,dest,0,size);
					}
					fileInfo.restFileSize = dataLen - size;
					if(fileInfo.restFileSize != 0){
						fileInfo.flag = "0";
					}else{
						fileInfo.flag = "1";
					}
					fileInfo.dataBlock = dest;
					channelInfo.setFileInfo(fileInfo);
					S2C4004 s2c4004 = new S2C4004();
					s2c4004.sFileInfo = fileInfo;
					channel.write(s2c4004);
				}
			}
		}else{ //收到的应答不成功，关闭连接
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			ChannelManager.getInstance().remove(channel);
		}
	}

}
