package com.etocrm.sdk.entity.Qr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodePageVO extends QrCodeVO {

    @ApiModelProperty(value = "条数",required = true )
    private Integer pageSize=10;

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageIndex=1;

    @JsonIgnore
    private Integer type;
}
