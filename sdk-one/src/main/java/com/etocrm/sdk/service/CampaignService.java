package com.etocrm.sdk.service;

import com.etocrm.sdk.entity.campaign.CampaignReturnVO;
import com.etocrm.sdk.entity.campaign.GetPvUvByParamsVO;

import java.util.List;

public interface CampaignService {

    List<CampaignReturnVO> getPvUvByParams(GetPvUvByParamsVO getPvUvByParamsVO);
}
