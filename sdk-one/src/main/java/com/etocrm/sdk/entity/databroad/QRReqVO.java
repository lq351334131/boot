package com.etocrm.sdk.entity.databroad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QRReqVO {


    @ApiModelProperty(value = "二维码名称" )
    private String qRName;

    @ApiModelProperty(value = "页面路径" )
    private String page;

    @ApiModelProperty(value = "二维码名称" )
    private String groupName;

    @ApiModelProperty(value = "创建时间" )
    private String createTime;

    @ApiModelProperty(value = "访问次数" )
    private Long trackCount;

    @ApiModelProperty(value = "访问人数" )
    private Long useridCount;

    @ApiModelProperty(value = "新用户数" )
    private Long newUseridCount;




}
