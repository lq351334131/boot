package com.etocrm.sdk.entity.DO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Date 2020/10/14 14:44
 */
@Data
public class QrCodeGroupDO extends BaseDO implements Serializable {

    @ApiModelProperty(value = "小程序id")
    private String appId;

    @ApiModelProperty(value = "二维码组名称")
    private String groupName;

    @ApiModelProperty(value = "二维码组描述")
    private String remark;
}
