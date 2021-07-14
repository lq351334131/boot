package com.etocrm.sdk.entity.page;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class PageDownloadVO {

    @NotNull
    private String appKey;

    private String pagePath;

    private String pageName;

    private String moduleName;

    private Integer pathTypeId;
}
