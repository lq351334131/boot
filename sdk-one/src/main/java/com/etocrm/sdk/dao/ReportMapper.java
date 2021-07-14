package com.etocrm.sdk.dao;

import java.util.List;
import java.util.Map;

public interface ReportMapper {
    //Notes	WK
    //1、会员总数
    //2、周新增乐高会员数
    Long getMember(Map<String, Object> map);

    List<Map<String,Object>> getMemberByDay(Map<String, Object> map);

}
