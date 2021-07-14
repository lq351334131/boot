package com.etocrm.sdk.entity.databroad;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "通用")
public class DataBroadVO {
    @NotNull
    @ApiModelProperty(value = "小程序标识不能为空",required = true )
    private String appKey;

    /*@NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;*/

   // private String appId;

    @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @ApiModelProperty(value = "开始时间不能为空" ,required = true)
    private String beginDate;

    @NotNull(message = "结束时间不能为空",profiles = "profile_4")
    @ApiModelProperty(value = "结束时间不能为空",required = true )
    private String endDate;

    /*
     * 区分缓存：1,昨天，2天，3,15天，4一个月
     * @author xing.liu
     * @date 2021/2/26
     **/
    //private Integer type;

}
