package com.etocrm.sdk.entity.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddCartVO {

    @ApiModelProperty("商品id")
    private String productId;

    @ApiModelProperty("次数")
    private Integer pv;
}
