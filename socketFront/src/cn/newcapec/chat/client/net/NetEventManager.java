package cn.newcapec.chat.client.net;

import cn.newcapec.chat.client.net.event.INetEvent;

import java.util.*;

/**
 * Created by es on 2016/6/15.
 */
public class NetEventManager {

    private static final Map<Integer, List<INetEvent>> eventMaps = new HashMap<Integer, List<INetEvent>>();

    private static final Object netEvent_lock = new Object();

    /**
     * 注册事件
     * @param type
     * @param event
     */
    public static void addEvent(int type, INetEvent event){
        if(event == null)return;
        List<INetEvent> list;
        synchronized (netEvent_lock) {
            if(eventMaps.containsKey(type)){
                list = eventMaps.get(type);
            }else{
                list = new ArrayList<INetEvent>();
                eventMaps.put(type, list);
            }
            if (list.contains(event)) return;
            list.add(event);
        }
    }

    public static void removeAllEvent(){
        synchronized (netEvent_lock) {
            eventMaps.clear();
        }
    }

    /**
     * 移除事件
     * @param type
     */
    public static void removeEvent(int type){
        if(!eventMaps.containsKey(type))return;
        synchronized (netEvent_lock) {
            List<INetEvent> list = eventMaps.get(type);
            list.clear();
        }
    }

    /**
     * 移除事件
     * @param type
     * @param event
     */
    public static void removeEvent(int type, INetEvent event){
        if(event == null)return;
        if(!eventMaps.containsKey(type))return;
        synchronized (netEvent_lock) {
            List<INetEvent> list = eventMaps.get(type);
            Iterator<INetEvent> it = list.iterator();
            while(it.hasNext()){
                INetEvent eventTmp = it.next();
                if(eventTmp == event){
                    it.remove();
                    break;
                }
            }
        }
    }

    /**
     * 发送通知
     * @param type
     * @param object
     */
    public static void notifyEvent(int type, Object object){
        if(!eventMaps.containsKey(type))return;
        synchronized (netEvent_lock) {
            List<INetEvent> list = eventMaps.get(type);
            Iterator<INetEvent> it = list.iterator();
            while(it.hasNext()){
                 INetEvent event = it.next();
                 if(event == null)continue;
                 try {
                     event.onEvent(type, object);
                 }catch (Exception e){
                     System.out.println(e.getMessage());
                 }
             }
        }
    }

}
