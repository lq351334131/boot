package com.etocrm.sdk.controller;


import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.dataapi.*;
import com.etocrm.sdk.service.DataApiService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description 求pv,uv
 * @author xing.liu
 * @date 2021/6/7
 **/
@RestController
@RequestMapping("/api/realtime")
@Api(tags = "实时API")
@ApiSort(13)
public class RealtimeApiController {

    @Autowired
    private DataApiService dataApiService;


    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "页面路径", notes = "")
    @RequestMapping(value = "/getVisitpath", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<DataApiReqVO>> getVisit() {
        return  dataApiService.getVisit();
    }

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "人数", notes = "")
    @RequestMapping(value = "/getTotal", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<TotalReqVO>> getTotal() {
        return  dataApiService.getTotal();
    }


    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "每天", notes = "")
    @RequestMapping(value = "/getEveryday", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<EverydayReqVO>> getEveryday() {
        return  dataApiService.getEveryday();
    }

    /*@ApiOperation(value = "省份", notes = "")
    @RequestMapping(value = "/getReg", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getReg() {
        return  dataApiService.getReg();
    }*/

    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "场景值", notes = "")
    @RequestMapping(value = "/getScene", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<SceneReqVO>> getScene() {
        return  dataApiService.getScene();
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "二维码", notes = "")
    @RequestMapping(value = "/getQrCode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<SceneReqVO>> getQrCode() {
        return  dataApiService.getQrCode();
    }

    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "事件", notes = "")
    @RequestMapping(value = "/getEvent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<EventReqVO>> getEvent() {
        return  dataApiService.getEvent();
    }


    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "热搜", notes = "")
    @RequestMapping(value = "/getSearch", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<OnlineReqVO>> getSearch() {
        return  dataApiService.getSearch();
    }


    @ApiOperation(value = "门店", notes = "")
    @ApiOperationSupport(order = 8)
    @RequestMapping(value = "/getShop", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<OnlineReqVO>> getShop() {
        return  dataApiService.getShop();
    }

    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "页面路径分享", notes = "")
    @RequestMapping(value = "/getShare", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<DataApiReqVO>> getShare() {
        return  dataApiService.getShare();
    }


    @ApiOperation(value = "分享总数据", notes = "")
    @ApiOperationSupport(order = 10)
    @RequestMapping(value = "/getShareTotal", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<TotalReqVO>> getShareTotal() {
        return  dataApiService.getShareTotal();
    }

    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "入口页", notes = "")
    @RequestMapping(value = "/getEntryTotal", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<EntryReqVO>> getEntryTotal() {
        return  dataApiService.getEntryTotal();
    }


    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "打开次数", notes = "")
    @RequestMapping(value = "/getOpenTotal", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<OpenReqVO>> getOpenTotal() {
        return  dataApiService.getOpenTotal();
    }

    //@ApiOperation(value = "打开次数明细", notes = "")
    @RequestMapping(value = "/getOpenDetail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<OpenDetailReqVO>> getOpenDetail() {
        return  dataApiService.getOpenDetail();
    }






























}
