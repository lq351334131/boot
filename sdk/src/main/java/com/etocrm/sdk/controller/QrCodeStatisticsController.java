package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcode.QrCodeStatisticsVO;
import com.etocrm.sdk.entity.qrcode.StatisticsChartOfQrCodeVO;
import com.etocrm.sdk.entity.qrcode.StatisticsTableOfQrCodeVO;
import com.etocrm.sdk.entity.qrcodegroup.DownLoadStatisticsTableOfQrGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrGroupChartVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrGroupStatisticsVO;
import com.etocrm.sdk.entity.qrcodegroup.StatisticsTableOfQrGroupVO;
import com.etocrm.sdk.service.QrCodeStatisticsService;
import com.etocrm.sdk.util.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @Date 2020/10/14 16:12
 */
@RestController
@RequestMapping("/api/QrCodeStatistics")
@Api(value = "二维码统计数据API")
public class QrCodeStatisticsController {
    @Autowired
    private QrCodeStatisticsService qrCodeStatisticsService;
    /**
     * @Description: 获取二维码统计概览
     */
    @ApiOperation(value = "获取二维码统计概览", notes = "获取二维码统计概览")
    @GetMapping(value = "/GetQrCodeStatistics")
    public Result getQrCodeStatistics(@Valid QrCodeStatisticsVO QrCodeStatisticsVO){
        return  this.qrCodeStatisticsService.getQrCodeStatistics(QrCodeStatisticsVO);
    }
    /**
     * @Description: 获取二维码统计图表显示（by天显示）
     */
    @ApiOperation(value = "获取二维码统计图表显示（by天显示）", notes = "获取二维码统计图表显示（by天显示）")
    @GetMapping(value = "/StatisticsChartOfQrCode")
    public Result statisticsChartOfQrCode(@Valid StatisticsChartOfQrCodeVO statisticsChartOfQrCodeVO){
        return  this.qrCodeStatisticsService.statisticsChartOfQrCode(statisticsChartOfQrCodeVO);
    }
    /**
     * @Description: 获取二维码统计表显示
     *
     * */
    @ApiOperation(value = "获取二维码统计表显示", notes = "获取二维码统计表显示")
    @GetMapping(value = "/StatisticsTableOfQrCode")
    public Result statisticsTableOfQrCode(@Valid StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO){
        return  this.qrCodeStatisticsService.StatisticsTableOfQrCode(statisticsTableOfQrCodeVO);
    }

    /**
     * @Description: 获取二维码组统计表显示
     *
     * */
    @ApiOperation(value = "获取二维码组统计表显示", notes = "获取二维码组统计表显示")
    @GetMapping(value = "/StatisticsTableOfQrGroup")
    public Result statisticsTableOfQrGroup(@Valid StatisticsTableOfQrGroupVO statisticsTableOfQrGroupVO){
        return  this.qrCodeStatisticsService.statisticsTableOfQrGroup(statisticsTableOfQrGroupVO);
    }
    /**
     * @Description: 获取二维码组统计概览
     *
     * */
    @ApiOperation(value = "获取二维码组统计概览", notes = "获取二维码组统计概览")
    @GetMapping(value = "/GetQrGroupStatistics")
    public Result getQrGroupStatistics(@Valid GetQrGroupStatisticsVO getQrGroupStatisticsVO){
        return  this.qrCodeStatisticsService.getQrGroupStatistics(getQrGroupStatisticsVO);
    }

    /**
     * @Description: 获取二维码组折线图统计
     *
     * */
    @ApiOperation(value = "获取二维码组折线图统计", notes = "获取二维码组折线图统计")
    @GetMapping(value = "/GetQrGroupChart")
    public Result getQrGroupChart(@Valid GetQrGroupChartVO getQrGroupChartVO){
        return  this.qrCodeStatisticsService.getQrGroupChart(getQrGroupChartVO);
    }
    /**
     * @Description: 二维码组统计列表下载
     *
     * */
    @ApiOperation(value = "二维码组统计列表下载", notes = "二维码组统计列表下载")
    @GetMapping(value = "/DownLoadStatisticsTableOfQrGroup")
    public void DownLoadStatisticsTableOfQrGroup(@Valid StatisticsTableOfQrGroupVO statisticsTableOfQrGroupVO, HttpServletResponse response) throws Exception {
        List<DownLoadStatisticsTableOfQrGroupVO> list =this.qrCodeStatisticsService.DownLoadStatisticsTableOfQrGroup(statisticsTableOfQrGroupVO);
        ExcelUtils.writeExcel(response,list,"二维码组统计列表下载",DownLoadStatisticsTableOfQrGroupVO.class);
    }
    /**
     * @Description: 二维码统计表下载
     *
     * */
    @ApiOperation(value = "二维码统计表下载", notes = "二维码统计表下载")
    @GetMapping(value = "/DownLoadStatisticsTableOfQrCode")
    public Result DownLoadStatisticsTableOfQrCode(@Valid StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO){
        return  this.qrCodeStatisticsService.DownLoadStatisticsTableOfQrCode(statisticsTableOfQrCodeVO);
    }
}
