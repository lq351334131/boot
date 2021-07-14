package com.etocrm.sdk.entity.campaign;

import lombok.Data;
import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
public class CampaignVO {

    @NotNull
    private String appKey;

    @NotNull(message = "开始时间不能为空")
    private String beginDate;

    @NotNull(message = "结束时间不能为空")
    private String endDate;


    //@NotNull(message = "页面路径")
    private String visitpath;

    @NotNull(message = "页面路径参数")
    private List<CampaignParamVO> visList;

   /* @NotNull(message = "tv")
    private String tv;

    //@NotNull(message = "tl")
    private String tl;*/

    //@NotNull(message = "事件参数")
   // private List<CompionParamVO> eventList;

    /**
     *
     * @Description type:1页面，2事件
     * @author xing.liu
     * @date 2021/6/3
     **/
    @NotNull()
    @Max(3)
    @Min(1)
    private Integer type;




}
