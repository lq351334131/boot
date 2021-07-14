package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageChannelListVO {


    @ApiModelProperty(value = "页面参数id" )
    private  String  id;

    @ApiModelProperty(value = "参数名称" )
    private  String  name;

    @ApiModelProperty(value = "参数key,value" )
    private String  params;

    @ApiModelProperty(value = "页面id" )
    private  String  pathId;

}
