package cn.newcapec.chat.server.base;

import cn.newcapec.tools.utils.Log;
import org.jboss.netty.channel.Channel;

import java.util.List;
import java.util.Map;

/**
 * 检查socket连接的状态
 * Created by es on 2016/9/13.
 */
public class CheckSocketTask extends Thread {

    public static final CheckSocketTask instance = new CheckSocketTask();

    private CheckSocketTask() {
    }

    public static void init() {
        instance.start();
    }

    public void run() {
        while (true) {
            try {
                execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            waitSleep(30 * 1000); // 睡眠30 * 1000毫秒
        }
    }

    //睡眠一段时间
    private void waitSleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void execute() {
        List<Channel> channels = ChannelManager.getInstance().getChannels();
        if (channels == null) {
            return;
        }
        Map<Channel, Long> map = ChannelManager.getInstance().getChannelMap();
        long currTime = System.currentTimeMillis();
        long endTime;
        for (Channel channel : channels) {
            try {
                if (channel.isConnected()) {
                    endTime = map.get(channel);
                    if (endTime <= 0l) { //永不超时
                        continue;
                    }
                    if (currTime > endTime) { //连接已经超时，断开连接
                        //关闭socket连接
                        channel.disconnect();
                        ChannelManager.getInstance().remove(channel);
                    }
                } else {
                    ChannelManager.getInstance().remove(channel);
                }
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }
}
