package com.etocrm.sdk.entity.qrcode;

import com.etocrm.sdk.entity.PageInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Date 2020/10/14 10:10
 */
@Data
@Api(value="获取二维码列表VO")
public class GetQrCodeListVO extends PageInfoVO implements Serializable {

    @NotBlank(message="AppId不能为空")
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
}
