package com.etocrm.gateway.entity;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author qi.li
 * @date 2020/9/15 14:50
 */
@Data
public class FilterEntity {

    //过滤器对应的Name
    private String name;

    //路由规则
    private Map<String, String> args = new LinkedHashMap<>();

}
