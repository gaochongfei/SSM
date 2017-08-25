package cn.newcapec.chat.client.net;

import cn.newcapec.chat.client.base.AppServer;
import cn.newcapec.chat.client.net.event.INetEvent;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.*;

;

/**
 * 接收到的数据消息处理中心
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetHandler extends SimpleChannelUpstreamHandler {

	private static Log log = LogFactory.getLog("client");

	public void channelConnected(ChannelHandlerContext ctx,
			final ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);
		log.info("<<<<<<<<<<<<已经连接>>>>>>>>>>>");
		// 保存网络连接的信息
		NetWork.tcpChannel = ctx.getChannel();
		NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_CONNECT, ctx.getChannel());
	}

	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelClosed(ctx, e);
		log.info(">>>>>>>>>>>连接断开<<<<<<<<<<");
		NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_CLOSEED, NetWork.tcpChannel);
		NetWork.tcpChannel = null;
	}

	public void messageReceived(final ChannelHandlerContext ctx,
			final MessageEvent e) {
		Object obj = e.getMessage();
		if (!(obj instanceof S2CMsg)) {
			return;
		}
		S2CMsg msg = (S2CMsg) obj;
		msg.e = e;
		log.info("接收:{ " + msg.getClass().getSimpleName() + " " + "msgid:" + msg.msgid +", "+ "msgfile:" + msg.cFileInfo.fileName +", "+
				"len:"+msg.len+", "+"syn:"+msg.syn+", "+"cutFlag:"+msg.cutFlag+", "+"enFlag:"+msg.enFlag+", "+"ver:"+msg.ver+", "
				+ msg.toString() +", "+"mac:"+msg.mac+"}");
		AppServer.instance.pushS2C(msg);
		NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_RECEIVED, msg);
	}

	/**
	 * Invoked when an exception was raised by an I/O thread or a
	 * {@link ChannelHandler}.
	 *
	 * @param ctx
	 * @param e
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
	}
}
