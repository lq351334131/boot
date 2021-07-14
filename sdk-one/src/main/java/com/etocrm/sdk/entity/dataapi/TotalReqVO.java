package com.etocrm.sdk.entity.dataapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TotalReqVO {

    @ApiModelProperty("次数")
    private Integer pv;

    @ApiModelProperty("人数")
    private Integer uv;

    private String appKey;
}
