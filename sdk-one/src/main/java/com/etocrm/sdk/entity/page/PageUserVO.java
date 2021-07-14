package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class PageUserVO {

    @NotNull
    @ApiModelProperty(value = "appkey", required = true)
    private  String    appKey;

    /*@NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

   //昵称或openID
   @ApiModelProperty(value = "昵称或openId")
   private String content;

    @NotNull
    @ApiModelProperty(value = "页面ID", required = true)
    private String visitpathId;

    @NotNull
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageIndex;

    @NotNull
    @ApiModelProperty(value = "分页条数", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "开始时间必填", required = true)
    @NotNull
    private String beginDate;

    @ApiModelProperty(value = "结束时间必填", required = true)
    @NotNull
    private String endDate;
}
