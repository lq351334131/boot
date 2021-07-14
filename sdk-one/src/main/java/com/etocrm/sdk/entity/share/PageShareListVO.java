package com.etocrm.sdk.entity.share;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import net.sf.oval.constraint.NotNull;

@Data
@ApiModel(value = "分享")
public class PageShareListVO {

    @NotNull
    private String appKey;

   /* @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
*/
    private String url;

    private String name;

    @NotNull(message = "开始时间不能为空",profiles = "profile_4")
    private String beginDate;

    @NotNull(message = "结束时间不能为空",profiles = "profile_4")
    private String endDate;
}
