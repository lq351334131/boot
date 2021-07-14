package com.etocrm.sdk.entity.qrcodegroup;

import com.etocrm.sdk.entity.PageInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Date 2020/10/13 16:47
 */
@Data
@Api(value="查询二维码组VO")
public class GetQrCodeGroupVO extends PageInfoVO {

    @ApiModelProperty(value = "主键id")
    private String id;

    @NotBlank(message="AppId不能为空")
    @ApiModelProperty(value = "小程序id")
    private String appId;

    @ApiModelProperty(value = "二维码组名称")
    private String groupName;

}
