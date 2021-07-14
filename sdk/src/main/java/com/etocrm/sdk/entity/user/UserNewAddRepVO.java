package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class UserNewAddRepVO {

    @ExcelProperty(value = "日期", index = 0)
    private String curDate;
    @ExcelProperty(value = "新用户数", index = 1)
    private Long count;

    @ExcelProperty(value = "1天以后", index = 3)
    private String oneLater;

    @ExcelProperty(value = "2天以后", index = 4)
    private String twoLater;

    @ExcelProperty(value = "3天以后", index = 5)
    private String threeLater;

    @ExcelProperty(value = "4天以后", index = 6)
    private String fourLater;

    @ExcelProperty(value = "5天以后", index = 7)
    private String fiveLater;

    @ExcelProperty(value = "6天以后", index = 8)
    private String sixLater;

    @ExcelProperty(value = "7天以后", index = 9)
    private String sevenLater;

    @ExcelProperty(value = "14天以后", index = 10)
    private String fourteenLater;

    @ExcelProperty(value = "30天以后", index = 5)
    private String thirtyLater;


}
