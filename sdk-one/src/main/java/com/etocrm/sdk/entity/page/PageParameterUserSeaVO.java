package com.etocrm.sdk.entity.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "页面参数-用户")
public class PageParameterUserSeaVO {

    @NotNull(message = "appKey不能为空")
    @ApiModelProperty(value = "appKey不能为空", required = true)
    private String appKey;

    //@NotNull(message = "appId不能为空")
    private String appId;

    /*@NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;*/

    @NotNull
    @ApiModelProperty(value = "页面参数id", required = true )
    private String id;//子

    @NotNull
    @ApiModelProperty(value = "页面管理id", required = true )
    private String  pathId;

    @ApiModelProperty(value = "openId或者用户昵称" )
    private String  content;

   /*    @JsonIgnore
    private String params;

    @JsonIgnore
    private String p;

    @JsonIgnore
    private String string="&";*/

    @NotNull
    @ApiModelProperty(value = "开始时间必填", required = true)
    private String beginDate;

    @NotNull
    @ApiModelProperty(value = "结束时间必填", required = true)
    private String endDate;

    //clickhouse 查询拼接sql
    //@NotNull
    @JsonIgnore
    private String sql;

    @JsonIgnore
    private String visitpath;



}
