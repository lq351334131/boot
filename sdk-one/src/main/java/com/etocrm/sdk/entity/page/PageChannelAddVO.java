package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
@ApiModel("新增页面参数")
public class PageChannelAddVO{


    @ApiModelProperty(value = "页面参数名称",required = true )
    @NotNull
    private  String  name;

    @NotNull
    @ApiModelProperty(value = "页面参数",required = true )
    private List<ChannelVO> list;

    @NotNull
    @ApiModelProperty(value = "页面管理id",required = true)
    private  String pathId;



}