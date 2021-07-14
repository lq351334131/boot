package com.etocrm.sdk.entity.funnel;

import lombok.Data;

@Data
public class FunnelRepVO {

    private String  id;

    private String funnelName;

    private String beginStepName;

    private String endStepName;

    private String conversionRate;

    private String createTime;

}
