package com.etocrm.sdk.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticsearchConfig {

    /**
     * 集群地址，多个用,隔开
     **/
    private static String hosts="10.10.6.33,10.10.6.34,10.10.6.35";

    /**
     * 端口号
     **/
    private static int port=9200;

    /**
     * 使用的协议
     **/
    private static String schema="http";

    /**
     * 连接超时时间
     **/
    private static int connectTimeOut=1000;

    /**
     * 连接超时时间
     **/
    private static int socketTimeOut=3000;

    /**
     * 获取连接的超时时间
     **/
    private static int connectionRequestTimeOut=500;

    /**
     * 最大连接数
     **/
    private static int maxConnectNum=50;

    /**
     * 最大路由连接数
     **/
    private static int maxConnectPerRoute=50;


    private List<HttpHost> hostList = new ArrayList<>();

    @PostConstruct
    private void init() {
        hostList = new ArrayList<>();
        String[] hostArray = hosts.split(",");
        for (String host : hostArray) {
            hostList.add(new HttpHost(host, port, schema));
        }
    }
    //此时可以采取两种bean的配置方法，分别代表同步 与异步 操作
    //同步操作
    /**
     @Bean
     public RestClientBuilder restClientBuilder() {
     return RestClient.builder(makeHttpHost());
     }

     @Bean
     public RestClient elasticsearchRestClient(){
     return RestClient.builder(new HttpHost(hosts, port, schema)).build();
     }

     private HttpHost makeHttpHost() {
     return new HttpHost(hosts, port, schema);
     }

     @Bean
     public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder){
     return new RestHighLevelClient(restClientBuilder);
     }
     */

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient getRestHighLevelClient() {

        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

}