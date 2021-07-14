package com.etocrm.sdk.entity.funnel;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class FunnelVO {

    @NotNull(message = "appKey不能为空")
    private String  appKey;

    @NotNull(message = "appKey不能为空")
    private Integer	pageSize;

    @NotNull(message = "页码不能为空")
    private Integer pageIndex;

    private String  funnelName;

}
