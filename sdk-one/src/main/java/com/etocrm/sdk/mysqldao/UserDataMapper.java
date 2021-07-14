package com.etocrm.sdk.mysqldao;

import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.user.UserSummaryVO;

public interface UserDataMapper {
    UserSummaryVO getUserStatisticsSummary(DataBroadVO dataBroadVO);
}
