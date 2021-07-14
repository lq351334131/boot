package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodeParam {

    @ApiModelProperty(value = "appId" )
    private String appId;

    @ApiModelProperty(value = "qrId",required = true )
    private String qrId;

}
