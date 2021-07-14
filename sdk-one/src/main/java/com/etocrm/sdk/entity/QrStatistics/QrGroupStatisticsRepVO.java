package com.etocrm.sdk.entity.QrStatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrGroupStatisticsRepVO {


    @ApiModelProperty(value = "扫码总计")
    private Integer scanQrGroupNum;

    @ApiModelProperty(value = "访问总次数")
    private Integer visitNum;

    @ApiModelProperty(value = "用户总计")
    private Integer  userNum;

    @ApiModelProperty(value = "新用户总计")
    private Integer  newVisitNum;
}
