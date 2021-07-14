package com.etocrm.sdk.entity.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PageAccessVo {

    @NotNull
    @ApiModelProperty(value = "appkey不为空",required = true )
    private  String   appKey;

   /* @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

    @JsonIgnore
    private Integer type;

    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间不能为空",required = true )
    private String beginDate;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间不能为空",required = true )
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String endDate;


}
