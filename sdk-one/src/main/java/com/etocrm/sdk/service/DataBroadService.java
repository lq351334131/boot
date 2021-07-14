package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.databroad.TotalDataRepVO;

public interface DataBroadService {
    Result portalData(DataBroadVO dataBroadVO);

    TotalDataRepVO getTotalDataRepVO(DataBroadVO dataBroadVO);
}
