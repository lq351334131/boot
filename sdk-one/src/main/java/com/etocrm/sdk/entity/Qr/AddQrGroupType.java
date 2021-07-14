package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "二维码返回")
public class AddQrGroupType   {

    private Integer type=0;

    private String   id;

    private String appId;

    private String groupName;

    private String remark;

    private String appSecret;

    private String codeType;

    private String codeWidth;

    private String page;

    private String qrGroupId;

    private String qrName;

    private String scene;

}
