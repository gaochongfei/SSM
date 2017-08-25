package cn.newcapec.chat.server.net;

import cn.newcapec.chat.server.base.AppServer;
import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.event.INetEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * 接收到的数据消息处理中心
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetHandler extends SimpleChannelUpstreamHandler {

	private static Log log = LogFactory.getLog("server");

	public void channelConnected(ChannelHandlerContext ctx,
			final ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);
		log.info("<<<<<<<<<<<<已经连接>>>>>>>>>>>");
		NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_CONNECT, ctx.getChannel());
	}

	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelClosed(ctx, e);
		log.info(">>>>>>>>>>>连接断开<<<<<<<<<<");
		NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_CLOSEED, ctx.getChannel());
	}

	public void messageReceived(final ChannelHandlerContext ctx,
			final MessageEvent e) {
		Object obj = e.getMessage();
		if (!(obj instanceof C2SMsg)) {
			return;
		}
		C2SMsg msg = (C2SMsg) obj;
		msg.e = e;
		log.info("接收:{ " + msg.getClass().getSimpleName() + " " + "msgid:" + msg.msgid +", "+
				"len:"+msg.len+", "+"syn:"+msg.syn+", "+"cutFlag:"+msg.cutFlag+", "+"enFlag:"+msg.enFlag+", "+"ver:"+msg.ver+", "
				+ msg.toString() +", "+"mac:"+msg.mac+"}");
		AppServer.instance.pushC2S(msg);
		NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_RECEIVED, msg);
	}
}
