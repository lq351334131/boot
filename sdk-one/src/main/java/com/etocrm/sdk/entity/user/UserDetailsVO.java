package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class UserDetailsVO {

    @NotNull
    @ApiModelProperty( value = "appKey",required = true)
    private String appKey;

    @NotNull
    @ApiModelProperty( value = "uu",required = true)
    private String userId;

}
