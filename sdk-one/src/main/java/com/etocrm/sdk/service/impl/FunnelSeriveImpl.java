package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.FunnelMapper;
import com.etocrm.sdk.entity.funnel.*;
import com.etocrm.sdk.entity.page.PageListResVO;
import com.etocrm.sdk.service.FunnelSerive;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FunnelSeriveImpl implements FunnelSerive {

    @Autowired
    private OvalUtils ovalUtils;

    @Autowired
    private FunnelMapper funnelMapper;


    @Override
    public Result getFunnelAnalysisList(FunnelVO funnelVO) {
        if(ovalUtils.validatorRequestParam(funnelVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        PageUtils<PageListResVO> pageUtil = new PageUtils<>(funnelVO.getPageSize(), funnelVO.getPageIndex(), 0);
        try {
            Integer pageCount = funnelMapper.getFunnelCount(funnelVO);
            pageUtil = new PageUtils<>(funnelVO.getPageSize(), funnelVO.getPageIndex(), pageCount);
            funnelVO.setPageIndex(pageUtil.getPageIndex());
            if (pageCount > 0) {
                List<FunnelRepVO> funnelList = funnelMapper.getFunnelPage(funnelVO);
                pageUtil.setLists(funnelList);
            } else {
                pageUtil.setLists(Collections.emptyList());
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);

        }
        return null;
    }

    @Override
    public Result addFunnel(FunnelAddVO funnelAddVO) {
        if(ovalUtils.validatorRequestParam(funnelAddVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        String id= UUID.randomUUID().toString().replace("-", "");
        FunnelIdVO funnelIdVO= new FunnelIdVO();
        BeanUtils.copyProperties(funnelAddVO,funnelIdVO);
        funnelIdVO.setId(id);
        log.info(JSON.toJSONString(funnelIdVO));
        List<FunnelSetpsVO> detailsList=funnelAddVO.getFunnelSetps();
        List<FunnelSetpsIdVO> detailsList1=new ArrayList<>();
        try{
            funnelMapper.insert(funnelIdVO);
            int stepId = 1;
            for(FunnelSetpsVO funnelSetpsVO:detailsList){
                funnelSetpsVO.setFunnelId(id);
                FunnelSetpsIdVO funnelSetpsIdVO= new FunnelSetpsIdVO();
                BeanUtils.copyProperties(funnelSetpsVO,funnelSetpsIdVO);
                funnelSetpsIdVO.setSetpId(stepId);
                stepId++;
                detailsList1.add(funnelSetpsIdVO);
            }
            funnelMapper.batchInsert(detailsList1);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return  Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success();
    }

    @Override
    public Result delFunnel(String id) {
        if (StringUtils.isEmpty(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        JSONObject j = JSONObject.parseObject(id);
        String id1 = j.getString("id");
        if(StringUtils.isNotBlank(id1)){
            return Result.success(ResponseCode.PARAMETERS_NULL);
        }
        funnelMapper.deleteId(id1);
        funnelMapper.deleteDetailId(id1);
        return Result.success();
    }

    @Override
    public Result editFunnel(FunnelEditVO funnelEditVO) {
        //
        if(ovalUtils.validatorRequestParam(funnelEditVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        try{
            funnelMapper.editFunnel(funnelEditVO);
            funnelMapper.deleteDetailId(funnelEditVO.getId());
            List<FunnelSetpsVO> detailsList = funnelEditVO.getFunnelSetps();
            List<FunnelSetpsIdVO> detailsList1=new ArrayList<>();
            int stepId = 1;
            for(FunnelSetpsVO funnelSetpsVO:detailsList){
                funnelSetpsVO.setFunnelId(funnelEditVO.getId());
                FunnelSetpsIdVO funnelSetpsIdVO= new FunnelSetpsIdVO();
                BeanUtils.copyProperties(funnelSetpsVO,funnelSetpsIdVO);

                funnelSetpsIdVO.setSetpId(stepId);
                stepId++;
                detailsList1.add(funnelSetpsIdVO);
            }
            funnelMapper.batchInsert(detailsList1);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success();
    }

    @Override
    public Result getSingleFunnel(FunnelOneVO funnelOneVO) {
        if(ovalUtils.validatorRequestParam(funnelOneVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        FunnelSingleRepVO funnelSingleRepVO=funnelMapper.getId(funnelOneVO.getId());
        if(funnelSingleRepVO!=null){
            List<FunnelSetpsIdVO> detailId = funnelMapper.getDetailId(funnelOneVO.getId());
            funnelSingleRepVO.setFunnelSetps(detailId);
        }
        return Result.success(funnelSingleRepVO);
    }

    @Override
    public List<FunnelRepExcelVO> downloadFunnelAnalysisList(DownLoadFunnelExcel vo) {
        return null;
    }

    @Override
    public Result getPageRate() {
        Map<String,Object> map=new HashMap<>();
        List<Map<String, Object>> funnPage = funnelMapper.getFunnPage(map);
        return Result.success(funnPage);
    }
}
