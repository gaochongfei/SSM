package cn.newcapec.chat.client;

import cn.newcapec.chat.client.base.AppServer;
import cn.newcapec.chat.client.base.ChannelManager;
import cn.newcapec.chat.client.base.ConfigManager;
import cn.newcapec.chat.client.bean.ChannelInfo;
import cn.newcapec.chat.client.net.NetEventManager;
import cn.newcapec.chat.client.net.NetWork;
import cn.newcapec.chat.client.net.c2s.C2S4001;
import cn.newcapec.chat.client.net.c2s.C2S4002;
import cn.newcapec.chat.client.net.c2s.C2STest;
import cn.newcapec.chat.client.net.event.INetEvent;
import cn.newcapec.chat.client.net.info.CFileInfo;
import cn.newcapec.tools.utils.ClientFileUtil;
import cn.newcapec.tools.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.netty.channel.Channel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Scanner;

/**
 * Created by Administrator on 2016/6/12.
 */
public class Main {

    private static Scanner sc;
    private static Log logServer = LogFactory.getLog("server");
    private static Log logClient = LogFactory.getLog("client");
    private static String host = ConfigManager.instance.getString("host_ip");
    private static int port = ConfigManager.instance.getInt("tcp_port");
    private static String[] centerCode;//配的客户端请求代码数组
    private static String centerName = ConfigManager.instance.getString("center_name");
    private static int codeNum;//剩余的机构代码数量
    private static String curCode;//当前的机构代码

