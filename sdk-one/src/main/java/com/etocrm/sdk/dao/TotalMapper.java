package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.total.ToTalPO;

import java.util.List;
import java.util.Map;

public interface TotalMapper {

    List<Map<String,Object>> getTotal(Map<String, Object> map);

    List<Map<String,Object>> getOpenNum(Map<String, Object> map);

    Long getTime(Map<String, Object> map);

    List<Map<String,Object>> getBounceRate(Map<String, Object> map);

    Integer getUv(Map<String, Object> map);

    List<Map<String,Object>> getNewUser(Map<String, Object> map);

    List<Map<String,Object>> getUserShare(Map<String, Object> map);

    List<Map<String,Object>> getActive(Map<String, Object> map);

    List<Map<String,Object>> getNewmember(Map<String, Object> map);

    List<ToTalPO> getTotalAll(Map<String, Object> map);

    List<ToTalPO> getTotalDateAll(Map<String, Object> map);

    List<Map<String,Object>> getBounceDateRate(Map<String, Object> map);

    List<Map<String,Object>> getNewUserDate(Map<String, Object> map);

    Integer getDateActive(Map<String, Object> map);





}
