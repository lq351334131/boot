package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcode.QrCodeStatisticsVO;
import com.etocrm.sdk.entity.qrcode.StatisticsChartOfQrCodeVO;
import com.etocrm.sdk.entity.qrcode.StatisticsTableOfQrCodeVO;
import com.etocrm.sdk.entity.qrcodegroup.DownLoadStatisticsTableOfQrGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrGroupChartVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrGroupStatisticsVO;
import com.etocrm.sdk.entity.qrcodegroup.StatisticsTableOfQrGroupVO;

import java.util.List;

public interface QrCodeStatisticsService {

    Result getQrCodeStatistics(QrCodeStatisticsVO qrCodeStatisticsVO);

    Result statisticsChartOfQrCode(StatisticsChartOfQrCodeVO statisticsChartOfQrCodeVO);

    Result StatisticsTableOfQrCode(StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO);

    Result statisticsTableOfQrGroup(StatisticsTableOfQrGroupVO statisticsTableOfQrGroupVO);

    Result getQrGroupStatistics(GetQrGroupStatisticsVO getQrGroupStatisticsVO);

    Result getQrGroupChart(GetQrGroupChartVO getQrGroupChartVO);

    List<DownLoadStatisticsTableOfQrGroupVO> DownLoadStatisticsTableOfQrGroup(StatisticsTableOfQrGroupVO statisticsTableOfQrGroupVO);

    Result DownLoadStatisticsTableOfQrCode(StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO);
}