    public static void main(String[] args){
        Main main = new Main();
        try {
            //加载外部配置文件  即打jar包时，log4j.properties放在跟jar包同级的conf目录下
            String filePath =  URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
            filePath = filePath.substring(0, filePath.lastIndexOf('/'));
            filePath = filePath + "/conf/log4j.properties";
            PropertyConfigurator.configure(filePath);

            logServer.info("------------------------------");
            logClient.info("启动客户端日志打印...");

            String tmpCode = ConfigManager.instance.getString("center_code");
            if(tmpCode != null){
                centerCode = tmpCode.split(",");
                codeNum = centerCode.length;
                curCode = centerCode[centerCode.length-codeNum];
                main.start();
                main.send4001(curCode);
            }else{
                logClient.info("无对应机构代码");
            }
        } catch (UnsupportedEncodingException e) {
            logClient.error("客户端获取log4j.properties路径错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 客户端上传文件
     * @param orgCode
     */
    public void send4001( String orgCode) {
        logClient.info("客户端机构代码"+orgCode+"上传文件开始......");
        try {
            //先看下有没有待上传的文件，没有的话就不请求了
            int fileCount = ClientFileUtil.getCFileCount(orgCode);
            if (fileCount == 0) {//没有需要上传的文件
                logClient.info("机构代码" + orgCode + "无需要上传的文件...");
                send4002(orgCode);
            } else {
                NetEventManager.addEvent(INetEvent.EVENT_TYPE_CONNECT, netEvent4001Connected);
                NetClient.getInstance().startConnect(host, port);//建立连接
            }
            codeNum --;
        } catch (IOException e) {
            logClient.error("发送4001时，获取文件个数抛出异常，异常信息：" + e.getMessage());
        }
    }

    /**
     * 客户端下载文件
     * @param orgCode
     */
    public void send4002(String orgCode) {
        logClient.info("客户端机构代码"+orgCode+"下载文件开始......");
        try {
            NetEventManager.addEvent(INetEvent.EVENT_TYPE_CONNECT, netEvent4002Connected);
            NetClient.getInstance().startConnect(host, port);//建立连接
        } catch (Exception e) {
            e.printStackTrace();
            logClient.error("发送4002时，抛出异常，异常信息：" + e.getMessage());
        }
    }

    /**
     * 增加监听
     */
    private void start() {
        NetEventManager.addEvent(INetEvent.EVENT_TYPE_CLOSEED, netEventClose);
        NetEventManager.addEvent(INetEvent.EVENT_TYPE_UPLOAD, netEvent4001Closed);
        NetEventManager.addEvent(INetEvent.EVENT_TYPE_DOWNLOAD, netEvent4002Closed);
    }
    /**
     * 退出当前mian
     */
    private void end(){
        try {
            NetEventManager.removeAllEvent();
            Thread.sleep(5000);//睡眠5s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void sendTest(){
        C2STest msg = new C2STest();
        msg.msg = "01";
        NetWork.sendC2S(msg);

        /*     C2STest msg = new C2STest();
            msg.msg = "中国";
            System.out.println(msg.toValueString().getBytes("GB2312").length);

            String s = "112";
            System.out.println(s);
            System.out.println(s.getBytes().length);
            System.out.println(s.getBytes("gb2312").length);
            System.out.println(s.getBytes("utf-8").length);
            String s1 = "中国";
            System.out.println(s1);
            System.out.println(s1.getBytes().length);
            System.out.println(s1.getBytes("gb2312").length);
            System.out.println(s1.getBytes("utf-8").length);

             sc = new Scanner(System.in);
            while(true) {
                System.out.println("请输入测试消息：");
                msg.msg = sc.next();
                 msg.len = 36+  msg.msg.getBytes("gb2312").length;
                NetWork.sendC2S(msg);
             }*/
    }

    /*
    * 监听广播，当4001上传文件请求结束时，开始下载文件请求
     */
    private INetEvent netEvent4001Closed = new INetEvent() {
        @Override
        public void onEvent(int type, Object event) {
            send4002(curCode);
        }
    };

    /**
     * 监听广播，当下载文件请求结束时，看有无配置的其它的机构，再次用这个机构代码上传下载
     */
    private INetEvent netEvent4002Closed = new INetEvent() {
        @Override
        public void onEvent(int type, Object event) {
            if(codeNum > 0){
                curCode = centerCode[centerCode.length-codeNum];
                send4001(curCode);
            }else{
                AppServer.isAppRun = false;
                end();
            }
        }
    };

    //监听连接关闭
    private INetEvent netEventClose = new INetEvent() {
        @Override
        public void onEvent(int type, Object event) {
            NetEventManager.removeEvent(INetEvent.EVENT_TYPE_CONNECT);
            Channel channel = (Channel) event;
            ChannelInfo channelInfo = ChannelManager.getInstance().get(channel);
            if (channelInfo != null){
                if (channelInfo.getCurmsgid() == 4001) {
                    NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_UPLOAD, "客户端上传文件请求完毕");
                } else if (channelInfo.getCurmsgid() == 4002) {
                    NetEventManager.notifyEvent(INetEvent.EVENT_TYPE_DOWNLOAD, "客户端下载文件请求完毕");
                }
         }
        }
    };

    /*
    * 监听广播，建立连接后才开始发送消息
    */
    private INetEvent netEvent4001Connected = new INetEvent() {
        @Override
        public void onEvent(int type, Object event) {
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setCurmsgid(4001);
            CFileInfo fileInfo = new CFileInfo();
            fileInfo.orgName = StringUtil.rightAddSpace(centerName, 40, " ");
            fileInfo.orgCode = StringUtil.rightAddSpace(curCode, 11, " ");
            fileInfo.reserveDomain4001 = StringUtil.rightAddSpace("", 256, "F");
            channelInfo.setFileInfo(fileInfo);
            ChannelManager.getInstance().add((Channel) event, channelInfo);
            C2S4001 msg = new C2S4001();
            msg.cFileInfo = fileInfo;
            NetWork.sendC2S(msg);
        }
    };

    /*
   * 监听广播，建立连接后才开始发送消息
   */
    private INetEvent netEvent4002Connected = new INetEvent() {
        @Override
        public void onEvent(int type, Object event) {
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setCurmsgid(4002);
            C2S4002 msg = new C2S4002();
            CFileInfo fileInfo = new CFileInfo();
            fileInfo.orgName = StringUtil.rightAddSpace(centerName, 40, " ");
            fileInfo.orgCode = StringUtil.rightAddSpace(curCode, 11, " ");
            fileInfo.accountDate = "00000000";
            fileInfo.reserveDomain4002 = StringUtil.rightAddSpace("", 256, "F");
            channelInfo.setFileInfo(fileInfo);
            ChannelManager.getInstance().add((Channel) event, channelInfo);
            msg.cFileInfo = fileInfo;
            NetWork.sendC2S(msg);
        }
    };
}