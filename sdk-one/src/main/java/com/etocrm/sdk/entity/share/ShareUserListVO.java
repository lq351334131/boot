package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;


@Data
public class ShareUserListVO {

    @NotNull
    @ApiModelProperty(value = "appkey不能为空",required = true )
    private String appKey;

    private String content;

    @NotNull
    @ApiModelProperty(value = "条数不能为空",required = true )
    private Integer pageSize;

    @NotNull
    @ApiModelProperty(value = "页码不能为空",required = true )
    private Integer pageIndex;

    @NotNull(message = "时间不能为空",profiles = "profile_4")
    @ApiModelProperty(value = "时间不能为空",required = true )
    private String date;
}
