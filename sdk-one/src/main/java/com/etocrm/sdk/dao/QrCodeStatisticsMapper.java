package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.QrStatistics.*;

import java.util.List;
import java.util.Map;

public interface QrCodeStatisticsMapper {

    QrCodeStatisticsRepVO getQrCodeStatistics(QrCodeStatisticsVO qrCodeStatisticsVO);

    Integer getNewUser(QrCodeStatisticsVO qrCodeStatisticsVO);

    QrGroupStatisticsRepVO getQrGroupStatistics(QrCodeStatisticsVO qrCodeStatisticsVO);

    Integer getQrGroupNewUser(QrCodeStatisticsVO qrCodeStatisticsVO);

    List<QrCodeDateRepVO> getQrCodeDate(QrCodeStatisticsVO qrCodeStatisticsVO);

    List<QrCodeDateRepVO> getQrCodeNewUserDate(QrCodeStatisticsVO qrCodeStatisticsVO);

    List<QrGroupDateRepVO> getQrGroupDate(QrCodeStatisticsVO qrCodeStatisticsVO);

    List<QrGroupDateRepVO> getQrGroupNewUserDate(QrCodeStatisticsVO qrCodeStatisticsVO);

    Integer getStatisticsTableOfQrCodeCount(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);

    List<StatisticsTableOfQrCodeVO> getStatisticsTableOfQrCode(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);

    List<StatisticsTableOfQrCodeVO> downLoadStatisticsTableOfQrCode(QrCodeStatisticsVO qrCodeStatisticsVO);

    List<StatisticsTableOfQrGroupVO>  getStatisticsTableOfQrGroup(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);

    List<StatisticsTableOfQrGroupVO> getStatisticsTableOfQrGroupExcel(QrCodeStatisticsVO qrCodeStatisticsPageVO);

    Integer getStatisticsTableOfQrGroupCount(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);

    List<Map<String,Object>> getVisitNewUser(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);

    List<Map<String,Object>> getVisitNewUserGroup(QrCodeStatisticsPageVO qrCodeStatisticsPageVO);
}
