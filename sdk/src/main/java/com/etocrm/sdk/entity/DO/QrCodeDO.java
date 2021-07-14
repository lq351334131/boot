package com.etocrm.sdk.entity.DO;

import com.etocrm.sdk.entity.qrcode.QrCodeSceneVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @Date 2020/10/14 14:43
 */
@Data
public class QrCodeDO extends BaseDO implements Serializable {

    @ApiModelProperty(value = "小程序id")
    private String appId;

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
