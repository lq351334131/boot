package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "二维码")
public class EditQrCodeVO  extends AddQrCodeVO {

    @NotNull
    @ApiModelProperty(value = "不能为空",required = true )
    private String id;

}
