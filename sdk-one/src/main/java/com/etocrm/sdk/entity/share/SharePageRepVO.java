package com.etocrm.sdk.entity.share;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分享页面")
public class SharePageRepVO {

    @ExcelProperty(value = "页面ID", index = 0)
    @ApiModelProperty("页面ID")
    private  String id;//页面ID

    @ExcelProperty(value = "页面url", index = 1)
    @ApiModelProperty("页面url")
    private String url;//页面url

    @ExcelProperty(value = "页面名称", index = 2)
    @ApiModelProperty("页面名称")
    private String name;//页面名称

    //分享人数总和
    @ExcelProperty(value = "分享人数", index = 3)
    @ApiModelProperty("分享人数总和")
    private Integer peopleNum;

    //分享次数总和
    @ApiModelProperty("分享次数总和")
    @ExcelProperty(value = "分享次数", index = 4)
    private Integer shareNum;

    @ApiModelProperty("回流量总和")
    @ExcelProperty(value = "回流量", index = 5)
    private Integer turnback;

    //分享回流比
    @ApiModelProperty("分享回流比总和")
    @ExcelProperty(value = "分享回流比", index = 6)
    private String turnbackProp;

    @ExcelProperty(value = "分享新增人数", index = 7)
    @ApiModelProperty("分享新增人数总和")
    private Integer userPlusNum;

}
