package com.etocrm.sdk.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author qi.li
 * @create 2020/11/20 16:14
 */
@Component
@Slf4j
public class OkHttpUtil {

    private static OkHttpClient okHttpClient;

    @Autowired
    public OkHttpUtil(OkHttpClient okHttpClient) {
        OkHttpUtil.okHttpClient = okHttpClient;
    }

    private static StringBuilder returnSb(String value, Map<String, String> queries) {
        StringBuilder sb = new StringBuilder(value);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        return sb;
    }

    /**
     * get 临时只针对IP解析第三方调用，写死了Authorization参数
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public static String get(String url, Map<String, String> queries) {
        StringBuilder sb = returnSb(url, queries);
        Request request = new Request.Builder()
                .addHeader("Authorization", "APPCODE a76393221ebc40bd81bc6ebf0971e2e4")
                .url(sb.toString())
                .build();
        return getString(request);
    }

    private static String getString(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        return getString(request);
    }

    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public static String getForHeader(String url, Map<String, String> queries) {
        StringBuilder sb = returnSb(url, queries);
        Request request = new Request.Builder()
                .addHeader("key", "value")
                .url(sb.toString())
                .build();
        return getString(request);
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String postJsonParams(String url, String jsonParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return getString(request);
    }

    /**
     * Post请求发送xml数据....
     * 参数一：请求Url
     * 参数二：请求的xmlString
     * 参数三：请求回调
     */
    public static String postXmlParams(String url, String xml) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return getString(request);
    }

}
