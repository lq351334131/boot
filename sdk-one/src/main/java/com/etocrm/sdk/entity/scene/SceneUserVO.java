package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class SceneUserVO {

   /* @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date queryDate;*/

    @NotNull
    private String appKey;

    @NotNull
    @ApiModelProperty(value = "条数",required = true)
    private Integer pageSize=10;

    @NotNull
    @ApiModelProperty(value = "页码",required = true)
    private Integer pageIndex=1;

    @NotNull
    @ApiModelProperty(value = "场景名称",required = true)
    private String  sceneTypeName;

    @NotNull
    @ApiModelProperty(value = "日期",required = true)
    private String queryDate;

}
