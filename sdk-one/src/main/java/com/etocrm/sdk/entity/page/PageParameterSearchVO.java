package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class PageParameterSearchVO {


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
    @ApiModelProperty(value = "页面id不能为空", required = true)
    private String pathId;

    private String  name;

    @NotNull
    @ApiModelProperty(value = "开始时间必填", required = true)
    private String beginDate;

    @NotNull
    @ApiModelProperty(value = "结束时间必填", required = true)
    private String endDate;

    @NotNull
    @ApiModelProperty(value = "页面参数id必填", required = true)
    private String id;
}
