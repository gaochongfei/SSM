package cn.newcapec.tools.utils;

import cn.newcapec.chat.server.base.ConfigManager;
import cn.newcapec.chat.server.net.c2s.C2SMsg;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by dimm on 2017/2/17.
 */
public class ServerFileUtil {

    private static String serverDownPath = ConfigManager.getInstance().getString("server.downPath");//服务端待下发路径
    private static String serverDownBak = ConfigManager.getInstance().getString("server.downPathBak");//服务端已下发备份路径
    private static String serverUploadPath = ConfigManager.getInstance().getString("server.uploadPath");//服务端接收文件路径
    private static String limitIpPath = ConfigManager.getInstance().getString("limitip.path");//白名单IP文件路径

    //创建服务端接收文件
    public static void createSFile(String orgCode,String fileName, byte[] content) throws IOException {
        File file = new File(serverUploadPath + orgCode +"/");
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(serverUploadPath + orgCode +"/" + fileName);
        fos.write(content);
        fos.flush();
        fos.close();
    }

    //服务端待下发文件转移至服务端已下发文件备份路径
    public static void copySFile(String orgCode,String fileName) throws IOException {
        copyFileUsingFileChannels(serverDownPath, serverDownBak, orgCode + "/",fileName);
    }

    public static void copyFileUsingFileChannels(String sPath, String dPath,String orgCode, String fileName) throws IOException {
        File dest = new File(dPath + orgCode);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(sPath + orgCode + fileName).getChannel();
            outputChannel = new FileOutputStream(dPath + orgCode + fileName).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
            File source = new File(sPath + orgCode + fileName);
            source.delete();

        }
    }

    //获取服务端文件夹下文件个数
    public static int getSFileCount(String orgCode) throws IOException {
        File file = new File(serverDownPath + orgCode + "/");
        int fileCount = 0;
        if (file != null && file.isDirectory()) {
            File[] fs = file.listFiles();
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].isFile()) {
                    fileCount++;
                }
            }
        }
        return fileCount;
    }

    //获取服务端文件下第一个待下发的文件名称
    public static String getSFileFirst(String orgCode) throws IOException {
        File file = new File(serverDownPath + orgCode + "/");
        String fileName = "";
        if (file != null && file.isDirectory()) {
            File[] fs = file.listFiles();
            if(fs != null && fs.length > 0){
                fileName = fs[0].getName();
            }
        }
        return fileName;
    }

    //获取服务端文件长度
    public static int getSFileLen(String fileName) {
        int fileLen = 0;
        File f = new File(serverDownPath + fileName);
        if (f.exists() && f.isFile()){
            fileLen = (int)f.length();
        }
        return fileLen;
    }

    //获取服务器端文件内容
    public static byte[] getSFileCont(String orgCode,String fileName) {
        byte[] strBuffer = null;
        int  flen = 0;
        File f = new File(serverDownPath + orgCode + "/" + fileName);
        try {
            InputStream in = new FileInputStream(f);
            flen = (int)f.length();
            strBuffer = new byte[flen];
            in.read(strBuffer, 0, flen);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strBuffer;
    }

    /**
     * 获取所有的白名单ip
     * @return
     */
    public static String findIpRules(){
        String ipRules = "";
        byte[] strBuffer = null;
        int  flen = 0;
        File f = new File(limitIpPath + "limitip.config");
        try {
            InputStream in = new FileInputStream(f);
            flen = (int)f.length();
            strBuffer = new byte[flen];
            in.read(strBuffer, 0, flen);
            in.close();
            ipRules = new String(strBuffer,"utf-8");
            if(!StringUtil.isEmpty(ipRules)){
                if(ipRules.endsWith(";")){
                    ipRules = ipRules.substring(0,ipRules.length()-1);
                }
                ipRules = ipRules.replaceAll(";",",+i:");
                ipRules = "+i:" + ipRules;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipRules;
    }

    public static void main(String[] args) {
        System.out.println(ServerFileUtil.findIpRules());
    }

    /**
     * 验证报文长度
     * @param len1
     * @param len2
     * @param msgid
     * @return
     */
    public static boolean ifBodyLen(int len1, C2SMsg msg){
        boolean bool = false;
        int len2;
        if(msg.msgid == 4004){
            len2 = 36 + msg.sFileInfo.flag.length() + msg.sFileInfo.dataBlock.length;
        }else{
            len2= 36 + StringUtil.toValueLen(msg.toString());
        }
        if(len1 == len2) bool = true;
        return bool;
    }
}