package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.QrStatistics.*;
import com.etocrm.sdk.service.QrCodeStatisticsService;
import com.etocrm.sdk.util.ExcelUtils;
import com.etocrm.sdk.util.PageUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/qrCodeStatistics")
@Api(tags = "二维码统计数据API-不调用")
@ApiSort(10)
public class QrCodeStatisticsController {

    @Autowired
    private QrCodeStatisticsService qrCodeStatisticsService;
    /**
     * @Description: 获取二维码统计概览
     * */
    @ApiOperationSupport(order = 1)
    @PostMapping(value = "/getQrCodeStatistics",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取二维码统计概览", notes = "")
    public Result<QrCodeStatisticsRepVO> getQrCodeStatistics(@RequestBody QrCodeStatisticsVO qrCodeStatisticsVO){
        return  qrCodeStatisticsService.getQrCodeStatistics(qrCodeStatisticsVO);
    }

    /**
    * @Description 统计/获取二维码组统计概览
    * @author xing.liu
    * @date 2020/12/8
    **/
    @ApiOperationSupport(order = 2)
    @PostMapping(value = "/getQrGroupStatistics",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "统计/获取二维码组统计概览", notes = "")
    public Result<QrGroupStatisticsRepVO> getQrGroupStatistics(@RequestBody QrCodeStatisticsVO qrCodeStatisticsVO){
        return  qrCodeStatisticsService.getQrGroupStatistics(qrCodeStatisticsVO);
    }

    /**
     * @Description 统计/二维码统计图显示
     * @author xing.liu
     * @date 2020/12/8
     **/
    @ApiOperation(value = "统计/二维码统计图显示", notes = "")
    @ApiOperationSupport(order = 3)
    @PostMapping(value = "/statisticsChartOfQrCode",produces = "application/json;charset=UTF-8")
    public Result<List<QrCodeDateRepVO>> getStatisticsChartOfQrCode(@RequestBody QrCodeStatisticsVO qrCodeStatisticsVO){
        return  qrCodeStatisticsService.getStatisticsChartOfQrCode(qrCodeStatisticsVO);
    }

    /**
     * @Description 统计/二维码统计图显示
     * @author xing.liu
     * @date 2020/12/8
     **/
    @PostMapping(value = "/getQrGroupChart",produces = "application/json;charset=UTF-8")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "统计/获取二维码组图表显示", notes = "")
    public Result<List<QrGroupDateRepVO>> getQrGroupChart(@RequestBody QrCodeStatisticsVO qrCodeStatisticsVO){
        return  qrCodeStatisticsService.getQrGroupChart(qrCodeStatisticsVO);
    }

    /**
     * @Description 统计/二维码统计表显示
     * @author xing.liu
     * @date 2020/12/8
     **/
    @ApiOperationSupport(order = 5)
    @PostMapping(value = "/statisticsTableOfQrCode",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "统计/二维码统计表显示", notes = "")
    public Result<PageUtils<StatisticsTableOfQrCodeVO>> getStatisticsTableOfQrCode(@RequestBody QrCodeStatisticsPageVO qrCodeStatisticsPageVO){
        return  qrCodeStatisticsService.getStatisticsTableOfQrCode(qrCodeStatisticsPageVO);
    }

    @RequestMapping(value = "/downLoadStatisticsTableOfQrCode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "二维码统计表显示下载", notes = "")
    @ApiOperationSupport(order = 6)
    public void downLoadStatisticsTableOfQrCode(HttpServletResponse response, @RequestBody QrCodeStatisticsVO qrCodeStatisticsVO) {
        List<StatisticsTableOfQrCodeVO> list=qrCodeStatisticsService.downLoadStatisticsTableOfQrCode(qrCodeStatisticsVO);
        ExcelUtils.writeExcel(response, list, "二维码统计表显示下载", StatisticsTableOfQrCodeVO.class);
    }

    @PostMapping(value = "/statisticsTableOfQrGroup",produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "统计/获取二维码组统计列表数据", notes = "")
    @ApiOperationSupport(order = 7)
    public Result<PageUtils<StatisticsTableOfQrGroupVO>> getStatisticsTableOfQrGroup(@RequestBody QrCodeStatisticsPageVO qrCodeStatisticsPageVO){
        return  qrCodeStatisticsService.getStatisticsTableOfQrGroup(qrCodeStatisticsPageVO);
    }

    @RequestMapping(value = "/downLoadStatisticsTableOfQrGroup", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "二维码组统计列表数据下载", notes = "")
    @ApiOperationSupport(order = 8)
    public void downLoadStatisticsTableOfQrGroup(HttpServletResponse response, @RequestBody QrCodeStatisticsVO qrCodeStatisticsVO) {
        List<StatisticsTableOfQrGroupVO> list=qrCodeStatisticsService.downLoadStatisticsTableOfQrGroup(qrCodeStatisticsVO);
        ExcelUtils.writeExcel(response, list, "二维码组统计列表数据下载", StatisticsTableOfQrGroupVO.class);
    }














}
