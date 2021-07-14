package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.data.UserDataVO;
import com.etocrm.sdk.entity.databroad.DataBroadVO;

public interface DataService {

    Result getUserBindDataTotal(UserDataVO userDataVO);

    Result getUserBindDataList(UserDataVO userDataVO);

    Result getUserVisitData();

    String getStopTime(DataBroadVO dataBroadVO);

    String  getOpenStopTime(DataBroadVO dataBroadVO, Integer open);

}
