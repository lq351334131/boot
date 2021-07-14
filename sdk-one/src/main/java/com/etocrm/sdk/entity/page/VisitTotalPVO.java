package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VisitTotalPVO  extends VisitTotalVO {

    private String id;

    @ApiModelProperty(value = "页面路径" )
    private  String  visitPath;

    @ApiModelProperty(value = "页面名称" )
    private  String  pathName;

    @ApiModelProperty(value = "所属功能模块" )
    private  String  moduleName;

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
     *  分享次数
     **/
    @ApiModelProperty(value = "分享次数" )
    private Integer shareNum;

    /**
     *  平均停留时长
     **/
    @ApiModelProperty(value = "平均停留时长" )
    private String avgStopTime;

    /**
     *  平均退出率
     **/
    @ApiModelProperty(value = "平均退出率" )
    private String avgExitRate;


}
