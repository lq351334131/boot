package com.etocrm.sdk.entity.qrcode;

import com.alibaba.excel.util.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Date 2020/10/15 15:14
 */
@Data
@Api(value="获取二维码统计图表VO")
public class StatisticsChartOfQrCodeVO {
    @NotBlank(message="AppId不能为空")
    @ApiModelProperty(value = "小程序id")
    private String appId;

    @DateTimeFormat(pattern= DateUtils.DATE_FORMAT_10)
    @ApiModelProperty(value = "开始日期")
    @NotNull(message = "开始日期不能为空")
    private Date beginDate;

    @DateTimeFormat(pattern= DateUtils.DATE_FORMAT_10)
    @ApiModelProperty(value = "结束日期")
    @NotNull(message = "结束日期不能为空")
    private Date endDate;
}
