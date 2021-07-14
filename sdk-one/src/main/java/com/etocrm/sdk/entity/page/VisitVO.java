package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class VisitVO {

    @ApiModelProperty(value = "appkey", required = true)
    @NotNull(message = "appkey不为空")
    private  String    appKey;

    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间必填", required = true)
    private String beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间不能为空", required = true)
    private String endDate;

    private  String  visitPath;

    private  String  moduleName;

    private  Integer pathTypeId;
}

