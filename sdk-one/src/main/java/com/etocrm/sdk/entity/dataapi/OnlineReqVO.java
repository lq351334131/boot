package com.etocrm.sdk.entity.dataapi;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "门店")
public class OnlineReqVO {

    @ApiModelProperty("访问次数")
    private Integer pv;

    @ApiModelProperty("访问人数")
    private Integer uv;

    @ApiModelProperty("门店名称")
    private String name;

    private String appKey;


}
