package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDetailsReqVO {


    @ApiModelProperty("uId")
    private  String uId;

    @ApiModelProperty("用户名称")
    private String nickName;

    @ApiModelProperty("openId")
    private String openId;

    private String unionId;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("语言")
    private String language;

    @ApiModelProperty("国家")
    private String country;

}
