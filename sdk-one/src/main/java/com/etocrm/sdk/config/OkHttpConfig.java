package com.etocrm.sdk.config;


import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author qi.li
 * @create 2020/11/20 16:12
 */
@Configuration
public class OkHttpConfig {

    @Autowired
    private ConnectionPool pool;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                //.sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .retryOnConnectionFailure(false)
                .connectionPool(pool)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

//    @Bean
//    public X509TrustManager x509TrustManager() {
//        return new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[0];
//            }
//        };
//    }

    /**
     * Create a new connection pool with tuning parameters appropriate for a single-user application.
     * The tuning parameters in this pool are subject to change in future OkHttp releases. Currently
     */
    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(200, 5, TimeUnit.MINUTES);
    }



}

