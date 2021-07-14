package com.etocrm.sdk.entity.scene;

import com.alibaba.excel.annotation.ExcelProperty;
import com.etocrm.sdk.entity.databroad.TotalDataRepVO;
import lombok.Data;

@Data
public class SceneTotalDetailDomainRepVO {

    @ExcelProperty(value = "场景id", index = 0)
    private String scene;

    @ExcelProperty(value = "场景名称", index = 1)
    private String sceneName;


    //用户数
    @ExcelProperty(value = "访问人数", index = 2)
    private Long userNum;

    //新用户数
    @ExcelProperty(value = "新用户数", index = 3)
    private Long newUserNum;

    //次数
    @ExcelProperty(value = "访问次数", index = 4)
    private Long  visitNum;

    //打开次数
    @ExcelProperty(value = "打开次数", index = 5)
    private Long openNum;

    @ExcelProperty(value = "次均停留时长", index = 6)
    private String  avgStopTime;

    @ExcelProperty(value = "跳出率", index = 7)
    private  String exitRate;



}
