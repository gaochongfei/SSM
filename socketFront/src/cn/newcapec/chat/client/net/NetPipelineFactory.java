package cn.newcapec.chat.client.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;


/**
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetPipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("encoder", new NetEncoder());
		pipeline.addLast("decoder", new NetDecoder());
		pipeline.addLast("handler", new NetHandler());
		return pipeline;
	}

}
