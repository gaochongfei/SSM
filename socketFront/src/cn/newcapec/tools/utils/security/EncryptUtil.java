package cn.newcapec.tools.utils.security;

import java.security.MessageDigest;

/**
 * 加密工具类
 * 
 * @author Administrator
 *
 */
public abstract class EncryptUtil {

	/**
	 * 32位MD5加密
	 *
	 * @param origin
	 * @return
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
           String m= md5(origin);
			md.update(resultString.getBytes("UTF-8"));
			resultString = byteArrayToHexString(md.digest());
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * 转换字节数组为16进制字串
	 *
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (byte aB : b) {
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };


	private final static char[] hexDigits1 = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {
			t = bytes[i];
			if (t < 0)
				t += 256;
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}
	public static String md5(String input) throws Exception {
		return code(input, 32);
	}
	public static String code(String input, int bit) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance(System.getProperty(
					"MD5.algorithm", "MD5"));
			if (bit == 16)
				return bytesToHex(md.digest(input.getBytes("utf-8")))
						.substring(8, 24);
			return bytesToHex(md.digest(input.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Could not found MD5 algorithm.", e);
		}
	}
	public static String md5_3(String b) throws Exception{
		MessageDigest md = MessageDigest.getInstance(System.getProperty(
				"MD5.algorithm", "MD5"));
		byte[] a = md.digest(b.getBytes());
		a = md.digest(a);
		a = md.digest(a);

		return bytesToHex(a);
	}

}
