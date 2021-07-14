package com.etocrm.sdk.entity.dataapi;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "每天")
public class EverydayReqVO {

    private Integer pv;

    private Integer uv;

    private String appKey;

    private String dt;
}
