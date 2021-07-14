package com.etocrm.sdk.entity.page;

import lombok.Data;

@Data
public class VisitPageResVO {

    private  String  visitPath;

    private  String  moduleName;

    private  String  pathName;

    /**
     * 访问次数
     **/
    private Long   visitNum;

    /**
     *  访问人数
     **/
    private Long  personNum;


    /**
     *  分享次数
     **/
    private Long shareNum;

    /**
     *  平均停留时长
     **/
    private String avgStopTime;

    /**
     *  平均退出率
     **/
    private String avgExitRate;

}
