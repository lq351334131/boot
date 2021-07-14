package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "二维码删除")
public class DeleteQrCodeVO {

    @NotNull
    private String qrId;

    @NotNull
    private String appId;
}
