package com.etocrm.sdk.entity.eventVO;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class EventVO {

    @NotNull(message = "appKey不能为空")
    private String  appKey;

    @NotNull(message = "appKey不能为空")
    private Integer	pageSize;

    @NotNull(message = "页码不能为空")
    private Integer pageIndex;

    private String eventName;

    private String eventKey;




}
