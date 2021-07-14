package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.user.*;
import com.etocrm.sdk.service.UserRetentionService;
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
@RequestMapping("/api/userRetention/")
@Api(tags = "用户留存")
@ApiSort(3)
public class UserRetentionController {

    @Autowired
    private UserRetentionService userRetentionService;


    @RequestMapping(value = "/getUserNewAddRetentionTableAndChartData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-新增留存", notes = "")
    @ApiOperationSupport(order = 1)
    public Result<List<UserNewAddRepVO>> getUserNewAddRetentionTableAndChartData(@RequestBody DataBroadVO dataBroadVO){
        return userRetentionService.getUserNewAddRetentionTableAndChartData(dataBroadVO);
    }

    @RequestMapping(value = "/getUserActiveRetentionTableAndChartData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户留存-活跃留存", notes = "")
    @ApiOperationSupport(order = 2)
    public Result<List<UserNewAddRepVO>> getUserActiveRetentionTableAndChartData(HttpServletResponse response, @RequestBody DataBroadVO dataBroadVO) {
        return userRetentionService.getUserActiveRetentionTableAndChartData(dataBroadVO);
    }

    @RequestMapping(value = "/getUserNewAddExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-新增留存下载", notes = "")
    @ApiOperationSupport(order = 3)
    public void getUserNewAddExcel(HttpServletResponse response,  @RequestBody  DataBroadVO dataBroadVO)  {
        List<UserNewAddRepVO> result = userRetentionService.getUserNewAddExcel(dataBroadVO);
        ExcelUtils.writeExcel(response,result,"用户留存-新留存下载", UserNewAddRepVO.class);
    }

    @RequestMapping(value = "/getUserActiveExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-活跃留存下载", notes = "")
    public void getUserActiveExcel(HttpServletResponse response,  @RequestBody  DataBroadVO dataBroadVO)  {
        List<UserNewAddRepVO> result = userRetentionService.getUserNewActiveExcel(dataBroadVO);
        ExcelUtils.writeExcel(response,result,"用户留存--活跃存下载", UserNewAddRepVO.class);
    }

    @RequestMapping(value = "/getUserBackflowRetentionTableAndChartData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "流失与回流", notes = "")
    @ApiOperationSupport(order = 5)
    public Result<List<LostReturnRepVO>> getUserBackflowRetentionTableAndChartData(@RequestBody  DataBroadVO dataBroadVO)  {
        return userRetentionService.getUserBackflowRetentionTableAndChartData(dataBroadVO);
    }

    @RequestMapping(value = "/getUserBackflowRetentionExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-流失与回流下载", notes = "")
    @ApiOperationSupport(order = 6)
    public void getUserBackflowRetentionExcel(HttpServletResponse response,  @RequestBody  DataBroadVO dataBroadVO)  {
        List<LostReturnRepVO> result = userRetentionService.getUserBackflowRetentionExcel(dataBroadVO);
        ExcelUtils.writeExcel(response,result,"用户留存-流失与回流下载", LostReturnRepVO.class);
    }

    @RequestMapping(value = "/getUserBackflowRetentionInfos", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-流失与回流用户-分页-不调用", notes = "")
    @ApiOperationSupport(order = 7)
    public Result<PageUtils<UserDatailVO>> getGetUserBackflowRetentionInfos(@RequestBody UserLostTypeVO userLostTypeVO)  {
        return userRetentionService.getUserPage(userLostTypeVO);
    }

    @RequestMapping(value = "/downloadUserBackflowRetentionInfos", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户流失与回流用户列表下载-不调用", notes = "")
    @ApiOperationSupport(order = 8)
    public void downloadUserBackflowRetentionInfos(HttpServletResponse response,  @RequestBody  UserLostTypeDowVO userLostTypeVO) {
        List<UserDatailVO> result = userRetentionService.downloadUserBackflowRetentionInfos(userLostTypeVO);
        ExcelUtils.writeExcel(response,result,"用户流失与回流用户列表下载", UserDatailVO.class);
    }

}
