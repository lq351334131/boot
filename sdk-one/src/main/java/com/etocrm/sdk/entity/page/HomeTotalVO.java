package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HomeTotalVO {

    /**
     * 访问次数
     **/
    @ApiModelProperty(value = "访问次数" )
    private Integer   visitNum;

    /**
     *  访问人数
     **/
    @ApiModelProperty(value = "访问人数" )
    private Integer  personNum;

    /**
     *  入口页
     **/
    @ApiModelProperty(value = "入口页" )
    private Integer  entryNum;

    @ApiModelProperty(value = "页停留时长" )
    private String avgStopTime;

    /**@JsonIgnore
    private Integer openNum;*/
}
