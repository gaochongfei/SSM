package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.net.s2c.S2CMsg;

/**
 * 客户端请求信息处理
 * @author WEIXING
 * @date 2014-8-24
 */
public interface S2CHandler {

	/**接收到服务端的返回信息*/
	public void handle(S2CMsg m) throws Exception;
	
}
