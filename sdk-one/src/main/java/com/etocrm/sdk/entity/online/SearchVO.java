package com.etocrm.sdk.entity.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchVO {

    @ApiModelProperty("热搜")
    private String search;

    @ApiModelProperty("次数")
    private Integer num;

}
