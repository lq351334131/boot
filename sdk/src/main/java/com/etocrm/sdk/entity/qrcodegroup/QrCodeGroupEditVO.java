package com.etocrm.sdk.entity.qrcodegroup;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Date 2020/10/13 15:59
 */
@Data
@Api(value="二维码组编辑VO")
public class QrCodeGroupEditVO implements Serializable {

    @NotBlank(message="主键id不能为空")
    @ApiModelProperty(value = "主键id")
    private String id;

    @NotBlank(message="AppId不能为空")
    @ApiModelProperty(value = "小程序id")
    private String appId;

    @NotBlank(message="二维码组名称不能为空")
    @ApiModelProperty(value = "二维码组名称")
    private String groupName;

    @ApiModelProperty(value = "二维码组描述")
    private String remark;
}
