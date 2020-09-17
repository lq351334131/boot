package com.etocrm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author qi.li
 * @create 2020/9/15 15:44
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
public class GatewayApplication {
    public static void main(String[] args) {
        try{
            SpringApplication.run(GatewayApplication.class, args);
        }catch(Exception e){
            e.printStackTrace();

        }

    }
}
