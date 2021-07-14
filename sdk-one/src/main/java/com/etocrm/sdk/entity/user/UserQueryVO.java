package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel("用户")
public class UserQueryVO {


    @NotNull
    @ApiModelProperty(value = "小程序标识不能为空" )
    private String appKey;

   /* @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endDate;*/

    private Integer pageIndex=1;

    private Integer pageSize=10;

    private String searchContent;

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间不能为空" )
    private String beginDate;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间不能为空" )
    private String endDate;




}
