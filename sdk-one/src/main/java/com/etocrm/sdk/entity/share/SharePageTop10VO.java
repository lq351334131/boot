package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SharePageTop10VO {

    @ApiModelProperty("页面ID")
    private  String id;//页面ID

    @ApiModelProperty("页面url")
    private String url;//页面url

    @ApiModelProperty("页面名称")
    private String name;//页面名称

    //分享人数总和
    @ApiModelProperty("分享人数总和")
    private Integer peopleNumTotal;

    //分享次数总和
    @ApiModelProperty("分享次数总和")
    private Integer shareNumTotal;

    //回流量总和
    @ApiModelProperty("回流量总和")
    private Integer  turnbackTotal;

    //分享回流比总和
    @ApiModelProperty("分享回流比总和")
    private String  turnbackPropTotal;

    //分享新增人数总和
    @ApiModelProperty("分享新增人数总和")
    private Integer  userPlusNumTotal;

}
