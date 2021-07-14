package com.etocrm.sdk.entity.share;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分享日期")
public class DateShareVO {

    @ApiModelProperty("分享日期")
    private String  date;

    @ApiModelProperty("分享人数")
    private Integer  peopleNum;//分享人数

    @ApiModelProperty("分享次数")
    private Integer  shareNum;//分享次数

    @ApiModelProperty(value = "回流量" )
    private Integer  turnback;//回流量

    @ApiModelProperty(value = "分享回流比" )
    private String  turnbackProp;//分享回流比

    @ApiModelProperty(value = "分享新增人数" )
    private Integer  userPlusNum;//分享新增人数

}
