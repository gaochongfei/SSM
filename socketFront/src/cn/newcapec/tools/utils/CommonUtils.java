package cn.newcapec.tools.utils;

import cn.newcapec.tools.model.JsonHead;
import cn.newcapec.tools.utils.security.ANSI99MacUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.bouncycastle.util.encoders.Base64;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 工具类
 * 
 * @author WEIXING
 *
 */
public abstract class CommonUtils {

	/**
	 * 
	 * @Title: stringIsEmpty
	 * @Description: 判断字符串是否为空
	 * @param str
	 * @return
	 * @return: boolean true 空 false 不空
	 */
	public static boolean stringIsEmpty(String str) {
		if (null == str || str.length() == 0 || str.equals("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: getUUID
	 * @Description: 生成随即UUID
	 * @return
	 * @return: String
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装json返回
	 * @param jh
	 * @return
     */
	private static String packageJson(JsonHead jh) {
		String result = JSONObject.toJSONString(jh, SerializerFeature.WriteMapNullValue).toString();
//		long endtime = System.currentTimeMillis();
//		Log.info("整体结束时间："+endtime);
		return result;
	}

	/**
	 *
	 * @Title: packageJson
	 * @Description: 封装json返回
	 * @param object
	 * @param respcode
	 * @param respinfo
	 * @return
	 * @return: Object
	 */
	public static JsonHead packageJson(Object object, String respcode, String respinfo){
		JsonHead jh = new JsonHead();
		jh.setObjson(object);
		if (stringIsEmpty(respcode)) {
			jh.setRespcode("00");
			jh.setRespinfo("success");
		} else {
			jh.setRespcode(respcode);
			jh.setRespinfo(respinfo);
		}
		return jh;
	}

	/**
	 * 封装json返回
	 * @param object
	 * @return
     */
	public static Object packageJson(Object object){
		return packageJson(packageJson(object, null, null));
	}

	/**
	 * 封装json返回
	 * @param object
	 * @param key 加密的密钥
     * @return
     */
	public static Object packageJson(Object object, Object key){
		return packageJson(object, null, null, key);
	}

	/**
	 * 封装json数据返回
	 * @param object
	 * @param respcode
	 * @param respinfo
	 * @param key 加密的密钥
	 * @return
	 * @throws RuntimeException
     */
	public static Object packageJson(Object object, String respcode, String respinfo, Object key) throws RuntimeException {
		if(key == null || "".equals(key.toString().trim())){
			return packageJson(packageJson(object, respcode, respinfo));
		}
		try { // 对返回的数据进行加密
			JsonHead jh = packageJson(object, respcode, respinfo);
			String result = packageJson(jh);
			byte[] resultData = ANSI99MacUtils.tEncryptDES(key.toString().getBytes("UTF-8"), result.getBytes("UTF-8"));
			result = Base64.toBase64String(resultData);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("数据异常");
		}
	}

	private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	/**
	 * 获得短uuid
	 * @return
     */
	public static String getShortUuid() {
		StringBuffer stringBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int strInteger = Integer.parseInt(str, 16);
			stringBuffer.append(chars[strInteger % 0x3E]);
		}

		return stringBuffer.toString();
	}

	/**
	 * Set集合转换成List集合
	 * 
	 * @param sets
	 * @return
	 */
	public static <T> List<T> setToList(Set<T> sets) {
		List<T> list = new ArrayList<T>();
		for (T k : sets) {
			list.add(k);
		}
		return list;
	}

	//十进制
	public static boolean isOctNumber(String str) {
		boolean flag = false;
		for(int i=0,n=str.length();i<n;i++){
			char c = str.charAt(i);
			if(c=='0'|c=='1'|c=='2'|c=='3'|c=='4'|c=='5'|c=='6'|c=='7'|c=='8'|c=='9'){
				flag =true;
			}
		}
		return flag;
	}

	//十六进制
	public static boolean isHexNumber(String str){
		boolean flag = false;
//		for(int i=0;i<str.length();i++){
//			char cc = str.charAt(i);
//			if(cc=='A'||cc=='B'||cc=='C'|| cc=='D'||cc=='E'||cc=='F'||cc=='a'||cc=='b'||cc=='c'||cc=='c'||cc=='d'||cc=='e'||cc=='f'){
//				flag = true;
//				if(cc=='0'||cc=='1'||cc=='2'||cc=='3'||cc=='4'||cc=='5'||cc=='6'||cc=='7'||cc=='8'||cc=='9'){
//					flag = true;
//				}
//			}
//		}
		if(str.length() == 16){
			flag = true;
		}
		return flag;
	}

}
