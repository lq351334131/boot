package com.etocrm.sdk.entity.page;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class HomePageExcel {

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
    private Long   visitNum;

    /**
     *  访问人数
     **/
    @ExcelProperty(value = "访问人数", index = 4)
    private Long  personNum;

    /**
     *  入口页
     **/
    @ExcelProperty(value = "入口页", index = 5)
    private Long  entryNum;

    @ExcelProperty(value = "均时长", index = 6)
    private String avgStopTime;

}
