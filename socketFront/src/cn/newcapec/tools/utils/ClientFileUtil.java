package cn.newcapec.tools.utils;

import cn.newcapec.chat.client.base.ConfigManager;
import cn.newcapec.chat.client.net.s2c.S2CMsg;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by dimm on 2017/2/17.
 */
public class ClientFileUtil {

    private static String clientUploadPath = ConfigManager.instance.getString("client.uploadPath");//客户端待上传文件路径
    private static String clientUploadPathBak = ConfigManager.instance.getString("client.uploadPathBak");//客户端已上传备份路径
    private static String clientDownPath = ConfigManager.instance.getString("client.downPath");//客户端接收路径

    //创建客户端接收文件
    public static void createCFile(String orgCode,String fileName, byte[] content) throws IOException {
        File file = new File(clientDownPath + orgCode + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(clientDownPath + orgCode + "/" + fileName);
        fos.write(content);
        fos.flush();
        fos.close();
    }

    //客户端待上传文件转移至服务端已上传文件备份路径
    public static void copyCFile(String orgCode,String fileName) throws IOException {
        copyFileUsingFileChannels(clientUploadPath, clientUploadPathBak,orgCode + "/", fileName);
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

    //获取客户端文件夹下文件个数
    public static int getCFileCount(String orgCode) throws IOException {
        File file = new File(clientUploadPath + orgCode + "/");
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

    //获取客户端文件下第一个待上传的文件名称
    public static String getCFileFirst(String orgCode) throws IOException {
        File file = new File(clientUploadPath + orgCode + "/");
        String fileName = "";
        if (file != null && file.isDirectory()) {
            File[] fs = file.listFiles();
            if(fs != null && fs.length > 0){
                fileName = fs[0].getName();
            }
        }
        return fileName;
    }

    //获取客户端文件长度
    public static int getCFileLen(String orgCode,String fileName) {
        int fileLen = 0;
        File f = new File(clientUploadPath + orgCode + "/" + fileName);
        if (f.exists() && f.isFile()){
            fileLen = (int)f.length();
        }
        return fileLen;
    }

    //获取客户端文件内容
    public static byte[] getCFileCont(String orgCode,String fileName) {
        byte[] strBuffer = null;
        int  flen = 0;
        File f = new File(clientUploadPath + orgCode + "/" + fileName);
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
     * 验证报文长度
     * @param len1
     * @param len2
     * @param msgid
     * @return
     */
    public static boolean ifBodyLen(int len1, S2CMsg msg){
        boolean bool = false;
        int len2;
        if(msg.msgid == 4004){
            len2 = 36 + msg.cFileInfo.flag.length() + msg.cFileInfo.dataBlock.length;
        }else{
            len2= 36 + StringUtil.toValueLen(msg.toString());
        }
        if(len1 == len2) bool = true;
        return bool;
    }
}