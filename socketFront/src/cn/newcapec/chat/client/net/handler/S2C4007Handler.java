package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.base.ChannelManager;
import cn.newcapec.chat.client.bean.ChannelInfo;
import cn.newcapec.chat.client.net.c2s.C2S4008;
import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.chat.client.net.s2c.S2C4007;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import cn.newcapec.tools.utils.ClientFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import cn.newcapec.tools.utils.http.HttpClientUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class S2C4007Handler implements S2CHandler {
	private static Log log = LogFactory.getLog("client");
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private String fileName;

	//4007
	public void handle(S2CMsg m) throws Exception {
		S2C4007 msg = (S2C4007)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		CFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.endFlag = msg.cFileInfo.endFlag;

		if(4007 != msg.msgid){
			fileInfo.resultCode4008 = "02";//无效的消息类型码
		}else if(!"01".equals(msg.ver)){
			fileInfo.resultCode4008 = "03";//无效的消息版本
		}else if(!ClientFileUtil.ifBodyLen(msg.len,msg)) {
			fileInfo.resultCode4008 = "04";//无效的报文长度
		}else {
			if("**TEOF**".equals(fileInfo.endFlag)){//收到结束报文后
				if(fileInfo.totalBlock.length == StringUtil.getCount(fileInfo.fileSize)){//判断收到的文件大小和之前发送的报文大小是否一致
					ClientFileUtil.createCFile(fileInfo.fileName.trim().substring(14,22),fileInfo.fileName.trim(),fileInfo.totalBlock);//客户端接收文件
					fileInfo.totalBlock = null;//totalBlock置为空
					fileInfo.restFileCount--;//剩余文件个数-1

					//调用interactive处理文件
					fileName = fileInfo.fileName.trim();
					executorService.execute(new Runnable() {
						@Override
						public void run() {
							String name = fileName;
							log.info("调用interactive.jtclient通知解析文件：" + name );
							String sPath = cn.newcapec.chat.client.base.ConfigManager.instance.getString("client.downPath");
							Map<String, String> params = new HashMap<String, String>();
							params.put("path", "");
							params.put("fileName",name);
							params.put("orgCode",name.substring(14,22));
							String url_ = cn.newcapec.chat.client.base.ConfigManager.instance.getString("client.dealUrl");
							Object[] objArr = HttpClientUtils.doPost(url_,params);
							log.info("url:"+url_+"params:"+ ArrayUtils.toString(params));
							//http请求无响应，或http请求返回Connection refused
							if(objArr == null || (objArr != null && "0".equals(objArr[0].toString()))){
								try {
									ClientFileUtil.copyFileUsingFileChannels(sPath, cn.newcapec.chat.client.base.ConfigManager.instance.getString("client.errorPath"),name.substring(14,22) + "/",name);
								} catch (IOException e) {
									log.error("移动文件" + name+ "抛出异常：" + e.getMessage());
								}

							}
							log.info("调用interactive解析文件"+name+"返回结果：" + ArrayUtils.toString(objArr));
						}
					});
					fileInfo.resultCode4008 = "00";
				}else{//文件大小没匹配上
					fileInfo.resultCode4008 = "08";//文件大小或内容不合法
				}
			}else{
				fileInfo.resultCode4008 = "50";//系统错误
			}
		}
		channelInfo.setFileInfo(fileInfo);
		C2S4008 c2sw4008 = new C2S4008();
		c2sw4008.cFileInfo = fileInfo;
		channel.write(c2sw4008);

		if(fileInfo.restFileCount == 0){//全部文件传送完毕
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			//ChannelManager.getInstance().remove(channel);
			//NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_DOWNLOAD,"客户端下载文件请求完毕");
		}
		if( !"00".equals(fileInfo.resultCode4008)) {//验证不通过，需要关闭连接
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			//ChannelManager.getInstance().remove(channel);
		}
	}
}
