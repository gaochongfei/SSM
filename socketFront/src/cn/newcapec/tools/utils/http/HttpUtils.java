package cn.newcapec.tools.utils.http;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Set;

/**
 * http请求工具类
 * 
 * @author WEIXING
 *
 */
public abstract class HttpUtils {

	static {
		MyX509TrustManager xtm = new MyX509TrustManager();
		MyHostnameVerifier hnv = new MyHostnameVerifier();
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS"); //或SSL
			X509TrustManager[] xtmArray = new X509TrustManager[] {xtm};
			sslContext.init(null, xtmArray, new java.security.SecureRandom());
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hnv);
	}

	//region GET请求
	/**
	 * get方式处理
	 *
	 * @param url
	 *            请求的url地址
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws IOException
	 */
	public static Object[] doGet(String url) throws Exception {
		return doGet(url, "UTF-8");
	}

	/**
	 * get方式处理
	 * 
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            请求的参数
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws IOException
	 */
	public static Object[] doGet(String url, Map<String, String> params) throws Exception {
		return doGet(url, params, "UTF-8");
	}

	/**
	 * get方式处理
	 * 
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            请求的参数
	 * @param charset
	 *            编码格式
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws IOException
	 */
	public static Object[] doGet(String url, Map<String, String> params, String charset) throws Exception {
		StringBuffer paramsStr = new StringBuffer("");
		if (params != null && params.size() > 0) {
			Set<String> keys = params.keySet();
			int index = 0;
			for (String key : keys) {
				String value = params.get(key);
				if (value == null || "".equals(value.trim())) {
					continue;
				}
				if (index++ > 0) {
					paramsStr.append("&");
				}
				paramsStr.append(key + "=" + params.get(key));
			}
		}
		if (url.indexOf("?") == -1) {
			url += "?";
		}
		return doGet(url + paramsStr.toString(), charset);
	}

	/**
	 * get方式处理
	 * 
	 * @param url
	 *            请求的url地址
	 * @param charset
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws IOException
	 */
	public static Object[] doGet(String url, String charset) throws Exception {
		Object[] result = new Object[2];
		Object[] postResult;
		InputStream inputStream;
		if(url.startsWith("https")){
			postResult = doHttpsGet(url);
		}else{
			postResult = doHttpGet(url);
		}
		result[0] = postResult[0];
		inputStream = (InputStream) postResult[1];
		if (inputStream == null) {
			result[1] = "";
		} else {
			// 获取应答内容
			String resContent = new String(InputStreamToByte(inputStream), charset);
			// 关闭输入流
			inputStream.close();
			result[1] = resContent;
		}
		return result;
	}
	//endregion

	//region POST请求
	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            POST请求的参数
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws Exception
	 */
	public static Object[] doPost(String url, Map<String, String> params) throws Exception {
		return doPost(url, params, "UTF-8");
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            POST请求的参数
	 * @param charset
	 *            编码格式
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws Exception
	 */
	public static Object[] doPost(String url, Map<String, String> params, String charset) throws Exception {
		return doPost(url, mapToUrl(params), charset);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            POST请求的参数
	 * @return
	 * @throws Exception
	 */
	public static Object[] doPost(String url, String params) throws Exception {
		return doPost(url, params, "UTF-8");
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            POST请求的参数
	 * @param charset
	 *            编码格式
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws Exception
	 */
	public static Object[] doPost(String url, String params, String charset) throws Exception {
		return doPost(url, params.getBytes("UTF-8"), charset);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            请求的url地址
	 * @param data
	 *            POST请求的参数
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws Exception
	 */
	public static Object[] doPost(String url, byte[] data) throws Exception {
		return doPost(url, data, "UTF-8");
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            请求的url地址
	 * @return
	 * @throws Exception
	 */
	public static Object[] doPost(String url) throws Exception {
		return doPost(url, new byte[] {});
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            请求的url地址
	 * @param data
	 *            POST请求的参数
	 * @param charset
	 *            编码格式
	 * @return 请求结果 是一个Object数组 [responseCode(int), resContent(String)]
	 * @throws Exception
	 */
	public static Object[] doPost(String url, byte[] data, String charset) throws Exception {
		Object[] result = new Object[2];
		Object[] postResult;
		InputStream inputStream;
		if(url.startsWith("https")){
			postResult = doHttpsPost(url, data);
		}else if(url.startsWith("multipart_")){
			url = url.substring("multipart_".length());
			postResult = doHttpPostMultipart(url, data);
		}else{
			postResult = doHttpPost(url, data);
		}
		result[0] = postResult[0];
		inputStream = (InputStream) postResult[1];
		if (inputStream == null) {
			result[1] = "";
		} else {
			// 获取应答内容
			String resContent = new String(InputStreamToByte(inputStream), charset);
			// 关闭输入流
			inputStream.close();
			result[1] = resContent;
		}
		return result;
	}
	//endregion

	//region 通用工具
	public static String mapToUrl(Map<String, String> params){
		StringBuffer ps = new StringBuffer();
		if (params != null && params.size() > 0) {
			Set<String> keys = params.keySet();
			int index = 0;
			for (String key : keys) {
				if (index > 0) {
					ps.append("&");
				}
				ps.append(key + "=" + params.get(key));
				index++;
			}
		}
		return ps.toString();
	}
	//endregion

	//region 网络请求的私有函数
	/**
	 * InputStream转换成Byte 注意:流关闭需要自行处理
	 *
	 * @param in
	 * @return byte
	 * @throws Exception
	 */
	private static byte[] InputStreamToByte(InputStream in) throws IOException {
		int BUFFER_SIZE = 4096;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
			outStream.write(data, 0, count);
		}
		data = null;
		byte[] outByte = outStream.toByteArray();
		outStream.close();
		return outByte;
	}

	//发送http的POST请求
	private static Object[] doHttpPost(String url, byte[] data) throws Exception {
		Object[] result = new Object[2];
		HttpURLConnection conn = (HttpURLConnection)(new URL(url).openConnection());
		// 以post方式通信
		conn.setRequestMethod("POST");
		setHttpRequest(conn); // 设置请求默认属性
		// Content-Type
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
		if (data != null && data.length > 0) {
			doOutput(out, data); // 处理输出
		}
		// 关闭流
		out.close();
		// 获取响应返回状态码
		int responseCode = conn.getResponseCode();
		result[0] = responseCode;
		// 获取应答输入流
		result[1] = conn.getInputStream();
		return result;
	}

	//发送https的POST请求
	private static Object[] doHttpsPost(String url, byte[] data) throws Exception {
		Object[] result = new Object[2];
		HttpsURLConnection conn = (HttpsURLConnection)(new URL(url).openConnection());
		// 以post方式通信
		conn.setRequestMethod("POST");
		setHttpRequest(conn); // 设置请求默认属性
		// Content-Type
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
		if (data != null && data.length > 0) {
			doOutput(out, data); // 处理输出
		}
		// 关闭流
		out.close();
		// 获取响应返回状态码
		int responseCode = conn.getResponseCode();
		result[0] = responseCode;
		// 获取应答输入流
		result[1] = conn.getInputStream();
		return result;
	}
	//发送http的POST请求
	private static Object[] doHttpPostMultipart(String url, byte[] data) throws Exception {
		Object[] result = new Object[2];
		HttpURLConnection conn = (HttpURLConnection)(new URL(url).openConnection());
		String boundary ="*****";
		String end ="\r\n";
		String twoHyphens ="--";
		/* 允许Input、Output，不使用Cache */
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
			 /* 设置传送的method=POST */
		conn.setRequestMethod("POST");
			/* setRequestProperty */
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		setHttpRequest(conn); // 设置请求默认属性
		BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
		if (data != null && data.length > 0) {
			doOutput(out, data); // 处理输出
		}
		// 关闭流
		out.close();
		// 获取响应返回状态码
		int responseCode = conn.getResponseCode();
		result[0] = responseCode;
		// 获取应答输入流
		result[1] = conn.getInputStream();
		return result;
	}
	//发送http的GET请求
	private static Object[] doHttpGet(String url) throws Exception{
		Object[] result = new Object[2];
		HttpURLConnection conn = (HttpURLConnection)(new URL(url).openConnection());
		// 以GET方式通信
		conn.setRequestMethod("GET");
		// 设置请求默认属性
		setHttpRequest(conn);
		// 获取响应返回状态码
		int responseCode = conn.getResponseCode();
		result[0] = responseCode;
		// 获取应答输入流
		result[1] = conn.getInputStream();
		return result;
	}

	//发送https的GET请求
	private static Object[] doHttpsGet(String url) throws Exception{
		Object[] result = new Object[2];
		HttpsURLConnection conn = (HttpsURLConnection)(new URL(url).openConnection());
		// 以GET方式通信
		conn.setRequestMethod("GET");
		// 设置请求默认属性
		setHttpRequest(conn);
		// 获取响应返回状态码
		int responseCode = conn.getResponseCode();
		result[0] = responseCode;
		// 获取应答输入流
		result[1] = conn.getInputStream();
		return result;
	}

	// 设置请求默认属性
	private static void setHttpRequest(HttpURLConnection conn) {
		// 设置请求默认属性//设置连接超时时间
		conn.setConnectTimeout(30 * 1000);
		final String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)";
		// User-Agent
		conn.setRequestProperty("User-Agent", USER_AGENT_VALUE);
		// 不使用缓存
		conn.setUseCaches(false);
		// 允许输入输出
		conn.setDoInput(true);
		conn.setDoOutput(true);
	}

	/** 处理输出 (流关闭请自己处理) */
	private static void doOutput(BufferedOutputStream out, byte[] data) throws IOException {
		final int len = 1024; // 1KB
		int dataLen = data.length;
		int off = 0;
		while (off < data.length) {
			if (len >= dataLen) {
				out.write(data, off, dataLen);
				off += dataLen;
			} else {
				out.write(data, off, len);
				off += len;
				dataLen -= len;
			}
			// 刷新缓冲区
			out.flush();
		}
	}
	//endregion
}
