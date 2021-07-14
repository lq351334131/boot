package com.etocrm.sdk.entity.user;

import lombok.Data;

@Data
public class SlientLostVO {


    private String appKey;
    private String beginDate;
    private String thirtyDay;//30
    private String ninetyDay;//90

    private Integer typeId;

    private Integer pageSize=10;

    private Integer pageIndex=1;



}
