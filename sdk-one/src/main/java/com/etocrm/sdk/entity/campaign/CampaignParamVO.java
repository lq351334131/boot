package com.etocrm.sdk.entity.campaign;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class CampaignParamVO {

    @NotNull(message = "参数名")
    private String key;

    @NotNull(message = "参数值")
    private String value;
}
