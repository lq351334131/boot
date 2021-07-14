package com.etocrm.sdk.entity.share;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分享城市")
public class ShareCityVO {

    @ExcelProperty(value = "城市", index = 0)
    @ApiModelProperty("城市")
    private String  city;

    @ExcelProperty(value = "分享次数", index = 1)
    @ApiModelProperty("分享次数")
    private Integer  shareNum;//分享次数

    @ExcelProperty(value = "分享人数", index = 2)
    @ApiModelProperty("分享人数")
    private Integer  peopleNum;//分享人数

    @ExcelProperty(value = "回流量", index = 3)
    @ApiModelProperty("回流量")
    private Integer  turnback;//@ApiModelProperty("分享人数")

    @ExcelProperty(value = "分享回流比", index = 4)
    @ApiModelProperty("分享回流比")
    private String  turnbackProp;//分享回流比

    @ExcelProperty(value = "分享新增人数", index = 5)
    @ApiModelProperty("分享新增人数")
    private Integer  userPlusNum;//分享新增人数



}
