package com.wrbug.wkcwallet.util;


import okhttp3.OkHttpClient;

import javax.net.ssl.*;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class OkhttpUtils {
    private static Proxy proxy;
    private static OkHttpClient sOkHttpClient;

    public static void initProxy(String ip, int port) {
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    }

    public static void stopProxy() {
        proxy = null;
    }

    public static OkHttpClient getClient() {
        if (sOkHttpClient == null) {
            sOkHttpClient = getClientFollowRedirects(true);
        }
        return sOkHttpClient;
    }

    public static OkHttpClient getClientFollowRedirects(boolean follow) {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        mBuilder.followRedirects(follow).followSslRedirects(follow);
        mBuilder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        return getClient(mBuilder);
    }

    public static OkHttpClient getClient(OkHttpClient.Builder builder) {
        if (proxy != null) {
            builder.proxy(proxy);
        }
        return builder.build();
    }
}
