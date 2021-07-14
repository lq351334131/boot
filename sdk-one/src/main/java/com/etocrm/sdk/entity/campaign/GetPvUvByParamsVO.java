package com.etocrm.sdk.entity.campaign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Date 2021/6/8 11:48
 */
@ApiModel(value = "获取pu/uv的请求参数")
@Data
public class GetPvUvByParamsVO {

    @ApiModelProperty(value = "页面类型：1页面访问,2页面事件,3分享")
    @NotNull(message = "类型不能为空")
    private Integer pageType;

    @ApiModelProperty(value = "页面路径")
    private String pageUrl;

    @ApiModelProperty(value = "页面参数list")
    private List<CampaignParamVO> pageParamList;

    @ApiModelProperty(value = "开始时间")
    //@DateTimeFormat(pattern = TimeUtils.DATE_FORMAT)
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    //@DateTimeFormat(pattern = TimeUtils.DATE_FORMAT)
    private String endTime;

    @ApiModelProperty(value = "小程序AppKey")
    private String appKey;


}
