package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class QrCodeSceneVO {

    @NotNull
    @ApiModelProperty(value = "key",required = true )
    private String key;

    @NotNull
    @ApiModelProperty(value = "value",required = true )
    private String value;
}
