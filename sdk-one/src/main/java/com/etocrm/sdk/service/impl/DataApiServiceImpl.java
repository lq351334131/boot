package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.DataApiMapper;
import com.etocrm.sdk.entity.dataapi.*;
import com.etocrm.sdk.service.DataApiService;
import com.etocrm.sdk.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataApiServiceImpl implements DataApiService {

    @Autowired
    private DataApiMapper dataApiMapper;


    @Override
    public Result getVisit() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<DataApiReqVO> visit = dataApiMapper.getVisit(dataApiVO);
        return Result.success(visit);
    }

    @Override
    public Result getTotal() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<TotalReqVO> visit = dataApiMapper.getTotal(dataApiVO);
        return Result.success(visit);
    }

    @Override
    public Result getEveryday() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<EverydayReqVO> visit = dataApiMapper.getEveryday(dataApiVO);
        return Result.success(visit);
    }

    @Override
    public Result getReg() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<RegReqVO> reg = dataApiMapper.getReg(dataApiVO);
        return Result.success(reg);
    }

    @Override
    public Result getScene() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<SceneReqVO> scene = dataApiMapper.getScene(dataApiVO);
        return Result.success(scene);
    }

    @Override
    public Result getQrCode() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<SceneReqVO> scene = dataApiMapper.getQrCode(dataApiVO);
        return Result.success(scene);
    }

    @Override
    public Result getEvent() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<EventReqVO> scene = dataApiMapper.getEvent(dataApiVO);
        return Result.success(scene);
    }

    @Override
    public Result getShop() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<OnlineReqVO> scene = dataApiMapper.getShop(dataApiVO);
        return Result.success(scene);
    }

    @Override
    public Result getSearch() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<OnlineReqVO> scene = dataApiMapper.getSearch(dataApiVO);
        return Result.success(scene);
    }

    @Override
    public Result getShare() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<DataApiReqVO> scene = dataApiMapper.getShare(dataApiVO);
        return Result.success(scene);
    }

    @Override
    public Result getShareTotal() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<TotalReqVO> scene = dataApiMapper.getShareTotal(dataApiVO);
        return Result.success(scene);
    }

    @Override
    public Result getEntryTotal() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<EntryReqVO> entryVisTotal = dataApiMapper.getEntryVisTotal(dataApiVO);
        List<EntryReqVO> entryTotal = dataApiMapper.getEntryTotal(dataApiVO);
        Map<String, Integer> userMap = entryTotal.stream().collect(Collectors.toMap(k->k.getAppKey(), v->v.getEntryNum()));
        for(EntryReqVO entryReqVO:entryVisTotal){
            String appKey = entryReqVO.getAppKey();
            Integer num = userMap.get(appKey);
            entryReqVO.setEntryNum(num);
        }
        return Result.success(entryVisTotal);
    }

    @Override
    public Result getOpenTotal() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<OpenReqVO> result = dataApiMapper.getOpenTotal(dataApiVO);
        return Result.success(result);
    }

    @Override
    public Result getOpenDetail() {
        DataApiVO dataApiVO=new DataApiVO();
        dataApiVO.setTime(TimeUtils.getBeforehalfHour());
        List<OpenDetailReqVO> result = dataApiMapper.getOpenDetail(dataApiVO);
        return Result.success(result);
    }


}
