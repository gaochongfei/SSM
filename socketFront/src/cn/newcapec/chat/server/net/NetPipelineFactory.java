package cn.newcapec.chat.server.net;

import cn.newcapec.tools.utils.ServerFileUtil;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.ipfilter.IpFilterRuleHandler;
import org.jboss.netty.handler.ipfilter.IpFilterRuleList;


/**
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetPipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		IpFilterRuleHandler ipFilterRuleHandler = new IpFilterRuleHandler();
//		IpFilterRuleList list =  new  IpFilterRuleList(ServerFileUtil.findIpRules()+ ", -i:*");
		IpFilterRuleList list =  new  IpFilterRuleList("+i:*, -i:*");
		ipFilterRuleHandler.addAll(list);
		pipeline.addLast("ipFilter", ipFilterRuleHandler);
		pipeline.addLast("encoder", new NetEncoder());
		pipeline.addLast("decoder", new NetDecoder());
		pipeline.addLast("handler", new NetHandler());
		return pipeline;
	}

}
