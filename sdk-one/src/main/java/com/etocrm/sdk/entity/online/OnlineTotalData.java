package com.etocrm.sdk.entity.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OnlineTotalData {

    @ApiModelProperty("次数")
    private Integer pv;

    @ApiModelProperty("人数")
    private Integer uv;
}
