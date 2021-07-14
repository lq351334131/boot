package com.etocrm.sdk.entity.VO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PageDepthVO {

    @ExcelProperty(value = "深度访问页", index = 0)
    private  String keyName;

    @ExcelProperty(value = "用户数", index = 1)
    private Integer num;

    @ExcelProperty(value = "所占比率", index = 2)
    private  String rate;
}
