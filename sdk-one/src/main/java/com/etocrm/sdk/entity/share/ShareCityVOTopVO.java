package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分享城市")
public class ShareCityVOTopVO {

    @ApiModelProperty("城市")
    private String  city;

    @ApiModelProperty(value = "分享人数")
    private Integer  peopleNum;//分享人数



}
