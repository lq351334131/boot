package com.etocrm.sdk.entity.dataapi;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "实时API")
public class DataApiReqVO {

    @ApiModelProperty("次数")
    private Integer pv;

    @ApiModelProperty("人数")
    private Integer uv;

    @ApiModelProperty("url")
    private String visitpath;

    @ApiModelProperty("appKey")
    private String appKey;

}
