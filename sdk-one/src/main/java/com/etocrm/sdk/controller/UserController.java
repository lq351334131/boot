package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.share.SharePageRepVO;
import com.etocrm.sdk.entity.user.*;
import com.etocrm.sdk.service.UserSerice;
import com.etocrm.sdk.util.ExcelUtils;
import com.etocrm.sdk.util.PageUtils;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/userStatistics/")
@Api(tags = "用户趋势")
@ApiSort(4)
public class UserController {

    @Autowired
    private UserSerice userService;

    @RequestMapping(value = "/getUserRegionData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-省份", notes = "")
    public Result<List<UserRegionRepVO>> getUserRegionData(HttpServletResponse response, @RequestBody UserVO userVO)  {
        return userService.getUserRegionData(userVO,"province");
    }

    @RequestMapping(value = "/getProvinceexcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-省份下载", notes = "")
    public void getProvinceexcel(HttpServletResponse response,  @RequestBody UserVO userVO)  {
        List<UserRegionRepVO> result = userService.getUserExcelData(userVO,"province");
        ExcelUtils.writeExcel(response,result,"用户画像省份下载", UserRegionRepVO.class);
    }


    @RequestMapping(value = "/getUserCityData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-城市", notes = "")
    public Result<List<CityVO>> getUserCityData(HttpServletResponse response, @RequestBody UserVO userVO)  {
        return userService.getCity(userVO,"city");
    }

    @RequestMapping(value = "/getCityExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-城市下载", notes = "")
    public void getCityExcel(HttpServletResponse response,  @RequestBody UserVO userVO)  {
        List<CityVO> result = userService.getCityList(userVO,"city");
        ExcelUtils.writeExcel(response,result,"用户画像-城市下载", CityVO.class);
    }

    @RequestMapping(value = "/getUserPhoneBrand", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-手机品牌", notes = "")
    public Result<List<BrankVO>> getUserPhoneBrand(HttpServletResponse response, @RequestBody UserVO userVO)  {
        return userService.getUserPhoneBrand(userVO,"brand");
    }

    @RequestMapping(value = "/getBrandExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-品牌下载", notes = "")
    public void getBrandExcel(HttpServletResponse response,  @RequestBody UserVO userVO)  {
        List<BrankVO> result = userService.getBrandListExcel(userVO,"brand");
        ExcelUtils.writeExcel(response,result,"用户画像-城市下载", BrankVO.class);
    }

    @RequestMapping(value = "/getUserPhoneSystem", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-手机操作系统", notes = "")
    public Result<List<SystemVO>> getUserPhoneSystem(HttpServletResponse response, @RequestBody UserVO userVO)  {
        return userService.getSystemList(userVO,"platForm");
    }

    @RequestMapping(value = "/getUserPhoneSystemExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户画像-系统下载", notes = "")
    public void getUserPhoneSystemExcel(HttpServletResponse response,  @RequestBody UserVO userVO)  {
        List<SystemVO> result = userService.getSystemListExcel(userVO,"platForm");
        ExcelUtils.writeExcel(response,result,"用户画像系统下载", SystemVO.class);
    }

    @RequestMapping(value = "/getUserByProvince", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "省份用户-不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getUserByProvince(HttpServletResponse response, @RequestBody UserQueryVO userQueryVO)  {
        return userService.getUser(userQueryVO);
    }

    @RequestMapping(value = "/getUserByBrand", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "品牌用户-不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getUserByBrand(HttpServletResponse response, @RequestBody UserQueryVO userQueryVO)  {
        return userService.getUserByBrand(userQueryVO);
    }

    @RequestMapping(value = "/getUserByCity", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "城市用户--不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getUserByCity(HttpServletResponse response, @RequestBody UserQueryVO userQueryVO)  {
        return userService.getUserByCity(userQueryVO);
    }

    @RequestMapping(value = "/getUserBySystem", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "系统用户--不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getUserBySystem(HttpServletResponse response, @RequestBody UserQueryVO userQueryVO)  {
        return userService.getUserBySystem(userQueryVO);
    }


    @RequestMapping(value = "/getUserStatisticsDateDate", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户分析界面统计汇总日期列表明细", notes = "")
    public Result<List<UserDateRepVO>> getUserStatisticsDateDate(HttpServletResponse response,  @RequestBody DataBroadVO dataBroadVO) {
        return  userService.getUserStatisticsDateDate(dataBroadVO);
    }

    @RequestMapping(value = "/dowloadGetUserStatisticsDateDate", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户分析界面统计下载", notes = "")
    public void dowloadGetUserStatisticsDateDate(HttpServletResponse response,  @RequestBody DataBroadVO dataBroadVO)  {
        List<UserDateRepVO> userDateRepVOS = userService.dowloadGetUserStatisticsDateDate(dataBroadVO);
        ExcelUtils.writeExcel(response,userDateRepVOS,"用户分析界面统计下载", UserDateRepVO.class);
    }

    @RequestMapping(value = "/userActiveStatisticsChart", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户活跃度-图数据-不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> userActiveStatisticsChart(HttpServletResponse response, @RequestBody UserPageVO userPageVO){
        return userService.getUserActiveStatisticsChart(userPageVO);
    }

    @RequestMapping(value = "/userActiveStatisticsTable", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户活跃度-明细数据", notes = "")
    public Result<List<UserActiveRepVO>> userActiveStatisticsTable(HttpServletResponse response, @RequestBody UserVO userVO){
        return userService.getUserActiveStatisticsTable(userVO);
    }

    @RequestMapping(value = "/downloadUserActiveData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "用户活跃度-明细下载", notes = "")
    public void downloadUserActiveData(HttpServletResponse response,  @RequestBody  UserVO userVO)  {
        List<UserActiveRepVO> result = userService.getUserActiveexcel(userVO);
        ExcelUtils.writeExcel(response,result,"用户活跃度明细下载", UserActiveRepVO.class);
    }


    @RequestMapping(value = "/getUserStatisticsGroupByDateSummary", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户分析界面统计汇总日期明细", notes = "")
    public Result<PageUtils<SharePageRepVO>> GetUserStatisticsGroupByDateSummary(HttpServletResponse response, @RequestBody UserPageVO userPageVO)  {
        return userService.getUserStatisticsGroupByDateSummary(userPageVO);
    }

    @RequestMapping(value = "/getUserStatisticsSummary", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户分析界面统计汇总", notes = "")
    public Result<UserSummaryVO> getUserStatisticsSummary(HttpServletResponse response, @RequestBody DataBroadVO dataBroadVO)  {
        return userService.getUserStatisticsSummary(dataBroadVO);
    }

    @RequestMapping(value = "/getUserDetails", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "获取用户详情-不调用", notes = "")
    public Result<List<UserDetailsReqVO>> getUserDetails(HttpServletResponse response, @RequestBody UserDetailsVO userDetailsVO)  {
        return userService.getUserDetails(userDetailsVO);
    }

}
