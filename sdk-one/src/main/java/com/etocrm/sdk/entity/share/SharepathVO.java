package com.etocrm.sdk.entity.share;

import lombok.Data;
import net.sf.oval.constraint.NotNull;

import java.util.List;

@Data
public class SharepathVO  {
    @NotNull
    private String appKey;

    @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    private String beginDate;
    @NotNull(message = "结束时间不能为空",profiles = "profile_4")
    private String endDate;

    //vistipath
    private List<String> list;

    private List<String> cityList;

}
