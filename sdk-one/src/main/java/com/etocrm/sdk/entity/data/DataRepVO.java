package com.etocrm.sdk.entity.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数据总览")
public class DataRepVO {

    @ApiModelProperty(value = "日期" )
    private  String date;

    private Long  value;
}
