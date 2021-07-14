package com.etocrm.sdk.entity.dataapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SceneReqVO {

    private Integer pv;

    private Integer uv;

    private String appKey;

    @ApiModelProperty("场景值")
    private Integer scene;


}
