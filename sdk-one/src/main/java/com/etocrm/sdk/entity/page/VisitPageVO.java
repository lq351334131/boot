package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class VisitPageVO {

    @ApiModelProperty(value = "appkey", required = true)
    @NotNull(message = "appkey不为空")
    private  String    appKey;

   /* @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

    @NotNull(message = "分页条数不为空")
    @ApiModelProperty(value = "分页条数", required = true)
    private Integer pageSize;

    @NotNull(message = "页码不为空")
    @ApiModelProperty(value = "页码不为空", required = true)
    private Integer pageIndex;

    @ApiModelProperty(value = "url")
    private  String  visitPath;

    @ApiModelProperty(value = "所属功能模块" )
    private  String  moduleName;

    @ApiModelProperty(value = "所属统计模块,1电商，2其他" )
    private  Integer pathTypeId;

    @NotNull
    @ApiModelProperty(value = "开始时间必填", required = true)
    private String beginDate;

    @ApiModelProperty(value = "结束时间必填", required = true)
    @NotNull
    private String endDate;



}
