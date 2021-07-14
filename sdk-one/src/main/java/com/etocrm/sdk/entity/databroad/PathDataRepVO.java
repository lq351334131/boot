package com.etocrm.sdk.entity.databroad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "每天")
public class PathDataRepVO {

    //访问页面
    @ApiModelProperty(value = "访问页面" )
    private String visitPath;

    @ApiModelProperty(value = "访问人数" )
    private Long userNum;

    @ApiModelProperty(value = "访问次数" )
    private Long visitNum;

    //跳出
    @ApiModelProperty(value = "跳出率" )
    private String bounceRate;

    @ApiModelProperty(value = "次均时长" )
    private String avgDate;

    @JsonIgnore
    private Long openNum;

}
