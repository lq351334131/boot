package com.etocrm.sdk.entity.scene;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SceneTotalDetailDomainRepVO {

    @ExcelProperty(value = "场景id", index = 0)
    @ApiModelProperty(value = "场景id" )
    private Integer scene;

    @ExcelProperty(value = "场景名称", index = 1)
    @ApiModelProperty(value = "场景名称" )
    private String sceneName;


    //用户数
    @ExcelProperty(value = "访问人数", index = 2)
    @JsonProperty("useridCount")
    private Integer userNum;

    //新用户数
    @ExcelProperty(value = "新用户数", index = 3)
    @JsonProperty("newUseridCount")
    private Integer newUserNum;

    //次数
    @ExcelProperty(value = "访问次数", index = 4)
    @JsonProperty("useridVisitCount")
    private Integer  visitNum;

    //打开次数
    @ExcelProperty(value = "打开次数", index = 5)
    @JsonProperty("totalSimpleBakCount")
    private Integer openNum;

    @ExcelProperty(value = "次均停留时长", index = 6)
    @JsonProperty("avgDate")
    private String  avgStopTime;

    @ExcelProperty(value = "跳出率", index = 7)
    @JsonProperty("bounceRate")
    private  String exitRate;



}
