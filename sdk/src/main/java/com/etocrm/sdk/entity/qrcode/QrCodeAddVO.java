package com.etocrm.sdk.entity.qrcode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @Date 2020/10/13 11:11
 */
@Data
@Api(value="二维码添加VO")
public class QrCodeAddVO implements Serializable {
    @NotBlank(message="AppId不能为空")
    @ApiModelProperty(value = "小程序id")
    private String appId;

    @ApiModelProperty(value = "小程序key")
    private String appKey;

    @NotBlank(message="二维码名称不能为空")
    @ApiModelProperty(value = "二维码名称")
    private String qrName;

    @ApiModelProperty(value = "小程序appSecret")
    private String appSecret;

    @ApiModelProperty(value = "二维码组ID")
    private String qrGroupId;

    @ApiModelProperty(value = "二维码小程序路径")
    private String page;

    @ApiModelProperty(value = "二维码图片url")
    private String imgUrl;

    @ApiModelProperty(value = "二维码宽度")
    private String codeWidth;

    @ApiModelProperty(value = "二维码Scene参数")
    private List<QrCodeSceneVO> scene;

}
