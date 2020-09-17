package com.etocrm.sdk.entity.EventVO;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

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
