package com.etocrm.sdk.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2020/10/13 16:54
 */
@Data
public class PageInfoVO implements Serializable {

    @ApiModelProperty(value = "显示条数")
    private int pageSize=10;

    @ApiModelProperty(value = "当前页")
    private int pageIndex=1;

}
