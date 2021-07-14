package com.etocrm.sdk.entity.QrStatistics;

import lombok.Data;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

@Data
public class QrCodeStatisticsVO {

    @NotBlank(message="AppId不能为空")
    private String appId;

    /*@DateTimeFormat(pattern= DateUtils.DATE_FORMAT_10)
    @NotNull(message = "开始日期不能为空")
    private Date beginDate;

    @DateTimeFormat(pattern= DateUtils.DATE_FORMAT_10)
    @NotNull(message = "结束日期不能为空")
    private Date endDate;*/

    @NotNull(message="appKey")
    private String appKey;

    @NotNull(message = "开始日期不能为空")
    private String beginDate;

    @NotNull(message = "结束日期不能为空")
    private String endDate;

}
