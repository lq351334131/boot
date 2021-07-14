package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class UserCitySeaPageVO extends  UserCitySeaVO{

    @ApiModelProperty(value = "条数不能为空",required = true )
    @NotNull
    private Integer pageSize=10;

    @NotNull
    @ApiModelProperty(value = "页码不能为空",required = true )
    private Integer pageIndex;
}
