package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "分享")
public class DateSharePageVO{

    @NotNull
    @ApiModelProperty(value = "页码不能为空",required = true )
    private Integer  pageIndex;

    @NotNull
    @ApiModelProperty(value = "条数不能为空",required = true )
    private Integer  pageSize=10;

    @NotNull
    @ApiModelProperty(value = "小程序标识不能为空",required = true )
    private String appKey;

    /*@NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

    @NotNull
    @ApiModelProperty(value = "开始时间不能为空" ,required = true)
    private String beginDate;

    @NotNull
    @ApiModelProperty(value = "结束时间不能为空" ,required = true)
    private String endDate;

}
