package com.etocrm.sdk.entity.data;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class YestDayVO {

    @NotNull
    private String appKey;

    private  String  appName;

}