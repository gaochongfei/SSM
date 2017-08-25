package cn.newcapec.chat.server.net.event;

/**
 * Created by es on 2016/6/15.
 */
public interface INetEvent {

    /** 建立网络连接 */
    public static final int EVENT_TYPE_CONNECT = 1;
    /** 网络连接断开 */
    public static final int EVENT_TYPE_CLOSEED = 2;
    /** 接收到网络消息 */
    public static final int EVENT_TYPE_RECEIVED = 3;

    void onEvent(int type, Object event);

}
