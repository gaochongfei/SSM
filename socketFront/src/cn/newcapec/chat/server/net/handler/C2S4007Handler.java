package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.base.ChannelManager;
import cn.newcapec.chat.server.base.ConfigManager;
import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.c2s.C2S4007;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.info.SFileInfo;
import cn.newcapec.chat.server.net.s2c.S2C4008;
import cn.newcapec.tools.utils.ServerFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import cn.newcapec.tools.utils.http.HttpClientUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class C2S4007Handler implements C2SHandler {
	private static Log log = LogFactory.getLog("server");
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private String fileName;

	//4007
	public void handle(C2SMsg m) throws Exception {
		C2S4007 msg = (C2S4007)m;
		Channel channel = msg.e.getChannel();
		ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
		SFileInfo fileInfo = channelInfo.getFileInfo();
		fileInfo.endFlag = msg.sFileInfo.endFlag;
		if(4007 != msg.msgid){
			fileInfo.resultCode4008 = "02";//无效的消息类型码
		}else if(!"01".equals(msg.ver)){
			fileInfo.resultCode4008 = "03";//无效的消息版本
		}else if(!ServerFileUtil.ifBodyLen(msg.len,msg)) {
			fileInfo.resultCode4008 = "04";//无效的报文长度
		}else {
			if("**TEOF**".equals(fileInfo.endFlag)){//收到结束报文后
				if(fileInfo.totalBlock.length == StringUtil.getCount(fileInfo.fileSize)){//判断收到的文件大小和之前发送的报文大小是否一致
					ServerFileUtil.createSFile(fileInfo.fileName.trim().substring(14,22),fileInfo.fileName.trim(),fileInfo.totalBlock);//服务端受文件
					fileInfo.totalBlock = null;//totalBlock置为空
					fileInfo.restFileCount--;//剩余文件个数

					//调用interactive处理文件
					fileName = fileInfo.fileName.trim();
					executorService.execute(new Runnable() {
						@Override
						public void run() {
							String name = fileName;
							log.info("调用interactive.jtserver通知解析文件：" + name );
							String sPath = ConfigManager.getInstance().getString("server.uploadPath");
							int i = 0;
							String code = "";//interactive返回码
							Map<String, String> params = new HashMap<String, String>();
							params.put("path","");
							params.put("fileName",name);
							params.put("orgCode",name.substring(14,22));
							String url = "";//访问接口的地址
							if(name.startsWith("TD")){
   								url = ConfigManager.getInstance().getString("server.odaDealUrl");
							}else{
								url = ConfigManager.getInstance().getString("server.dealUrl");
							}
							log.info("url:"+url+"params:"+ ArrayUtils.toString(params));
							Object[] objArr = HttpClientUtils.doPost(url,params);
							if(objArr != null && "200".equals(objArr[0].toString())){
								if(name != null && name.startsWith("CD")) {//CD文件需要验证是否正在结算,若正在结算，则再两次请求，每次间隔30s
									code = JSONObject.parseObject(objArr[1].toString()).getString("code");
									if ("10012".equals(code)) {//系统正在结算
										while(i < 2){
											try {
												Thread.sleep(30 * 1000);//休息30s
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											objArr = HttpClientUtils.doPost(ConfigManager.getInstance().getString("server.dealUrl"),params);
											code = JSONObject.parseObject(objArr[1].toString()).getString("code");
											log.info("第" + (i+2) + "次调用interactive解析文件"+name+"返回结果："+JSONObject.parseObject(objArr[1].toString()).getString("msg"));
											if(!"10012".equals(code)){
												break;
											}else {
												i++;
											}
										}
									}
								}
							}
							//http请求无响应，或http请求返回Connection refused，或系统正在日结，则将文件移至错误文件夹，后续手工处理
							if(objArr == null || (objArr != null && "0".equals(objArr[0].toString())) || i == 2){
								try {
									ServerFileUtil.copyFileUsingFileChannels(sPath,ConfigManager.getInstance().getString("server.errorPath"),name.substring(14,22) + "/",name);
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
		S2C4008 s2c4008 = new S2C4008();
		s2c4008.sFileInfo = fileInfo;
		channel.write(s2c4008);

		if( !"00".equals(fileInfo.resultCode4008)) {//验证不通过，需要关闭连接
			Thread.sleep(1000);//等待1s
			channel.disconnect();
			ChannelManager.getInstance().remove(channel);
		}
	}

}
