package com.etocrm.sdk.entity.QrStatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodeDateRepVO {

    @ApiModelProperty(value = "日期")
    private String dateTime;

    @ApiModelProperty(value = "访问次数")
    private Integer visitNum;

    @ApiModelProperty(value = "访问人数")
    private Integer  userNum;

    @ApiModelProperty(value = "新用户数")
    private Integer  newVisit;

}