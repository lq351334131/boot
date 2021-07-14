package com.etocrm.sdk.entity.funnel;

import lombok.Data;

@Data
public class FunnelSetpsIdVO extends FunnelSetpsVO {
    //步骤id
    private Integer setpId;

    private String funnelId;
}
