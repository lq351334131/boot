package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class SceneExcelVO {

    /*@NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date queryDate;*/

    @NotNull
    @ApiModelProperty(required = true)
    private String appKey;

    @NotNull
    @ApiModelProperty(value = "场景id",required = true)
    private Integer sceneId;

    @NotNull
    @ApiModelProperty(value = "时间",required = true)
    private String queryDate;

}
