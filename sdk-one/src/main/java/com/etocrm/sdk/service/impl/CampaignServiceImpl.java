package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.db3dao.CampaignMapper;
import com.etocrm.sdk.entity.campaign.CampaignParamVO;
import com.etocrm.sdk.entity.campaign.CampaignReturnVO;
import com.etocrm.sdk.entity.campaign.GetPvUvByOneParamVO;
import com.etocrm.sdk.entity.campaign.GetPvUvByParamsVO;
import com.etocrm.sdk.service.CampaignService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Date 2021/6/8 11:23
 */
@Service
public class CampaignServiceImpl  implements CampaignService {

    @Resource
    private CampaignMapper campaignMapper;

    @Override
    public List<CampaignReturnVO> getPvUvByParams(GetPvUvByParamsVO getPvUvByParamsVO) {
        //页面访问数据
        List<CampaignReturnVO>campaignReturnVOList=new ArrayList<>();
        if(getPvUvByParamsVO.getPageType()==1){
            GetPvUvByOneParamVO getPvUvByOneParamVO = new GetPvUvByOneParamVO();
            BeanUtils.copyProperties(getPvUvByParamsVO,getPvUvByOneParamVO);
            if(getPvUvByParamsVO.getPageParamList().size()>0){
                for (CampaignParamVO campaignParamVO : getPvUvByParamsVO.getPageParamList()) {
                    getPvUvByOneParamVO.setPageParam(campaignParamVO);
                    CampaignReturnVO pvUvByParams = campaignMapper.getPvUvByParams(getPvUvByOneParamVO);
                    campaignReturnVOList.add(pvUvByParams);
                }
            }else{
                campaignReturnVOList.add(campaignMapper.getPvUvByParams(getPvUvByOneParamVO));
            }
        }
        return  campaignReturnVOList;
    }
}
