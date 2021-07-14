package com.etocrm.sdk.entity.funnel;

import lombok.Data;

import java.util.Date;

@Data
public class StepRepVO {

    private String stepName;

    //漏斗步骤的类型，1：页面，2：事件
    private String stepType;

    private String peId;

    private Date createTime;

    private String k;

    private String funnelId;
}
