package com.etocrm.sdk.entity.page;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ParamDataRepVO {

    @ExcelProperty(value = "子参数ID", index = 0)
    private String id;

    /**
     * 访问次数
     **/
    @ExcelProperty(value = "访问次数", index = 1)
    private Integer   visitNum;

    /**
     *  访问人数
     **/
    @ExcelProperty(value = "访问人数", index = 2)
    private Integer  personNum;


    @ExcelProperty(value = "平均退出率", index = 3)
    private String   avgExitRate="0.00";

    @ExcelProperty(value = "子参数名称", index = 4)
    private  String   name;







}
