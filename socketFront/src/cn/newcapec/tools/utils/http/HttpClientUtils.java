package cn.newcapec.tools.utils.http;

import cn.newcapec.tools.utils.Log;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * httpClient通用工具类
 * Created by es on 2016/9/24.
 */
public class HttpClientUtils {

    /**
     * 上传文件
     *
     * @param url  请求的url地址
     * @param file 要上传的文件
     * @return
     */
    public static Object[] doUploadFile(String url, File file) {
        return doUploadFile(url, file, null);
    }

    /**
     * 上传文件
     *
     * @param url    请求的url地址
     * @param file   要上传的文件
     * @param params 提交的参数
     * @return
     */
    public static Object[] doUploadFile(String url, File file, Map<String, String> params) {
        return doUploadFile(url, new File[]{file}, params);
    }

    /**
     * 批量上传文件
     *
     * @param url   请求的url地址
     * @param files 要批量上传的文件
     * @return
     */
    public static Object[] doUploadFile(String url, File[] files) {
        return doUploadFile(url, files, null);
    }

    /**
     * 批量上传文件
     *
     * @param url    请求的url地址
     * @param files  要批量上传的文件
     * @param params 提交的参数
     * @return
     */
    public static Object[] doUploadFile(String url, File[] files, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                entity.addPart("file", new FileBody(files[i]));
            }
        }
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                try {
                    entity.addPart(key, new StringBody(params.get(key), Charset.forName("UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        httpPost.setEntity(entity);
        return doRequest(httpPost);
    }

    /**
     * 发送get请求
     *
     * @param url 请求的url地址
     * @return
     */
    public static Object[] doGet(String url) {
        return doGet(url, null);
    }

    /**
     * 发送get请求
     *
     * @param url    请求的url地址
     * @param params 提交的参数
     * @return
     */
    public static Object[] doGet(String url, Map<String, String> params) {
        if (params != null && params.size() > 0) {
            if (url.indexOf("?") == -1) {
                url += "?";
            }
            url += mapToUrl(params);
        }
        HttpGet httpGet = new HttpGet(url);
        return doRequest(httpGet);
    }

    /**
     * 进行POST请求
     *
     * @param url 请求的url地址
     * @return
     */
    public static Object[] doPost(String url) {
        return doPost(url, null);
    }

    /**
     * 进行POST请求
     *
     * @param url    请求的url地址
     * @param params 请求的参数
     * @return
     */
    public static Object[] doPost(String url, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> list = mapToNameValuePair(params);
            httpPost.setEntity(new UrlEncodedFormEntity(list, Charset.forName("utf-8")));
        }
        return doRequest(httpPost);
    }



    /**
     * map转url参数
     *
     * @param params
     * @return
     */
    public static String mapToUrl(Map<String, String> params) {
        StringBuffer ps = new StringBuffer();
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            int index = 0;
            for (String key : keys) {
                if (index > 0) {
                    ps.append("&");
                }
                try {
                    ps.append(key + "=" + URLEncoder.encode(params.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                index++;
            }
        }
        return ps.toString();
    }

    private static List<NameValuePair> mapToNameValuePair(Map<String, String> params) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                try {
                    list.add(new BasicNameValuePair(key, URLEncoder.encode(params.get(key), "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    //发送Http请求，获取数据
    private static Object[] doRequest(HttpUriRequest request) {
        Object[] results = new Object[2];
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            client = HttpClients.createDefault();
            response = client.execute(request);
            //获得应答码
            int statusCode = response.getStatusLine().getStatusCode();
            results[0] = statusCode;
            // 获取应答内容
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();
            String resContent = new String(inputStreamToByte(is), "utf-8");
            results[1] = resContent;
        } catch (IOException e) {
            results[0] = 0;
            results[1] = e;
           // Log.error(e);
        } finally {
            close(client, response, is);
        }
        return results;
    }

    //关闭请求流
    private static void close(Closeable... closeables) {
        Closeable closeable = null;
        for (int i = 0; i < closeables.length; i++) {
            closeable = closeables[i];
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
        }
    }

    /**
     * InputStream转换成Byte 注意:流关闭需要自行处理
     *
     * @param in
     * @return byte
     * @throws Exception
     */
    private static byte[] inputStreamToByte(InputStream in) throws IOException {
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

}
