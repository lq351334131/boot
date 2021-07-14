package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class QrCodeVO {

    private String groupName;


    private String qrName;

    @NotNull
    @ApiModelProperty(value = "appId",required = true )
    private String appId;



}
