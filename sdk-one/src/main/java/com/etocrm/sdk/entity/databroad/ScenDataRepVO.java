package com.etocrm.sdk.entity.databroad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScenDataRepVO {

    @ApiModelProperty(value = "次均时长" )
    private String avgDate;

    @ApiModelProperty(value = "跳出率" )
    private String bounceRate;

    @ApiModelProperty(value = "场景值" )
    private Integer scene;

    @ApiModelProperty(value = "场景名称" )
    private String sceneName;

    @ApiModelProperty(value = "访问人数" )
    private Integer useridCount;

    @ApiModelProperty(value = "打开次数" )
    private Integer openCount;

    @ApiModelProperty(value = "访问次数" )
    private Integer useridVisitCount;

}
