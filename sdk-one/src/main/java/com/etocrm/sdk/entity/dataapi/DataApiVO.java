package com.etocrm.sdk.entity.dataapi;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "参数")
public class DataApiVO {

    @NotNull
    private String appKey;

    private String time;


}
