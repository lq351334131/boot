package com.etocrm.sdk.entity.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor
public class TotalVO {

    @ApiModelProperty(value = "1,新用户数,2访问次数,3,访问人数,4打开次数,5次均停留时长" )
    private Integer id;

    private String name;

    private String  value1;

    private String value2;





}
