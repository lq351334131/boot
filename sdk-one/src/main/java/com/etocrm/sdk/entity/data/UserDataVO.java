package com.etocrm.sdk.entity.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
@ApiModel(value = "数据总览")
public class UserDataVO {

    @NotNull(message="appkey不为空")
    @ApiModelProperty(value = "小程序标识不能为空" ,required = true)
    List<DataListVO> appKeys;

    /*@NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间不能为空",required = true )
    private String beginDate;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间不能为空",required = true )
    private String endDate;

   // private Integer type1;//缓存





}
