package com.etocrm.sdk.entity.Qr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
public class QueryGroupPageVO extends QrGroupList {

    @NotNull
    private Integer pageIndex;

    @NotNull
    private Integer pageSize=10;

    @JsonIgnore
    private Integer type;

}
