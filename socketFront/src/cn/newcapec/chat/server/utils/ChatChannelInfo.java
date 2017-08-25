package cn.newcapec.chat.server.utils;

import cn.newcapec.chat.server.net.NetEventManager;
import cn.newcapec.chat.server.net.event.INetEvent;
import org.jboss.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存聊天通道信息
 * Created by es on 2016/6/15.
 */
public class ChatChannelInfo {

    /** 闲置连接超时时间 */
    public static final long TIMEOUT = 30 * 1000;

    private static INetEvent netEvent = new INetEvent() {
        public void onEvent(int type, Object event) {
            if(event != null && event instanceof Channel){
                Channel channel = (Channel)event;
                ChatChannelInfo.getInstance().remove(channel);
            }
        }
    };

    private static ChatChannelInfo instance;

    public static ChatChannelInfo getInstance(){
        if(instance == null){
            instance = new ChatChannelInfo();
            NetEventManager.addEvent(INetEvent.EVENT_TYPE_CLOSEED, netEvent);
        }
        return instance;
    }

    private final Map<Channel, Long> channelMap = new ConcurrentHashMap<Channel, Long>();

    public Map<Channel, Long> getChannelMap(){
        return channelMap;
    }

    public void add(Channel channel, Long endTime){
        channelMap.put(channel, endTime);
    }

    public Long remove(Channel channel){
        return channelMap.remove(channel);
    }

    public List<Channel> getChannels(){
        if(channelMap.size() == 0){
            return null;
        }
        Collection<Channel> collections = channelMap.keySet();
        List<Channel> list = new ArrayList<Channel>();
        for (Channel channel : collections){
            list.add(channel);
        }
        return list;
    }

    /**
     * 清理断开所有连接
     */
    public void clear(){
        if(channelMap.size() == 0){
            return;
        }
        Collection<Channel> collections = channelMap.keySet();
        List<Channel> list = new ArrayList<Channel>();
        for (Channel channel : collections){
            if(channel != null && channel.isConnected()) {
                channel.close();
            }
        }
    }

}
