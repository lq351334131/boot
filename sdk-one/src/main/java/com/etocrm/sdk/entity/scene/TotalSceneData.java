package com.etocrm.sdk.entity.scene;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TotalSceneData {

    @ApiModelProperty(value = "新增用户数" )
    private Integer newUseridCount=0;

    @ApiModelProperty(value = "访问人数" )
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


}

