package cn.newcapec.chat.client;

import cn.newcapec.chat.client.base.AppServer;
import cn.newcapec.chat.client.net.MsgTable;
import cn.newcapec.chat.client.net.NetWork;
import cn.newcapec.chat.client.base.CheckSocketTask;
import org.jboss.netty.channel.Channel;

/**
 * Created by es on 2016/6/15.
 */
public class NetClient {

    private static boolean isConfig = false;
    private static NetClient instance;

    public static NetClient getInstance() {
        if (instance == null) {
            instance = new NetClient();
        }
        return instance;
    }
    private NetClient() {
    }

    /**
     * 获得连接对象
     * @return
     */
    public Channel getChannel(){
        return NetWork.tcpChannel;
    }

    /**
     * 开始建立连接
     */
    public void startConnect(String ip, int tcpPort) {
        if(!isConfig) {
            MsgTable.init();
            AppServer.init();
            CheckSocketTask.init();
            isConfig = true;
        }
        NetWork.start(ip, tcpPort);
    }

    /**
     * 关闭当前连接
     */
    public void shutdown(){
        NetWork.shutdown();
    }

    /**
     * 判断连接释放已经打开
     * @return
     */
    public boolean isOpen(){
        return NetWork.isOpen();
    }

    /**
     * 判断当前是否已经连接
     * @return
     */
    public boolean isConnect(){
        return NetWork.isConnect();
    }

    /**
     * 断开当前连接
     */
    public void disconnect(){
        NetWork.disconnect();
    }

}
