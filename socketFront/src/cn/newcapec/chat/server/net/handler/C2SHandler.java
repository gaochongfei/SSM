package cn.newcapec.chat.server.net.handler;


import cn.newcapec.chat.server.net.c2s.C2SMsg;

/**
 * 客户端请求信息处理
 * @author WEIXING
 * @date 2014-8-24
 */
public interface C2SHandler {

	/**接收到客户端的请求信息*/
	public void handle(C2SMsg m) throws Exception;
	
}
