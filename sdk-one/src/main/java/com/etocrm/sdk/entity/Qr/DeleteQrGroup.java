package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "二维码组返回")
public class DeleteQrGroup {

    private String id;

    private String appId;

}
