package cn.newcapec.chat.client.bean;

import cn.newcapec.chat.client.net.info.CFileInfo;

import java.text.SimpleDateFormat;

/**
 * 网络连接信息
 */
public class ChannelInfo extends BaseObject {

    //网络连接失效时间  默认0(永不失效)
    private Long invalidTime = 60000l;//30s
    //连接保存的上一个消息类型
    private int premsgid;
    //当前消息类型是上传还是下载4001上传，4002下载
    private int curmsgid;
    private CFileInfo fileInfo;

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

    public CFileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(CFileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public int getCurmsgid() {
        return curmsgid;
    }

    public void setCurmsgid(int curmsgid) {
        this.curmsgid = curmsgid;
    }


  /*  public static void main(String[] args) throws  Exception{
        String sDt = "08/15/2017 20:30:00";
        SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        java.util.Date dt2 = sdf.parse(sDt);
        long lTime = dt2.getTime();
        System.out.println(lTime);
    }*/
}