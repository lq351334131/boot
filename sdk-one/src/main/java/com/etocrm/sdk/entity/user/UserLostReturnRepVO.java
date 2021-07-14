package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLostReturnRepVO {

    @ApiModelProperty("unionId")
    private String unionId;

    @ApiModelProperty("uu")
    private String uu;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("昵称")
    private String nickName;
}
