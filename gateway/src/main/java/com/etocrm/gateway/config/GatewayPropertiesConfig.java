package com.etocrm.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author qi.li
 * @create 2020/9/15 15:09
 */
@Data
@Component
@ConfigurationProperties(prefix = "gateway")
public class GatewayPropertiesConfig {

    private List<String> ignoreUrl;

}
