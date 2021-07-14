package com.etocrm.sdk.entity.event;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
public class EventAddVO {

    private String id;

    @NotNull
    private String eventKey;

    @NotNull
    private String eventName;

    @NotNull
    private List<EventParam> eventParam;



}
