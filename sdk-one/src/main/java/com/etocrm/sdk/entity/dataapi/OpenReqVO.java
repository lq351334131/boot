package com.etocrm.sdk.entity.dataapi;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "打开汇总")
public class OpenReqVO {

    @ApiModelProperty("打开次数")
    private Integer openNum;

    private String appKey;
}
