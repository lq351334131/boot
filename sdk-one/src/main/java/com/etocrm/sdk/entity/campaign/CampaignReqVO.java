package com.etocrm.sdk.entity.campaign;

import lombok.Data;

import java.util.List;

@Data
public class CampaignReqVO {

    private TotalVO totalVO;

    private List<ParamList> params;

    private List<DetailVO> details;
}
