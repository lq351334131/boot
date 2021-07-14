package com.etocrm.sdk.entity.QrStatistics;

import lombok.Data;

@Data
public class QrCodeStatisticsPageVO extends QrCodeStatisticsVO {

    private Integer pageIndex=1;

    private Integer pageSize=10;

}
