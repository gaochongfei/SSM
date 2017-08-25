package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.base.ChannelManager;
import cn.newcapec.chat.client.bean.ChannelInfo;
import cn.newcapec.chat.client.net.c2s.C2S4008;
import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.chat.client.net.s2c.S2C4004;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import cn.newcapec.tools.utils.StringUtil;
import org.jboss.netty.channel.Channel;

public class S2C4004Handler implements S2CHandler {

	//4004-->4008
	public void handle(S2CMsg m) throws Exception {
		S2C4004 msg = (S2C4004)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		CFileInfo fileInfo = channelInfo.getFileInfo();

		byte[] src = msg.cFileInfo.dataBlock;
		int destPos;
		if(fileInfo.totalBlock == null){
			fileInfo.totalBlock = new byte[Integer.parseInt(fileInfo.fileSize)];
			destPos = 0;
		}else{
			destPos = StringUtil.getRealLength(fileInfo.totalBlock);
		}
		System.arraycopy(src,0,fileInfo.totalBlock,destPos,src.length);//每次接收的字节放到一个临时字节数组中，待传输完毕时再生成文件
		fileInfo.flag = msg.cFileInfo.flag;
		fileInfo.resultCode4008 = "00";
		channelInfo.setFileInfo(fileInfo);
		C2S4008 c2s4008 = new C2S4008();
		c2s4008.cFileInfo = fileInfo;
		channel.write(c2s4008);
	}

}
