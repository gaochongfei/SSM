package cn.newcapec.chat.server.base;

import cn.newcapec.chat.server.bean.ChannelInfo;
import cn.newcapec.chat.server.net.NetEventManager;
import cn.newcapec.chat.server.net.event.INetEvent;
import org.jboss.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接通道信息管理
 * Created by es on 2016/6/15.
 */
public class ChannelManager {

    private static Object object = new Object();

    /**
     * 闲置连接超时时间
     */
    public static final long TIMEOUT = 60 * 1000;//60s

    private static INetEvent netEvent = new INetEvent() {
        public void onEvent(int type, Object event) {
            if (event != null && event instanceof Channel) {
                Channel channel = (Channel) event;
                ChannelManager.getInstance().remove(channel);
            }
        }
    };

    private static ChannelManager instance;

    public static ChannelManager getInstance() {
        if (instance == null) {
            synchronized (object) {
                if (instance == null) {
                    instance = new ChannelManager();
                    NetEventManager.addEvent(INetEvent.EVENT_TYPE_CLOSEED, netEvent);
                }
            }
        }
        return instance;
    }

    private final Map<Channel, ChannelInfo> channelMap = new ConcurrentHashMap<Channel, ChannelInfo>();

    public Map<Channel, Long> getChannelMap() {
        Map<Channel, Long> result = new HashMap<Channel, Long>();
        synchronized (object) {
            Set<Channel> keys = channelMap.keySet();
            ChannelInfo channelInfo;
            for (Channel channel : keys) {
                channelInfo = channelMap.get(channel);
                if (channelInfo != null) {
                    result.put(channel, channelInfo.getInvalidTime());
                }
            }
        }
        return result;
    }

    public void add(Channel channel, ChannelInfo channelInfo) {
        synchronized (object) {
            channelMap.put(channel, channelInfo);
        }
    }

    public ChannelInfo remove(Channel channel) {
        if (!channelMap.containsKey(channel)) {
            return null;
        }
        synchronized (object) {
            return channelMap.remove(channel);
        }
    }

    /**
     * 获得通道保存的用户信息
     *
     * @param channel
     * @return
     */
    public ChannelInfo get(Channel channel) {
        return channelMap.get(channel);
    }

    /**
     * 获得所有的聊天通道信息
     *
     * @return
     */
    public List<Channel> getChannels() {
        if (channelMap.size() == 0) {
            return null;
        }
        List<Channel> list = new ArrayList<Channel>();
        synchronized (object) {
            Collection<Channel> collections = channelMap.keySet();
            for (Channel channel : collections) {
                list.add(channel);
            }
        }
        return list;
    }

    /**
     * 清理断开所有连接
     */
    public void clear() {
        if (channelMap.size() == 0) {
            return;
        }
        synchronized (object) {
            Collection<Channel> collections = channelMap.keySet();
            List<Channel> list = new ArrayList<Channel>();
            for (Channel channel : collections) {
                if (channel != null && channel.isConnected()) {
                    channel.close();
                }
            }
        }
        channelMap.clear();
    }
    /**
     * 获得某一设备或者某一用户的聊天通道信息
     *
     * @return
     */
    public List<Channel> getChannelsByWybs(String wybs) {
        if (channelMap.size() == 0) {
            return null;
        }
        List<Channel> list = new ArrayList<Channel>();
        ChannelInfo channelInfo;
        synchronized (object) {
            Collection<Channel> collections = channelMap.keySet();
            for (Channel channel : collections) {
                channelInfo = channelMap.get(channel);
                if (channelInfo != null) {
                    /*if(channelInfo.getDeviceId().equals(wybs)){
                        list.add(channel);
                    }else if(channelInfo.getUserInfo().id.equals(wybs)){
                        list.add(channel);
                    }else if(channelInfo.getUserInfo().phonenum.equals(wybs)){
                        list.add(channel);
                    }*/
                }
            }
        }
        return list;
    }
}
