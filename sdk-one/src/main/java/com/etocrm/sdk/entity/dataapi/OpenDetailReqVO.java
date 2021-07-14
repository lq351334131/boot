package com.etocrm.sdk.entity.dataapi;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "打开详情")
public class OpenDetailReqVO {

    private String back;

    private String appKey;

    private Integer pv;

    private Integer uv;

}
