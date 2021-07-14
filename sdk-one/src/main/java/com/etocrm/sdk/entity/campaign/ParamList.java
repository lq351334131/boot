package com.etocrm.sdk.entity.campaign;

import lombok.Data;

@Data
public class ParamList {

    private String key;

    private String value;

    private Integer pv;

    private Integer uv;

    private String json;
}
