package com.etocrm.sdk.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.scene.SceneTotalDetailDomainRepVO;
import com.etocrm.sdk.entity.scene.SceneVO;
import com.etocrm.sdk.entity.user.*;
import com.etocrm.sdk.service.UserService;
import com.etocrm.sdk.util.ExcelUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "数用户趋势", notes = "")
    @RequestMapping(value = "/getUserStatisticsSummary", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getUserStatisticsSummary(@RequestBody UserVO vo) {
        return  userService.getUserStatisticsSummary(vo);
    }


    @ApiOperation(value = "用户趋势-明细数据", notes = "")
    @RequestMapping(value = "/getUserStatisticsDateDate", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getUserStatisticsDateDate(@RequestBody UserVO vo) {
        return  userService.getUserStatisticsDateDate(vo);
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户趋势-明细数据下载", notes = "")
    public void getexcel(HttpServletResponse response, @RequestBody UserVO vo)throws Exception{
        List<UserDateRepVO> event = userService.getexcel(vo);
        ExcelUtils.writeExcel(response,event,"数用户趋势下载", UserDateRepVO.class);
    }

    @RequestMapping(value = "/getUserRegionData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-省份", notes = "")
    public Result getUserRegionData(HttpServletResponse response, @RequestBody UserVO vo) throws Exception {
        return userService.getUserRegionData(vo,"province");
    }

    @RequestMapping(value = "/getUserCityData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-城市", notes = "")
    public Result getUserCityData(HttpServletResponse response, @RequestBody UserVO vo) throws Exception {
        return userService.getUserRegionData(vo,"city");
    }

    @RequestMapping(value = "/getUserPhoneBrand", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-手机品牌", notes = "")
    public Result getUserPhoneBrand(HttpServletResponse response, @RequestBody UserVO vo) throws Exception {
        return userService.getUserRegionData(vo,"brand");
    }

    @RequestMapping(value = "/GetUserPhoneSystem", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-手机操作系统", notes = "")
    public Result GetUserPhoneSystem(HttpServletResponse response, @RequestBody UserVO vo) throws Exception {
        return userService.getUserRegionData(vo,"platform");
    }

    @RequestMapping(value = "/getCityexcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-城市下载", notes = "")
    public void getCityexcel(HttpServletResponse response,  @RequestBody UserVO vo) throws Exception {
        List<UserRegionRepVO> event = userService.getUserExcelData(vo,"city");
        List<CityVO> result=new ArrayList<>();
        BeanUtils.copyProperties(event,result);
        ExcelUtils.writeExcel(response,result,"用户画像城市下载", CityVO.class);
    }

    @RequestMapping(value = "/getProvinceexcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-省份下载", notes = "")
    public void getProvinceexcel(HttpServletResponse response,  @RequestBody UserVO vo) throws Exception {
        List<UserRegionRepVO> result = userService.getUserExcelData(vo,"province");
        ExcelUtils.writeExcel(response,result,"用户画像省份下载", UserRegionRepVO.class);
    }
    @RequestMapping(value = "/getBrandexcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-品牌下载", notes = "")
    public void getBrandexcel(HttpServletResponse response, @RequestBody UserVO vo) throws Exception {
        List<UserRegionRepVO> result = userService.getUserExcelData(vo,"brand");
        ExcelUtils.writeExcel(response,result,"用户画像品牌下载", UserRegionRepVO.class);
    }

    @RequestMapping(value = "/getPlatformexcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-系统下载", notes = "")
    public void getPlatformexcel(HttpServletResponse response,  @RequestBody  UserVO vo) throws Exception {
        List<UserRegionRepVO> result = userService.getUserExcelData(vo,"platform");
        ExcelUtils.writeExcel(response,result,"用户画像系统下载", UserRegionRepVO.class);
    }

    @RequestMapping(value = "/userActiveStatisticsChart", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户活跃度-图数据", notes = "")
    public String userActiveStatisticsChart(HttpServletResponse response, @RequestBody UserVO vo){
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("dwRate");
        filter.getExcludes().add("dmRate");
        String result = JSONObject.toJSONString(userService.userActiveStatisticsChart(vo), filter);//object是Java对象
        return result ;
    }

    @RequestMapping(value = "/userActiveStatisticsTable", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户活跃度-明细数据", notes = "")
    public Result userActiveStatisticsTable(HttpServletResponse response, @RequestBody UserVO vo){
        return userService.userActiveStatisticsTable(vo);
    }

    @RequestMapping(value = "/getUserActiveexcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户活跃度-明细下载", notes = "")
    public void getUserActiveexcel(HttpServletResponse response,  @RequestBody  UserVO vo) throws Exception {
        List<UserActiveRepVO> result = userService.getUserActiveexcel(vo);
        ExcelUtils.writeExcel(response,result,"用户活跃度明细下载", UserActiveRepVO.class);
    }

    @RequestMapping(value = "/getUserNewAddRetentionTableAndChartData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-新增留存", notes = "")
    public Result getUserNewAddRetentionTableAndChartData( @RequestBody UserVO vo){
        return userService.getUserNewAddRetentionTableAndChartData(vo);
    }
    @RequestMapping(value = "/getUserActiveRetentionTableAndChartData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-活跃留存", notes = "")
    public Result getUserActiveRetentionTableAndChartData(HttpServletResponse response, @RequestBody UserVO vo) throws Exception {
        return userService.getUserActiveRetentionTableAndChartData(vo);
    }

    @RequestMapping(value = "/getUserNewAddExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-新增留存", notes = "")
    public void getUserNewAddExcel(HttpServletResponse response,  @RequestBody  UserVO vo) throws Exception {
        List<UserNewAddRepVO> result = userService.getUserNewAddExcel(vo);
        ExcelUtils.writeExcel(response,result,"用户留存-新留存下载", UserNewAddRepVO.class);
    }

    @RequestMapping(value = "/getUserNewActiveExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-活跃留存", notes = "")
    public void getUserNewActiveExcel(HttpServletResponse response,  @RequestBody  UserVO vo) throws Exception {
        List<UserNewAddRepVO> result = userService.getUserNewActiveExcel(vo);
        ExcelUtils.writeExcel(response,result,"用户留存-新留存下载", UserNewAddRepVO.class);
    }


    @RequestMapping(value = "/getUserBackflowRetentionTableAndChartData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-流失与回流", notes = "")
    public Result getUserBackflowRetentionTableAndChartData(@RequestBody  UserVO vo)  {
       return userService.getUserBackflowRetentionTableAndChartData(vo);
    }

    @RequestMapping(value = "/getUserBackflowRetentionExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户留存-流失与回流", notes = "")
    public void getUserBackflowRetentionExcel(HttpServletResponse response,  @RequestBody  UserVO vo) throws Exception {
        List<LostReturnRepVO> result = userService.getUserBackflowRetentionExcel(vo);
        ExcelUtils.writeExcel(response,result,"用户留存-流失与回流下载", LostReturnRepVO.class);
    }

    @RequestMapping(value = "/getGetUserBackflowRetentionInfos", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户列表", notes = "")
    public Result getGetUserBackflowRetentionInfos(@RequestBody  UserLostTypeVO vo)  {
        return userService.getUser(vo);
    }





















}

