package com.etocrm.sdk.entity.Qr;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodeRepVOParVO {


    private String id;

    @ApiModelProperty("二维码名称")
    private String  qrName;

    @ApiModelProperty("url")
    private String  page;

    @ApiModelProperty("尺寸")
    private String  codeWidth;

    @ApiModelProperty("二维码组id")
    private String  qrGroupId;

    @ApiModelProperty("二维码组名称")
    private String  groupName;

    @ApiModelProperty("创建时间")
    private String  createTime;

   // private String imgUrl;

    @ApiModelProperty("场景值")
    private String scene;

    @ApiModelProperty("样式")
    private String codeType;


}
