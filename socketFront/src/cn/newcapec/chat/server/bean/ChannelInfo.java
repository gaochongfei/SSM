package cn.newcapec.chat.server.bean;

import cn.newcapec.chat.server.net.info.SFileInfo;

/**
 * 网络连接信息
 */
public class ChannelInfo extends BaseObject {

    //网络连接失效时间  默认0(永不失效)
    private Long invalidTime =0l ;//30s
    //连接保存的上一个消息类型
    private int premsgid;

    private SFileInfo fileInfo;

    public Long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Long invalidTime) {
        this.invalidTime = invalidTime;
    }

    public int getPremsgid() {
        return premsgid;
    }

    public void setPremsgid(int premsgid) {
        this.premsgid = premsgid;
    }

    public SFileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(SFileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}