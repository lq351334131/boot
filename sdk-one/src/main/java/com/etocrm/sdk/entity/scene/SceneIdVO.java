package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class SceneIdVO {

    //@NotNull
    private  Integer sceneId;

    @NotNull
    @ApiModelProperty(value = "开始时间",required = true)
    private String beginDate;

    @NotNull
    @ApiModelProperty(value = "结束时间",required = true)
    private String endDate;

    @NotNull
    @ApiModelProperty(required = true)
    private  String appKey;


}
