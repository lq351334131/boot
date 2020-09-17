package com.etocrm.sdk.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Event {

    @ExcelProperty(value = "事件Key", index = 0)
    private String eventKey;
    @ExcelProperty(value = "事件名称", index = 1)
    private String eventName;
    @ExcelProperty(value = "创建时间", index = 2)
    private String createTime;

}
