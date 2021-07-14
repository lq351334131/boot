package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel("用户画像")
public class UserVO {

    @NotNull
    @ApiModelProperty(value = "小程序标识不能为空" )
    private String appKey;

    @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @ApiModelProperty(value = "开始时间不能为空" )
    private String beginDate;

    @NotNull(message = "结束时间不能为空",profiles = "profile_4")
    @ApiModelProperty(value = "结束时间不能为空" )
    private String endDate;
}
