package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "二维码组添加")
public class AddQrGroup {

    @NotNull
    @ApiModelProperty(value = "appid不能为空",required = true )
    private String appId;

    @NotNull
    @ApiModelProperty(value = "名称不能为空",required = true )
    private String groupName;

    //@NotNull
    private String remark;
}
