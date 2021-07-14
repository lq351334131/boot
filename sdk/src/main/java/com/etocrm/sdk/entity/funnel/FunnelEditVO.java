package com.etocrm.sdk.entity.funnel;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
public class FunnelEditVO{

    @NotNull
    private  String id;

    @NotNull
    private String funnelName;

    private String typeId;

    private String appKey;

    @NotNull
    private List<FunnelSetpsIdVO> funnelSetps;

}
