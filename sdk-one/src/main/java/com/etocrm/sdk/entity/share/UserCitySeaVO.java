package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class UserCitySeaVO {

    @NotNull
    @ApiModelProperty(value = "页面管理id", required = true )
    private String appKey;

   /* @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

    private String userName;

    @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @ApiModelProperty(value = "开始时间不能为空" ,required = true)
    private String beginDate;

    @NotNull(message = "结束时间不能为空",profiles = "profile_4")
    @ApiModelProperty(value = "结束时间不能为空" ,required = true)
    private String endDate;




}
