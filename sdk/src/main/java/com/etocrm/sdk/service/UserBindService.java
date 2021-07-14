package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.UserDataVO;
import com.etocrm.sdk.entity.YestDayVO;

import java.util.List;

public interface UserBindService {

    Result getUserBindDataList(UserDataVO vo);

    Result getUserBindDataTotal(UserDataVO vo);

    Result getUserVisitData(List<YestDayVO> vo);
}
