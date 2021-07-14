package com.etocrm.sdk.db3dao;

import com.etocrm.sdk.entity.campaign.CampaignReturnVO;
import com.etocrm.sdk.entity.campaign.GetPvUvByOneParamVO;

/**
 * @Date 2021/6/10 11:25
 */
public interface CampaignMapper {
    CampaignReturnVO getPvUvByParams(GetPvUvByOneParamVO getPvUvByOneParamVO);
}
