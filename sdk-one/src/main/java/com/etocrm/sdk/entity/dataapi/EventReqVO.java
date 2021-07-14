package com.etocrm.sdk.entity.dataapi;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "事件")
public class EventReqVO {

    private Integer pv;

    private Integer uv;

    private String appKey;

    private String tv="event";

    private String tl;


}
