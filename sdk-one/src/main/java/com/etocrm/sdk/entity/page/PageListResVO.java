package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel("页面管理")
public class PageListResVO {

    @NotNull
    @ApiModelProperty(value = "id不为空" ,required = true)
    private String id;

    @ApiModelProperty(value = "所属功能模块" )
    private String moduleName;

    @NotNull
    @ApiModelProperty(value = "页面名称" )
    private String pathName;

    @ApiModelProperty(value = "所属统计模块,1电商，2其他" )
    private Integer pathTypeId;

    @ApiModelProperty(value = "url" )
    private String visitPath;
}
