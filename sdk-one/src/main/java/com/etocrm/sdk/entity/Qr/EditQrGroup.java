package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "二维码组编辑")
public class EditQrGroup {
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
