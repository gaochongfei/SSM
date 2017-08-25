package cn.newcapec.chat.server.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class SerializeUtil {
	/**
     * 序列化
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
    	byte[] bytes = null;
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            bytes = baos.toByteArray();
        } catch (Exception e) {
//        	Log.error(e);
        } finally{
        	try{
        		if(oos!=null)oos.close();
        		if(baos!=null)baos.close();
        	}catch(Exception e){}
        }
        return bytes;
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
    	Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
//        	Log.error(e);
        } finally{
        	try{
        		if(bais!=null)bais.close();
        		if(ois!=null)ois.close();
        	}catch(Exception e){}
        }
        return obj;
    }

}
