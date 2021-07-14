package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分享")
public class ShareNumVO {

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "次数")
    private Integer num;
}
