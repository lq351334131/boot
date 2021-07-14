package com.etocrm.sdk.entity.databroad;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "总数据")
public class TotalDataRepVO {

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

    //平均停留时间（次）
    @ApiModelProperty(value = "次均停留时长" )
    private String  avgStopTime;

    //跳出率
    @ApiModelProperty(value = "跳出率" )
    private  String bounceRate;


}
