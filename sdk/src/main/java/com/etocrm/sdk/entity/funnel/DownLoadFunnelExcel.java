package com.etocrm.sdk.entity.funnel;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class DownLoadFunnelExcel {

    @NotNull
    private String appKey;

    private String funnelName;

}
