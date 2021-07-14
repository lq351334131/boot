package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserActiveRepVO {

    @ExcelProperty(value = "日期", index = 0)
    private String curDate;

    @ExcelProperty(value = "DAU", index = 1)
    private Long dau;

    @ExcelProperty(value = "mau", index = 2)
    private Long mau;

    @ExcelProperty(value = "wau", index = 3)
    private Long wau;

    @ExcelProperty(value = "DAU/WAU", index = 4)
    private String dwRate;
    @ExcelProperty(value = "DAU/MAU", index = 5)
    private String dmRate;


}
