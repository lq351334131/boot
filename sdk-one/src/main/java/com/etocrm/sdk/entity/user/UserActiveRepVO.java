package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserActiveRepVO {

    @ExcelProperty(value = "日期", index = 0)
    @ApiModelProperty( "日期")
    private String curDate;

    @ExcelProperty(value = "DAU", index = 1)
    @ApiModelProperty("dau")
    private Integer dau;

    @ExcelProperty(value = "mau", index = 2)
    @ApiModelProperty("mau")
    private Integer mau;

    @ExcelProperty(value = "wau", index = 3)
    @ApiModelProperty("wau")
    private Integer wau;

    @ExcelProperty(value = "DAU/WAU", index = 4)
    @ApiModelProperty("DAU/WAU")
    private String dwRate;

    @ExcelProperty(value = "DAU/MAU", index = 5)
    @ApiModelProperty("DAU/MAU")
    private String dmRate;


}
