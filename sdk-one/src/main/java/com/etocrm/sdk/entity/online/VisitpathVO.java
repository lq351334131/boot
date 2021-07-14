package com.etocrm.sdk.entity.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description shop
 * @author xing.liu
 * @date 2021/6/1
 **/
@Data
public class VisitpathVO {

    @ApiModelProperty("url")
    private String visitpath;

    @ApiModelProperty("次数")
    private Integer num;

}
