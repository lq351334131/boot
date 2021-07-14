package com.etocrm.sdk.entity.eventVO;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class EventEditVO {

    @NotNull
    private String  appKey;

    @NotNull
    private String tv;

    @NotNull
    private String tl;

    private  String  id;
}
