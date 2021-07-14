package com.etocrm.sdk.entity.page;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageDepthVO {

    @ExcelProperty(value = "深度访问页", index = 0)
    @ApiModelProperty("访问深度")
    private  String keyName;

    @ExcelProperty(value = "用户数", index = 1)
    @ApiModelProperty("周期次数")
    private Integer num;

    @ExcelProperty(value = "所占比率", index = 2)
    @ApiModelProperty("比列")
    private  String rate;

}
