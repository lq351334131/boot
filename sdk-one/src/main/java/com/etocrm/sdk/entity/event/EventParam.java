package com.etocrm.sdk.entity.event;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class EventParam {

    @NotNull
    private Integer type;

    @NotNull
    private String k;

    @NotNull
    private String name;

    private String eventId;

    private String id;


}
