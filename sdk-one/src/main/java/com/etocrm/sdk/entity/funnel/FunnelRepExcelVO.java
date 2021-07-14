package com.etocrm.sdk.entity.funnel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class FunnelRepExcelVO {

    @ExcelProperty(value = "漏斗名称", index = 0)
    private String funnelName;

    @ExcelProperty(value = "创建时间", index = 1)
    private String createTime;

    @ExcelProperty(value = "初始页面", index = 2)
    private String beginStepName;

    @ExcelProperty(value = "目标页面", index = 3)
    private String endStepName;

    @ExcelProperty(value = "最终转化率", index = 4)
    private String conversionRate;
}


