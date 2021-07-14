package com.etocrm.sdk.entity;

import lombok.Data;

@Data
public class YestDayRepVO {

    private String  name;

    //用户数
    private Long userNum;

    //新用户数
    private Long newUserNum;

    //次数
    private Long  visitNum;

    //打开次数
    private Long openNum;

    private String  avgStopTime;

    private  String exitRate;
}
