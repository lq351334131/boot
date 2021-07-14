package com.etocrm.sdk.entity.page;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class VisitPageExcel {

    @ExcelProperty(value = "页面URL", index = 0)
    private  String  visitPath;

    @ExcelProperty(value = "页面名称", index = 1)
    private  String  pathName;

    @ExcelProperty(value = "模块名称", index = 2)
    private  String  moduleName;



    /**
     * 访问次数
     **/
    @ExcelProperty(value = "访问次数", index = 3)
    private Integer   visitNum;

    /**
     *  访问人数
     **/
    @ExcelProperty(value = "访问人数", index = 4)
    private Integer  personNum;


    /**
     *  分享次数
     **/
    @ExcelProperty(value = "分享次数", index = 5)
    private Integer shareNum;

    /**
     *  平均停留时长
     **/
    @ExcelProperty(value = "平均停留时长", index = 6)
    private String avgStopTime;

    /**
     *  平均退出率
     **/
    @ExcelProperty(value = "平均退出率", index = 7)
    private String avgExitRate;


}
