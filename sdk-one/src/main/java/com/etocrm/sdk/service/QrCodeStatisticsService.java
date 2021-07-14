package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.QrStatistics.QrCodeStatisticsPageVO;
import com.etocrm.sdk.entity.QrStatistics.QrCodeStatisticsVO;
import com.etocrm.sdk.entity.QrStatistics.StatisticsTableOfQrCodeVO;
import com.etocrm.sdk.entity.QrStatistics.StatisticsTableOfQrGroupVO;

import java.util.List;

public interface QrCodeStatisticsService {

    Result getQrCodeStatistics(QrCodeStatisticsVO qrCodeStatisticsVO);

    Result getQrGroupStatistics(QrCodeStatisticsVO qrCodeStatisticsVO);

    Result getStatisticsChartOfQrCode(QrCodeStatisticsVO qrCodeStatisticsVO);

    Result getQrGroupChart(QrCodeStatisticsVO qrCodeStatisticsVO);

    Result getStatisticsTableOfQrCode(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);

    List<StatisticsTableOfQrCodeVO> downLoadStatisticsTableOfQrCode(QrCodeStatisticsVO qrCodeStatisticsVO);

    List<StatisticsTableOfQrGroupVO> downLoadStatisticsTableOfQrGroup(QrCodeStatisticsVO qrCodeStatisticsVO);

    Result getStatisticsTableOfQrGroup(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);

}
