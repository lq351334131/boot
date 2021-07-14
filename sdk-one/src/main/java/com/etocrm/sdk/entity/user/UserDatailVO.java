package com.etocrm.sdk.entity.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDatailVO {

    @ExcelProperty(value = "userId", index = 0)
    @ApiModelProperty(value = "userId" )
    private String userId;

    @ExcelProperty(value = "unionId", index = 1)
    @ApiModelProperty(value = "unionId" )
    private String unionId;

    @ExcelProperty(value = "openId", index = 2)
    @ApiModelProperty(value = "openId" )
    private String openId;

    @ExcelProperty(value = "nickName", index = 3)
    @ApiModelProperty(value = "昵称" )
    private String nickName;

}
