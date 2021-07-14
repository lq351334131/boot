package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SystemVO {

    @ExcelProperty(value = "操作系统", index = 0)
    private String regin;

    @ExcelProperty(value = "新用户数", index = 1)
    private Long newUserNum;

    @ExcelProperty(value = "访问人数", index = 2)
    private Long userNum;

    @ExcelProperty(value = "访问次数", index = 3)
    private Long visitNum;

    @ExcelProperty(value = "打开次数", index = 4)
    private Long openNum;

    @ExcelProperty(value = "次均停留时长", index = 5)
    private String avgStopTime;

    @ExcelProperty(value = "跳出率", index = 6)
    private String exitRate;

}
