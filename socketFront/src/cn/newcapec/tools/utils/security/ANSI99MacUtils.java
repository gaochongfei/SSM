package cn.newcapec.tools.utils.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.Vector;

/**
 * 关键字：Java MAC计算 X9.9
 * 
 * 为了检查通讯报文是否被篡改，常需要在报文中加上一个MAC（Message Authentication Code，报文校验码）。
 * 
 * 在 JDK 1.4里，已包含一个 Mac 类（javax.crypto.Mac），可以生成MAC。 但它是参照HMAC（Hash-based Message Authentication Code，基于散列的消息验证代码）实现的。 有时，需要采用ANSI-X9.9算法计算MAC。
 * 
 * 1. 算法描述 参与ANSI X9.9 MAC计算的数据主要由三部分产生：初始数据、原始数据、补位数据。 1) 算法定义：采用DEC CBC算法 2) 初始数据：0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 3) 原始数据: 4)
 * 补位数据：若原始数据不是8的倍数,则右补齐0x00；若原始数据位8的整数倍，则不用补齐0x00。 5) 密钥: MAC密钥
 * 
 * MAC的产生由以下方式完成：(最后一组数据长度若不足8的倍数，则右补齐0x00；若数据长度为8的整数倍，则无需补充0x00) 初始数据 BLOCK #1 BLOCK #2 BLOCK #3 ... BLOCK #N | | | | | +-----> XOR +---> XOR +---> XOR +---> XOR | | | | | | | DES
 * ---+ DES ---+ DES ---+ DES ---> MAC | | | | KEY KEY KEY KEY
 * 
 * 2. Java实现 网上用C语言实现的案例很多；但用Java实现的很少（几乎没有）。 下面为本人实现的Java程序。其中，要用到DES加密函数(tEncryptDES)，该函数的说明如下： byte[] tEncryptDES(byte[] tKey, byte[] tBuf) // tKey -- 密钥数据 // tBuf -- 待加密的缓冲区 //
 * 返回 -- 加密后的缓冲区
 */
public class ANSI99MacUtils {
	/**
	 * 采用x9.9算法计算MAC (Count MAC by ANSI-x9.9).
	 * 
	 * @param tKey  密钥数据
	 * @param tBuffer 待计算的缓冲区
	 * @param iOffset 数据的偏移量(0,1,...，即起始位置)
	 * @param iLength  数据的长度(<0 - 默认值，即整个长度)
	 * @throws GeneralSecurityException 
	 */
	public static byte[] tCountMACx9_9(byte[] tKey, byte[] tBuffer, int iOffset, int iLength) throws GeneralSecurityException {
		byte[] tResult = null;
		Vector<byte[]> vctBlk = new Vector<byte[]>();
		byte[] tTmp, tBlk, tXor, tDes;
		int iNum, iLen, iPos, iN, i;

		if (tKey == null || tBuffer == null)
			return tResult;

		if (iOffset < 0)
			iOffset = 0;
		if (iLength < 0)
			iLength = tBuffer.length - iOffset;

		// 拆分数据（8字节块/Block）
		iLen = 0;
		iPos = iOffset;
		while (iLen < iLength && iPos < tBuffer.length) {
			tBlk = new byte[8];
			for (i = 0; i < tBlk.length; i++)
				tBlk[i] = (byte) 0; // clear(0x00)
			for (i = 0; i < tBlk.length && iLen < iLength && iPos < tBuffer.length; i++) {
				tBlk[i] = tBuffer[iPos++];
				iLen++;
			}
			vctBlk.addElement(tBlk); // store (back)
		}

		// 循环计算（XOR + DES）
		tDes = new byte[8]; // 初始数据
		for (i = 0; i < tDes.length; i++)
			tDes[i] = (byte) 0; // clear(0x00)

		iNum = vctBlk.size();
		for (iN = 0; iN < iNum; iN++) {
			tBlk = (byte[]) vctBlk.elementAt(iN);
			if (tBlk == null)
				continue;

			tXor = new byte[Math.min(tDes.length, tBlk.length)];
			for (i = 0; i < tXor.length; i++)
				tXor[i] = (byte) (tDes[i] ^ tBlk[i]); // 异或(Xor)

			tTmp = tEncryptDES(tKey, tXor); // DES加密

			for (i = 0; i < tDes.length; i++)
				tDes[i] = (byte) 0; // clear
			for (i = 0; i < Math.min(tDes.length, tTmp.length); i++)
				tDes[i] = tTmp[i]; // copy / transfer
		}

		vctBlk.removeAllElements(); // clear

		tResult = tDes;

		return tResult;
	}

