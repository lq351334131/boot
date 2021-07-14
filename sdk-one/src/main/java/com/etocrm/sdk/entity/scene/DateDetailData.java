package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DateDetailData {

    /*private Integer scene;

    private String sceneName;

    private Integer newUserNum;

    private Integer userNum;

    private Integer visitNum;

    private Integer openNum;

   // private Integer distanceValue;

    private String avgStopTime;

    private String exitRate;

    private String date;*/
    @ApiModelProperty(value = "场景id" )
    private Integer scene;

    @ApiModelProperty(value = "场景名称" )
    private String sceneName;

    @ApiModelProperty(value = "新用户数" )
    private Integer newUseridCount=0;

    @ApiModelProperty(value = "用户数" )
    private Integer useridCount=0;

    @ApiModelProperty(value = "访问次数" )
    private Integer useridVisitCount=0;

    /**
     *
     * @Description 打开
     * @author xing.liu
     * @date 2021/5/31
     **/
    @ApiModelProperty(value = "打开次数" )
    private Integer totalSimpleBakCount=0;

    @ApiModelProperty(value = "次时长" )
    private String  avgDate="0.00";

    @ApiModelProperty(value = "跳出率" )
    private String  bounceRate="0.00";

    @ApiModelProperty(value = "日期" )
    private String date;

}
