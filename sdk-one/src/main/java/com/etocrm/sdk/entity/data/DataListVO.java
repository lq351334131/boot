package com.etocrm.sdk.entity.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "参数")
public class DataListVO {

    @NotNull
    @ApiModelProperty(value = "小程序标识不能为空",required = true)
    private String appKey;

    private  String  appName;
}
