package cn.newcapec.chat.server.net;

import cn.newcapec.chat.server.base.ConfigManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerBossPool;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioWorkerPool;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络监听类
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetWork {
	private static Log log = LogFactory.getLog("server");

	private static ServerBootstrap tcpBootstrap;
	private static ConnectionlessBootstrap udpBootstrap;

	public static int TCP_PORT(){
		return ConfigManager.getInstance().getInt("tcp_port");
	}

	public static int UDP_PORT(){
		return ConfigManager.getInstance().getInt("udp_port");
	}

	public static void start() {
		// TCP网络服务
		ExecutorService executor = Executors.newCachedThreadPool();
		NioServerBossPool bossPool = new NioServerBossPool(executor, 4);
		NioWorkerPool workerPool = new NioWorkerPool(executor, 32);
		tcpBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				bossPool, workerPool));
		tcpBootstrap.setPipelineFactory(new NetPipelineFactory());
		log.info("tcp_port: " + TCP_PORT());
		InetSocketAddress tcp_addr = new InetSocketAddress(TCP_PORT());
		tcpBootstrap.bind(tcp_addr);


		// UDP网络服务
//		udpBootstrap = new ConnectionlessBootstrap(
//				new NioDatagramChannelFactory(Executors.newCachedThreadPool()));
//		udpBootstrap.setPipelineFactory(new NetPipelineFactory());
//		InetSocketAddress udp_addr = new InetSocketAddress(UDP_PORT());
//		udpBootstrap.bind(udp_addr);
	}

	public static void shutdown() {
		if(tcpBootstrap != null) {
			tcpBootstrap.shutdown();
		}
		if(udpBootstrap != null) {
			udpBootstrap.shutdown();
		}
	}
}
