package com.etocrm.sdk.entity.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YestDayRepVO {


    private String  name;

    private String appKey;

    //用户数
    @ApiModelProperty(value = "人数" )
    private Integer userNum;

    //新用户数
    @ApiModelProperty(value = "新用户数" )
    private Integer newUserNum;

    //次数
    @ApiModelProperty(value = "访问次数" )
    private Integer  visitNum;

    //打开次数
    @ApiModelProperty(value = "打开次数" )
    private Integer openNum;

    @ApiModelProperty(value = "次均停留时长" )
    private String  avgStopTime;

    @ApiModelProperty(value = "跳出率" )
    private  String exitRate;
}
