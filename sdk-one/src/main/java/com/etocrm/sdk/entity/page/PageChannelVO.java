package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotNull;

@Data
public class PageChannelVO {

    private  String  name;

    @NotNull
    @ApiModelProperty(value = "页面id",required = true )
    private String  pathId;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "条数不能为空" ,required = true)
    private Integer pageSize=10;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "页码不能为空",required = true )
    private Integer pageIndex=1;
}
