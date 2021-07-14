package com.etocrm.sdk.entity.user;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class UserLostTypeVO {

    @NotNull
    private String appKey;

    @NotNull
    private String date;

    //区分流失与回流
    @NotNull
    private Integer type;

    @NotNull
    private Integer pageSize;

    @NotNull
    private Integer pageIndex;

}
