package cn.newcapec.chat.client.net;

import cn.newcapec.chat.client.net.c2s.C2SMsg;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 网络监听类
 *
 * @author WEIXING
 * @date 2014-8-24
 */
public class NetWork {

    /**
     * TCP连接信息
     */
    public static Channel tcpChannel;

    private static ClientBootstrap tcpBootstrap;
    private static ConnectionlessBootstrap udpBootstrap;

    /**
     * 建立网络连接
     *
     * @param ip
     * @param tcpPort
     */
    public static void start(String ip, int tcpPort) {
        if (isConnect()) { //当前已经连接
            return;
        }
        // TCP网络协议
        if (tcpBootstrap == null) {
            tcpBootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool()));
            tcpBootstrap.setPipelineFactory(new NetPipelineFactory());
        }
        String host = ip;
        int port = tcpPort;
        InetSocketAddress isa = new InetSocketAddress(host, port);
        // 连接到服务器
        tcpBootstrap.connect(isa);
        // UDP网络协议(暂时不用)
//        udpBootstrap = new ConnectionlessBootstrap(
//                new NioDatagramChannelFactory(Executors.newCachedThreadPool()));
//        udpBootstrap.setPipelineFactory(new NetPipelineFactory());
//        udpBootstrap.connect(new InetSocketAddress(HOST(), UDP_PORT()));
    }

    /**
     * 断开网络连接
     */
    public static void shutdown() {
        if (tcpBootstrap != null) {
            tcpBootstrap.shutdown();
            tcpBootstrap = null;
        }
        if (udpBootstrap != null) {
            udpBootstrap.shutdown();
            udpBootstrap = null;
        }
    }

    /**
     * 发送网络消息
     *
     * @param msg
     */
    public static void sendC2S(C2SMsg msg) throws RuntimeException {
        if (msg == null) {
            return;
        }
        if (tcpChannel == null || !tcpChannel.isOpen()) {
            System.out.println("网络连接已断开，无法发送消息。");
        }
        try {
            /*ChannelInfo channelInfo = new ChannelInfo();
            CFileInfo fileInfo = new CFileInfo();
            fileInfo.orgCode = msg.cFileInfo.orgCode.trim();
            channelInfo.setFileInfo(fileInfo);
            ChannelManager.getInstance().add(tcpChannel,channelInfo);*/
            tcpChannel.write(msg);
        } catch (Exception e) {
        }
    }

    /**
     * 判断当前是否已经打开
     *
     * @return
     */
    public static boolean isOpen() {
        if (tcpChannel == null) {
            return false;
        }
        return tcpChannel.isOpen();
    }

    /**
     * 判断当前是否已经连接
     *
     * @return
     */
    public static boolean isConnect() {
        if (tcpChannel == null) {
            return false;
        }
        return tcpChannel.isConnected();
    }

    /**
     * 断开当前连接
     */
    public static void disconnect() {
        if (tcpChannel != null && tcpChannel.isConnected()) {
            tcpChannel.disconnect();
        }
    }
}
