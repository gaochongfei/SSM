package cn.newcapec.tools.utils;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.*;

/**
 * Created by dimm on 2017/2/17.
 */
public class StringUtil {

    /**
     * 获取字节的长度
     */
    public static int getStrByteCount(String str){
        int num=0;
        try{
            if(!StringUtil.isEmpty(str)){
                num=str.getBytes("GB2312").length;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return num;
    }

    /**
     * [判断字符串是否为null为""]
     *
     * @param str
     * @return boolean true:为null或""
     */

    public static boolean isEmpty(String str){
        return null == str || "".equals(str.trim());
    }

    /**
     * 左填充字符串，规定字符串补齐
     */
    public static String leftAddSpace(String str,int num,String space){
        String resultStr="";
        String tempStr="";
        int length=0;
        try {
            length=getStrByteCount(str);
            if(length<=num){
                for(int i=0;i<(num-length);i++){
                    tempStr+=space;
                }
                resultStr=tempStr+str;
            }else{
               System.out.println("补齐字符串："+str+"出错。");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 右填充字符串，规定字符串补齐
     */
    public static String rightAddSpace(String str,int num,String space){
        String resultStr="";
        String tempStr="";
        int length=0;
        try {
            length=getStrByteCount(str);
            if(length<=num){
                for(int i=0;i<(num-length);i++){
                    tempStr+=space;
                }
                resultStr=str+tempStr;
                //System.out.println("补齐后字符串长度为："+getStrByteCount(resultStr));
            }else{
                System.out.println("补齐字符串："+str+"出错。");
            }

        } catch (Exception e) {
            System.out.println("补齐字符串："+str+"异常。"+e);
        }
        return resultStr;
    }

    public static String getSubStr(String msg,int start,int length){
        byte[] resByte = null ;
        String resCode="";
        byte[] newData = new byte[length];
        try {
            resByte = msg.getBytes("GB2312");
            System.arraycopy(resByte, start, newData,0, length);
            resCode = new String(newData,"GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resCode;
    }

    public static void writeUTF8(Object s, ChannelBuffer buffer)
            throws UnsupportedEncodingException {
        if (s == null) {
            return;
        }
        byte[] src = s.toString().getBytes("GB2312");
        buffer.writeBytes(src);
    }

    public static String readUTF8(ChannelBuffer buffer,int len)
            throws UnsupportedEncodingException {
        byte[] dst = new byte[len];
        buffer.readBytes(dst);
        return new String(dst, "GB2312");
    }

    public static int getFileLen() {
        int fileLen = 0;
        File f = new File("E:/dimm/项目/云南省/胡凡凡/BN161024000000026449300000000132A");
        if (f.exists() && f.isFile()){
            fileLen = (int)f.length();
        }
        return fileLen;
    }

    /**
     * 获取字符串长度
     * @param str
     * @return
     */
    public static int toValueLen(String str){
        int len = 0;
        String strVal = "";
        String []sp = str.split(",");
        for (int i=0; i<sp.length; i++) {
            strVal += sp[i].substring(sp[i].indexOf(":")+1,sp[i].length());
        }
        try {
            len = strVal.getBytes("GB2312").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return len;
    }

    //求得byte数组的逻辑长度
    public static int getRealLength(byte[] a){
        int i=0;
        for(;i<a.length;i++){
            if(a[i]=='\0') break;
        }
         return i;
    }

    /**
     * 去掉左边的0
     * @param str
     * @return
     */
    public static int getCount(String str){
        str = str.replaceAll("^(0+)", "");
        if("".equals(str)) str= "0";
        int cnt = Integer.parseInt(str);
        return cnt;
    }

}
