package com.etocrm.sdk.entity.qrcodegroup;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Date 2020/10/13 11:16
 */
@Data
@Api(value="二维码组添加VO")
public class QrCodeGroupAddVO implements Serializable {
    @NotBlank(message="AppId不能为空")
    @ApiModelProperty(value = "小程序id")
    private String appId;

    @NotBlank(message="二维码组名称不能为空")
    @ApiModelProperty(value = "二维码组名称")
    private String groupName;

    @ApiModelProperty(value = "二维码组描述")
    private String remark;

}
