package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.user.*;

import java.util.List;

public interface UserService {

    Result getUserStatisticsSummary(UserVO vo);

    Result getUserStatisticsDateDate(UserVO vo);

    List<UserDateRepVO> getexcel(UserVO vo);

    Result getUserRegionData(UserVO vo,String field);

    List<UserRegionRepVO> getUserExcelData(UserVO vo, String field);

    Result userActiveStatisticsChart(UserVO vo);

    List<UserActiveRepVO> getUserActiveexcel(UserVO vo);

    Result userActiveStatisticsTable(UserVO vo);

    Result getUserNewAddRetentionTableAndChartData(UserVO vo);

    Result getUserActiveRetentionTableAndChartData(UserVO vo);

    List<UserNewAddRepVO> getUserNewAddExcel(UserVO vo);

    List<UserNewAddRepVO> getUserNewActiveExcel(UserVO vo);

    Result getUserBackflowRetentionTableAndChartData(UserVO vo);

    List<LostReturnRepVO> getUserBackflowRetentionExcel(UserVO vo);

    Result getUser(UserLostTypeVO vo);
}
