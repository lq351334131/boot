package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SystemVO {

    @ExcelProperty(value = "操作系统", index = 0)
    @ApiModelProperty( "操作系统")
    private String clientPlat;

    @ExcelProperty(value = "新用户数", index = 1)
    @ApiModelProperty( "新用户数")
    private Integer newUserNum;

    @ExcelProperty(value = "访问人数", index = 2)
    @ApiModelProperty( "访问人数")
    private Integer userNum;

    @ExcelProperty(value = "访问次数", index = 3)
    @ApiModelProperty( "访问次数")
    private Integer visitNum;

    @ExcelProperty(value = "打开次数", index = 4)
    @ApiModelProperty( "打开次数")
    private Integer openNum;

    @ExcelProperty(value = "次均停留时长", index = 5)
    @ApiModelProperty( "次均停留时长")
    private String avgStopTime;

    @ExcelProperty(value = "跳出率", index = 6)
    @ApiModelProperty( "跳出率")
    private String exitRate;

}
