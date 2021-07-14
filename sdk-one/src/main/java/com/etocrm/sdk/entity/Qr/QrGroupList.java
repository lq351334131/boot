package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrGroupList {

    @ApiModelProperty(value = "二维码组名称" )
    private String value;

    @ApiModelProperty(value = "appId",required = true)
    private String appId;

}
