package cn.newcapec.tools.utils.http;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by es on 2016/8/8.
 */
public class MyX509TrustManager implements X509TrustManager {
    
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//        Log.info("cert: " + chain[0].toString() + ", authType: " + authType);
    }

    
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
