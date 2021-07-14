package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class SceneDateVO {

   /* @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date queryDate;*/

    @NotNull
    @ApiModelProperty(value = "appKey",required = true)
    private String appKey;

    @NotNull
    @ApiModelProperty(value = "条数",required = true)
    private Integer pageSize=10;

    @NotNull
    @ApiModelProperty(value = "页码",required = true)
    private Integer pageIndex=1;

    @NotNull
    @ApiModelProperty(value = "场景id",required = true)
    private Integer sceneId;

    private String queryDate;



}
