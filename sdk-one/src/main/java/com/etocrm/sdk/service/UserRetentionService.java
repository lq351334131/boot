package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.user.*;

import java.util.List;

public interface UserRetentionService {

    Result getUserNewAddRetentionTableAndChartData(DataBroadVO dataBroadVO);

    Result getUserActiveRetentionTableAndChartData(DataBroadVO dataBroadVO);

    List<UserNewAddRepVO> getUserNewAddExcel(DataBroadVO dataBroadVO);

    List<UserNewAddRepVO> getUserNewActiveExcel(DataBroadVO dataBroadVO);

    Result getUserBackflowRetentionTableAndChartData(DataBroadVO dataBroadVO);

    List<LostReturnRepVO> getUserBackflowRetentionExcel(DataBroadVO dataBroadVO);

    Result getUserPage(UserLostTypeVO userLostTypeVO);

    List<UserDatailVO> downloadUserBackflowRetentionInfos(UserLostTypeDowVO userLostTypeVO);
}
