package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;


@Data
@ApiModel(value = "用户流失与回流")
public class UserLostTypeDowVO {

    @NotNull
    @ApiModelProperty(value = "appkey不为空",required = true)
    private String appKey;

    @NotNull
    @ApiModelProperty(value = "区分流失与回流，1流失，2回流",required = true)
    private Integer typeId;


    @NotNull
    @ApiModelProperty(value = "时间不为空",required = true)
    private String curDate;

}
