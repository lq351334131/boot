package com.etocrm.sdk.entity.campaign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2021/6/8 11:38
 */
@ApiModel(value = "获取pu/uv返回数据")
@Data
public class CampaignReturnVO implements Serializable {

    @ApiModelProperty(value = "参数名")
    private Integer paramKey;

    @ApiModelProperty(value = "参数值")
    private Integer paramValue;

    @ApiModelProperty(value = "pv")
    private Integer pv;

    @ApiModelProperty(value = "uv")
    private Integer uv;

}
