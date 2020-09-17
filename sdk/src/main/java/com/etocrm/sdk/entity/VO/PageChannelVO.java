package com.etocrm.sdk.entity.VO;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class PageChannelVO {

    private  String  name;

    @NotNull
    private String  pathId;

    @NotNull
    private int pageSize;

    @NotNull
    private int pageIndex;
}
