package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel("页面管理")
public class PageListVO {

    @ApiModelProperty(value = "页面路径" )
    private String visitPath;

    @ApiModelProperty(value = "页面名称" )
    private String pathName;

    @ApiModelProperty(value = "所属功能模块" )
    private String moduleName;

    @ApiModelProperty(value = "所属统计模块,1电商，2其他" )
    private Integer pathTypeId;

    @NotNull
    @ApiModelProperty(value = "条数不能为空" ,required = true)
    @Min(value = 1,message = "条数最小1")
    private int pageSize=10;

    @NotNull
    @ApiModelProperty(value = "页码不能为空",required = true )
    @Min(value = 1,message = "页码最小1")
    private int pageIndex=1;

}
