package com.etocrm.sdk.entity.funnel;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.Date;

@Data
public class FunnelAddRepVO {

    @NotNull
    private String funnelName;

    private String typeId;

    //private List<FunnelSetpsVO> funnelSetps;

    private Date createTime;

    private String k;

}