	public static byte[] tEncryptDES(byte[] key, byte[] datasource) throws GeneralSecurityException {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(datasource);
	}
	/**
	 * des加密算法
	 * @param key
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
//	public static byte[] tEncryptDES(String key, String datasource) throws Exception {
//		return tEncryptDES(key.getBytes("UTF-8"), datasource.getBytes("UTF-8"));
//	}
	
	/**
	 * des解密过程
	 * @param key
	 * @param datasource
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] tDecryptDES(byte[] key, byte[] datasource) throws GeneralSecurityException {
//		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		// 现在，获取数据并解密
		// 正式执行解密操作
//		return cipher.doFinal(datasource);
		return cipher.doFinal(org.bouncycastle.util.encoders.Base64.decode(datasource));
	}
	
	/**
	 * des解密过程
	 * @param key
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
//	public static byte[] tDecryptDES(String key, String datasource) throws Exception {
//		return tDecryptDES(key.getBytes("UTF-8"), datasource.getBytes("UTF-8"));
//	}
	
	/**
	 * des加密算法，ECB方式，NoPadding模式，数据字节必须是8的整数倍
	 * @param key
	 * @param data 数据字节必须是8的整数倍
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptECBNoPadding(byte[] key, byte[] data) throws Exception {   
		
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");       
		DESKeySpec desKeySpec = new DESKeySpec(key); 
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");      
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);      
		
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);         
		
		return cipher.doFinal(data);        
		
	}
	
	/**
	 * des解密算法，ECB方式，NoPadding模式，数据字节必须是8的整数倍
	 * @param key
	 * @param data 数据字节必须是8的整数倍
	 * @return
	 * @throws GeneralSecurityException 
	 * @throws  
	 * @throws Exception
	 */
	public static byte[] decryptECBNoPadding(byte[] key, byte[] data) throws GeneralSecurityException {   
		
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");       
		DESKeySpec desKeySpec = new DESKeySpec(key);      
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");      
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);      
		
		cipher.init(Cipher.DECRYPT_MODE, secretKey);         
		
		return cipher.doFinal(data);        
		
	}
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	 /** 
     * 获得一个UUID 
     * @return String UUID 
     */ 
    public static String createWorkKey(){ 
        String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    } 
	
	/**
Mac的3个结果 ,key="11111111"
1: 
"abcdefgh" -> "11111111" =>
120 115 -19 -88 118 -54 15 -22 
2: 
"abcdefgh1234" -> "11111111" =>
-22 -64 42 94 72 -92 -60 59 
3: 
"abcdefgh12345678ABCD" -> "11111111" =>
67 11 -74 127 -63 -20 5 -45 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		String uuid  = createWorkKey();
        System.out.println(uuid);
        System.out.println(uuid.toString().length());
		
		//byte[] key = "11111111".getBytes();
		byte[] key = uuid.toString().getBytes();
		byte[] data1 = "{data:'测试des算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法算法'}".getBytes();
//		byte[] data2 = "abcdefgh1234".getBytes();
//		byte[] data3 = "abcdefgh12345678ABCD".getBytes();
		
//		byte[] key1 = Hex.decodeHex("533bb8e2caebee89".toCharArray());
//		byte[] data4 = Hex.decodeHex("0320010c0101000000000000000001202020202020202020202020202020202020202000202020202020202000202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020".toCharArray());
////		byte[] byData = Hex.decodeHex(data.toCharArray());
////		byte[] bykey = Hex.decodeHex(key.toCharArray());
//		
//		byte[] byMac1 = tCountMACx9_9(key, data1, 0, data1.length);
//		System.out.println(Arrays.toString(byMac1));
//		byte[] byMac2 = tCountMACx9_9(key, data2, 0, data2.length);
//		System.out.println(Arrays.toString(byMac2));
//		byte[] byMac3 = tCountMACx9_9(key, data3, 0, data3.length);
//		System.out.println(Arrays.toString(byMac3));
//		byte[] byMac4 = tCountMACx9_9(key1, data4, 0, data4.length);
//		System.out.println(Hex.encodeHexString(byMac4));
		
//		tDecryptDES
		
		System.out.println("密钥：11111111，数据：{data:'测试des算法'}");
		System.out.println("加密后 ");
		byte[] enbyte = tEncryptDES(key, data1);
		StringBuffer sb = new StringBuffer();
		sb .append("new byte[]{");
		for(int i=0;i<enbyte.length;i++)
		{
			sb.append(enbyte[i]+",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("};");
		System.out.println(new String(enbyte));
		
		System.out.println("加密后的数据定义");
		System.out.println(sb);
		//byte[] xx = new byte[]{-87,-97,-122,-41,62,-46,67,85,13,104,-75,-110,50,-67,22,112,98,-74,-17,-31,80,19,-75,-21,-83,106,-120,-76,-6,55,-125,61};
		
		
		System.out.println("还原后 ");
		//System.out.println(new String(tDecryptDES(key, xx)));
		System.out.println(new String(tDecryptDES(key, enbyte)));

	}
}
