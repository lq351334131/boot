package com.etocrm.sdk.entity.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VisitTotalVO {


    @ApiModelProperty(value = "访问人数" )
    private Integer personNum=0;

    @ApiModelProperty(value = "访问次数" )
    private Integer visitNum=0;

    @ApiModelProperty(value = "分享次数" )
    private Integer shareNum=0;

    @ApiModelProperty(value = "退出率" )
    private String avgExitRate;

    @ApiModelProperty(value = "页停留时长" )
    private String avgStopTime;

    @JsonIgnore
    private Integer exitNum=0;

    @JsonIgnore
    private Integer openNum=0;


}
