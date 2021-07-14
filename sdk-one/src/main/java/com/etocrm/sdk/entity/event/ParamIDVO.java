package com.etocrm.sdk.entity.event;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class ParamIDVO {

    @NotNull
    private String appKey;

    @NotNull
    private String eventId;

    @NotNull
    private Integer type;

}
