package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
@ApiModel("页面参数")
public class PageChannelEditVO {

    //private  String  appKey;

    @ApiModelProperty(value = "Id",required = true )
    private String  id;

    //@NotNull

    @ApiModelProperty(value = "页面参数名称", required = true)
    private  String  name;

    @NotNull
    private List<ChannelVO> list;

    @NotNull
    @ApiModelProperty(value = "页面管理id",required = true)
    private  String pathId;



}
