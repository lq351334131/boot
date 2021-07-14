package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class LostReturnRepVO {

    @ExcelProperty(value = "日期", index = 0)
    private String curDate;

    @ExcelProperty(value = "沉默回流用户", index = 1)
    private Long lostNum;

    @ExcelProperty(value = "流失回流用户", index = 2)
    private Long returnNum;

}
