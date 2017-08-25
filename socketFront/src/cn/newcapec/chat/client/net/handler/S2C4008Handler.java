package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.base.ChannelManager;
import cn.newcapec.chat.client.bean.ChannelInfo;
import cn.newcapec.chat.client.net.c2s.C2S4003;
import cn.newcapec.chat.client.net.c2s.C2S4004;
import cn.newcapec.chat.client.net.c2s.C2S4006;
import cn.newcapec.chat.client.net.c2s.C2S4007;
import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.chat.client.net.s2c.S2C4008;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import cn.newcapec.tools.utils.ClientFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import org.jboss.netty.channel.Channel;

public class S2C4008Handler implements S2CHandler {
	
	//4008
	public void handle(S2CMsg m) throws Exception {
		S2C4008 msg = (S2C4008)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		CFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.resultCode4008 = msg.cFileInfo.resultCode4008;
		if("00".equals(fileInfo.resultCode4008)){
			int premsgid = channelInfo.getPremsgid();
			if(premsgid == 0){
				int fileCount = ClientFileUtil.getCFileCount(fileInfo.orgCode.trim());//获取客户端需要上传的文件数
				if(fileCount == 0){//需要下发的文件为0,则关闭连接，结束下载。
					channel.disconnect();
				}else {
					fileInfo.fileCount = StringUtil.leftAddSpace(String.valueOf(fileCount), 4, "0");
					fileInfo.restFileCount = fileCount;
					fileInfo.resultCode4006 = "00";
					channelInfo.setFileInfo(fileInfo);

					channelInfo.setPremsgid(4006);
					C2S4006 c2s4006 = new C2S4006();
					c2s4006.cFileInfo = fileInfo;
					channel.write(c2s4006);
				}
			}else if(premsgid == 4006 || premsgid == 4007 ){
				if(premsgid == 4007){
					//将成功传送的文件移至备份目录
					ClientFileUtil.copyCFile(fileInfo.fileName.trim().substring(14,22),fileInfo.fileName.trim());
				}
				if(fileInfo.restFileCount != 0) {//证明还有文件，继续下发
					fileInfo.fileName = StringUtil.rightAddSpace(ClientFileUtil.getCFileFirst(fileInfo.orgCode.trim()), 50, " ");//获取需要上传的第一个文件
					fileInfo.fileMark = StringUtil.rightAddSpace("", 256, " ");
					int fileSize = ClientFileUtil.getCFileLen(fileInfo.orgCode.trim(),fileInfo.fileName.trim());
					fileInfo.fileSize = StringUtil.leftAddSpace(String.valueOf(fileSize), 10, "0");
					fileInfo.restFileSzie = fileSize;
					fileInfo.reserveDomain4003 = StringUtil.rightAddSpace("", 256, " ");
					channelInfo.setFileInfo(fileInfo);

					channelInfo.setPremsgid(4003);
					C2S4003 c2s4003 = new C2S4003();
					c2s4003.cFileInfo = fileInfo;
					channel.write(c2s4003);
				}else{
					channel.disconnect();
					//ChannelManager.getInstance().remove(channel);
					//NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_UPLOAD,"客户端上传文件请求完毕");
				}
			}else if(premsgid == 4003){
				if(fileInfo.flag == "1") {
					fileInfo.restFileCount--;//成功传输一个，文件数-1
					fileInfo.endFlag = "**TEOF**";
					channelInfo.setPremsgid(4007);
					C2S4007 c2s4007 = new C2S4007();
					c2s4007.cFileInfo = fileInfo;
					channelInfo.setFileInfo(fileInfo);
					channel.write(c2s4007);
				}else{
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
					System.arraycopy(ClientFileUtil.getCFileCont(fileInfo.orgCode.trim(),fileInfo.fileName.trim()),start,dest,0,size);
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
				}
			}else if(premsgid == 4005){

			}
		}else{ //收到的应答不成功，关闭连接
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			//ChannelManager.getInstance().remove(channel);
		}

	}

}
