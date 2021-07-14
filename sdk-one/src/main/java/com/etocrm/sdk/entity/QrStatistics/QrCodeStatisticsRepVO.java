package com.etocrm.sdk.entity.QrStatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodeStatisticsRepVO {

    @ApiModelProperty(value = "扫码次数")
    private Integer scanQrNum;

    @ApiModelProperty(value = "访问总次数")
    private Integer visitNum;

    @ApiModelProperty(value = "扫码人数")
    private Integer  userNum;

    @ApiModelProperty(value = "新用户")
    private Integer  newVisitNum;
}
