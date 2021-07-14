package com.etocrm.sdk.entity.qrcode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Date 2020/10/14 10:09
 */
@Data
@Api(value="二维码删除VO")
public class DeleteQrCodeVO implements Serializable {
    @NotBlank(message="qrId不能为空")
    @ApiModelProperty(value = "二维码id")
    private String qrId;

    @NotBlank(message="AppId不能为空")
    @ApiModelProperty(value = "小程序id")
    private String appId;
}
