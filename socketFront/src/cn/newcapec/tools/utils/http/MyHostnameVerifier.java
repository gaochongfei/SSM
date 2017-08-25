package cn.newcapec.tools.utils.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by es on 2016/8/8.
 */
public class MyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
//        Log.info("Warning: URL Host: " + hostname + " vs. " + session.getPeerHost());
        return true;
    }
}
