package cn.newcapec.chat.server;

import cn.newcapec.chat.server.base.AppServer;
import cn.newcapec.chat.server.base.CheckSocketTask;
import cn.newcapec.chat.server.base.ConfigManager;
import cn.newcapec.chat.server.net.MsgTable;
import cn.newcapec.chat.server.net.NetWork;
import cn.newcapec.chat.server.base.ChannelManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * 应用程序主入口
 *
 * @author WEIXING
 * @date 2014-8-24
 */
public class Bootstrap {

    private static Log logServer = LogFactory.getLog("server");
    private static Log logClient = LogFactory.getLog("client");
    private static String cacheUrl;

    public static String cacheUrl(){
        return cacheUrl;
    }
    private static Bootstrap instance;

    public static void main(String[] args) throws IOException {
        //加载外部配置文件 即打jar包时，log4j.properties放在跟jar包同级的conf目录下
        String filePath = URLDecoder.decode(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
        filePath = filePath.substring(0, filePath.lastIndexOf('/'));
        filePath = filePath + "/conf/log4j.properties";
        PropertyConfigurator.configure(filePath);

        logServer.info("启动服务端日志打印...");
        logClient.info("------------------------------");
        cacheUrl = ConfigManager.getInstance().getString("cache.url");
        logServer.info("cacheUrl: " + cacheUrl);
        getInstance().startServer();
    }

    public static Bootstrap getInstance() {
        if (instance == null) {
            instance = new Bootstrap();
        }
        return instance;
    }

    /**
     * 启动服务
     */
    public void startServer() {
        MsgTable.init();
        AppServer.init();
        CheckSocketTask.init();
        NetWork.start();
        logServer.info("服务器启动完成...");
    }

    /**
     * 关闭服务
     */
    public void shutdown() {
        NetWork.shutdown();
        ChannelManager.getInstance().clear();
        logServer.info("服务器已关闭...");
    }

}
