package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "页面参数")
public class ChannelVO {

    @ApiModelProperty(value = "页面参数key" ,required = true)
    private String  key;

    @ApiModelProperty(value = "页面参数value",required = true )
    private String  value;
}
