package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserStaticDate {

    @ApiModelProperty("日期")
    private String dateTime;

    private String count;

}
