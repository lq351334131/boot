package com.etocrm.tag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author qi.li
 * @create 2020/9/14 18:19
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@SpringBootApplication
public class TagApplication {

    public static void main(String[] args) {
        SpringApplication.run(TagApplication.class,args);
    }

}
