package com.etocrm.sdk.entity.VO;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class PageListVO {
    //@NotNull
    private String appKey;

    private String pagePath;

    private String pageName;

    private String moduleName;

    private Integer pathTypeId;

    @NotNull
    private int pageSize;

    @NotNull
    private int pageIndex;

}
