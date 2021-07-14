package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserNewAddRepVO {

    @ExcelProperty(value = "日期", index = 0)
    @ApiModelProperty(value = "日期" )
    private String curDate;

    @ExcelProperty(value = "新用户数", index = 1)
    @ApiModelProperty(value = "新用户数" )
    private Long count;

    @ExcelProperty(value = "1天以后", index = 2)
    @ApiModelProperty(value = "1天以后" )
    private String oneLater;

    @ExcelProperty(value = "2天以后", index = 3)
    @ApiModelProperty(value = "2天以后" )
    private String twoLater;

    @ExcelProperty(value = "3天以后", index = 4)
    @ApiModelProperty(value = "3天以后" )
    private String threeLater;

    @ExcelProperty(value = "4天以后", index = 5)
    @ApiModelProperty(value = "4天以后" )

    private String fourLater;

    @ExcelProperty(value = "5天以后", index = 6)
    @ApiModelProperty(value = "5天以后" )
    private String fiveLater;

    @ExcelProperty(value = "6天以后", index = 7)
    @ApiModelProperty(value = "6天以后" )
    private String sixLater;

    @ExcelProperty(value = "7天以后", index = 8)
    @ApiModelProperty(value = "7天以后" )
    private String sevenLater;

    @ExcelProperty(value = "14天以后", index = 9)
    @ApiModelProperty(value = "14天以后" )
    private String fourteenLater;

    @ExcelProperty(value = "30天以后", index = 10)
    @ApiModelProperty(value = "30天以后" )
    private String thirtyLater;


}
