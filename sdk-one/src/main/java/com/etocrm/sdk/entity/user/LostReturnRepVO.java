package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LostReturnRepVO {

    @ExcelProperty(value = "日期", index = 0)
    @ApiModelProperty("日期")
    private String curDate;

    @ExcelProperty(value = "沉默回流用户", index = 1)
    @ApiModelProperty("沉默回流用户")
    private Integer lostNum;

    @ExcelProperty(value = "流失回流用户", index = 2)
    @ApiModelProperty("流失回流用户")
    private Integer returnNum;

}
