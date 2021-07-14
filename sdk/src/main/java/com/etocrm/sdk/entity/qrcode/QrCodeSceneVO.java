package com.etocrm.sdk.entity.qrcode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2020/10/14 9:57
 */
@Data
@Api(value="二维码Scene参数VO")
public class QrCodeSceneVO implements Serializable {
    @ApiModelProperty(value = "Scene参数键")
    private String key;

    @ApiModelProperty(value = "Scene参数值")
    private String value;
}
