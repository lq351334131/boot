package com.etocrm.sdk.entity.QrStatistics;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticsTableOfQrCodeVO {

    @ExcelProperty(index = 0,value = "二维码名称")
    @ApiModelProperty(value = "二维码名称")
    private String qrName;

    @ExcelProperty(index = 1,value = "二维码路径")
    @ApiModelProperty(value = "二维码路径")
    private String qrURL;

    @ExcelProperty(index = 2,value = "分组名称")
    @ApiModelProperty(value = "分组名称")
    private String qrGroupName;

    @ExcelProperty(index = 3,value = "创建时间")
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ExcelProperty(index = 4,value = "访问次数")
    @ApiModelProperty(value = "访问次数")
    private Integer visitNum;

    @ExcelProperty(index = 5,value = "访问人数")
    @ApiModelProperty(value = "访问人数")
    private Integer  userNum;

    @ExcelProperty(index = 6,value = "访问新人数")
    @ApiModelProperty(value = "访问新人数")
    private Integer  newVisit;

}
