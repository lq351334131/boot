package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.user.*;

import java.util.List;

public interface UserSerice {

    Result getUserRegionData(UserVO userVO, String province);

    List<UserRegionRepVO> getUserExcelData(UserVO userVO, String province);

    Result getCity(UserVO userVO, String city);

    List<CityVO> getCityList(UserVO userVO, String city);

    Result getUserPhoneBrand(UserVO userVO, String brand);

    List<BrankVO>getBrandListExcel(UserVO userVO, String brand);

    Result getSystemList(UserVO userVO, String platform);

    List<SystemVO> getSystemListExcel(UserVO userVO, String platform);

    Result getUser(UserQueryVO userVO);

    Result getUserByBrand(UserQueryVO userQueryVO);

    Result getUserBySystem(UserQueryVO userQueryVO);

    Result getUserByCity(UserQueryVO userQueryVO);

    Result getUserStatisticsSummary(DataBroadVO dataBroadVO);

    Result getUserStatisticsDateDate(DataBroadVO dataBroadVO);

    List<UserDateRepVO> dowloadGetUserStatisticsDateDate(DataBroadVO dataBroadVO);

    Result getUserActiveStatisticsChart(UserPageVO userPageVO);

    Result getUserActiveStatisticsTable(UserVO userVO);

    List<UserActiveRepVO> getUserActiveexcel(UserVO userVO);

    Result getUserStatisticsGroupByDateSummary(UserPageVO userPageVO);

    Result getUserDetails(UserDetailsVO userDetailsVO);
}
