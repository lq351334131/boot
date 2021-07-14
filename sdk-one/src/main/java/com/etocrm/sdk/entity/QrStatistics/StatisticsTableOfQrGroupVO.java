package com.etocrm.sdk.entity.QrStatistics;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticsTableOfQrGroupVO {

    @ExcelProperty(index = 0,value = "id")
    private String id;

    @ExcelProperty(index = 1,value = "分组名称")
    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ExcelProperty(index = 2,value = "创建时间")
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ExcelProperty(index = 3,value = "访问次数")
    @ApiModelProperty(value = "访问次数")
    private Integer visitNum;

    @ExcelProperty(index = 4,value = "访问人数")
    @ApiModelProperty(value = "访问人数")
    private Integer  userNum;

    @ExcelProperty(index = 5,value = "访问新人数")
    @ApiModelProperty(value = "访问新人数")
    private Integer  newVisit;

}
