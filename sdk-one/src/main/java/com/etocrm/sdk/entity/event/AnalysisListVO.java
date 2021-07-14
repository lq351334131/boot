package com.etocrm.sdk.entity.event;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AnalysisListVO {

    @ExcelProperty(value = "事件key", index = 0)
    private String eventKey;

    @ExcelProperty(value = "事件名称", index = 1)
    private String eventName;

    @ExcelProperty(value = "事件创建时间", index = 2)
    private String createTime;

    @ExcelProperty(value = "用户数", index = 3)
    private Integer userNum;

    @ExcelProperty(value = "次数", index = 4)
    private Integer eventNum;

    @ExcelProperty(value = "人均次数", index = 5)
    private Integer avgEventNum;
}
