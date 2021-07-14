package com.etocrm.sdk.entity.user;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class UserPageVO {
    @NotNull
    private String appKey;

    @NotNull
    private String beginDate;
    @NotNull
    private String endDate;

    private Integer pageIndex=1;

    private Integer pageSize=10;

    //private Integer type;//缓存
}
