package com.etocrm.sdk.entity.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Range;

@Data
public class ParamDataSearchVO {

    @ApiModelProperty(value = "appkey不为空", required = true)
    private String appKey;

    //@NotNull(message = "appId不能为空")
   /* private String appId;

    @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;*/

    @NotNull
    @ApiModelProperty(value = "页面id不为空", required = true)
    private String pathId;

    /**
     *
     * @Description 渠道名称
     * @author xing.liu
     * @date 2021/6/5
     **/
    private String name;

    @NotNull
    @Range(min = 1)
    @ApiModelProperty(value = "分页条数", required = true)
    private Integer pageSize;

    @NotNull
    @Range(min = 1)
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageIndex;

  /*  @JsonIgnore
    private String p;*/

    @NotNull
    @ApiModelProperty(value = "开始时间必填", required = true)
    private String beginDate;

    @ApiModelProperty(value = "结束时间必填", required = true)
    @NotNull
    private String endDate;


    @JsonIgnore
    private String visitpath;





}


