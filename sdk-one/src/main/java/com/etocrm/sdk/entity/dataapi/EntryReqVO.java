package com.etocrm.sdk.entity.dataapi;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "入口")
public class EntryReqVO {

    @ApiModelProperty("入口页")
    private Integer entryNum;

    private String appKey;

    @ApiModelProperty("次数")
    private Integer pv;

    @ApiModelProperty("人数")
    private Integer uv;
}
