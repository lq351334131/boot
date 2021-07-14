package com.etocrm.sdk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableHystrix
@MapperScan(basePackages = {"com.etocrm.sdk.dao"})
@EnableScheduling
public class ClickhouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClickhouseApplication.class,args);
    }
}
