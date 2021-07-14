package com.etocrm.sdk.entity.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PageParameterPageSearchVO  extends PageParameterSearchVO {

    private Integer pageSize;

    private Integer pageIndex;

    @JsonIgnore
    private String sql;

    private String content;

    private String visitPath;

}
