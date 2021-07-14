package com.etocrm.sdk.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "用户流失与回流")
public class UserLostTypeVO {

    @NotNull
    @ApiModelProperty(value = "appkey不为空",required = true)
    private String appKey;

  /*  @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date curDate;*/
    //区分流失与回流
    @NotNull
    @ApiModelProperty(value = "区分流失与回流，1流失，2回流",required = true)
    private Integer typeId;

    @NotNull
    @ApiModelProperty(value = "条数不为空",required = true)
    private Integer pageSize=10;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "页码不为空",required = true)
    private Integer pageIndex=1;

    @NotNull
    @ApiModelProperty(value = "时间不为空",required = true)
    private String curDate;

}
