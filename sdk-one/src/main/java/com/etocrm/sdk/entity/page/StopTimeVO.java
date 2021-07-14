package com.etocrm.sdk.entity.page;

import lombok.Data;

import java.util.List;

@Data
public class StopTimeVO {

    private  String    appKey;
/*
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

    private String p;

    private String beginDate;

    private String endDate;

    private String path;//入口页

    private List<String> plist;

    private List<String> pathlist;



}
