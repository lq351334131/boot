package com.etocrm.sdk.entity.funnel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FunnelRepVO {

    private String  id;

    private String funnelName;

    private String beginStepName;

    private String endStepName;

    private String conversionRate;

    private String createTime;

}
