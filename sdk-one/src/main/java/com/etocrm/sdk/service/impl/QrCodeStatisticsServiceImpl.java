package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.QrCodeStatisticsMapper;
import com.etocrm.sdk.entity.QrStatistics.*;
import com.etocrm.sdk.service.QrCodeStatisticsService;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QrCodeStatisticsServiceImpl  implements QrCodeStatisticsService {

    @Autowired
    private QrCodeStatisticsMapper qrCodeStatisticsMapper;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result getQrCodeStatistics(QrCodeStatisticsVO qrCodeStatisticsVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        QrCodeStatisticsRepVO qrCodeStatistics= qrCodeStatisticsMapper.getQrCodeStatistics(qrCodeStatisticsVO);
        if(qrCodeStatistics!=null){
            qrCodeStatistics.setNewVisitNum(qrCodeStatisticsMapper.getNewUser(qrCodeStatisticsVO));
        }
        return Result.success(qrCodeStatistics);
    }

    @Override
    public Result getQrGroupStatistics(QrCodeStatisticsVO qrCodeStatisticsVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        QrGroupStatisticsRepVO qrGroupStatistics= qrCodeStatisticsMapper.getQrGroupStatistics(qrCodeStatisticsVO);
        if(qrGroupStatistics!=null){
            qrGroupStatistics.setNewVisitNum(qrCodeStatisticsMapper.getQrGroupNewUser(qrCodeStatisticsVO));
        }
        return Result.success(qrGroupStatistics);
    }

    @Override
    public Result getStatisticsChartOfQrCode(QrCodeStatisticsVO qrCodeStatisticsVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<QrCodeDateRepVO> qrCodeDate = qrCodeStatisticsMapper.getQrCodeDate(qrCodeStatisticsVO);
        if(CollectionUtils.isNotEmpty(qrCodeDate)){

            List<QrCodeDateRepVO> qrCodeNewUserDate = qrCodeStatisticsMapper.getQrCodeNewUserDate(qrCodeStatisticsVO);
            Map<String, Integer> collect = qrCodeNewUserDate.stream().collect(Collectors.toMap(QrCodeDateRepVO::getDateTime, QrCodeDateRepVO::getNewVisit));
            for(QrCodeDateRepVO qrCodeDateRepVO:qrCodeDate){
                String dateTime = qrCodeDateRepVO.getDateTime();
                Integer newUserNum = collect.get(dateTime);
                newUserNum=newUserNum!=null?newUserNum:0;
                qrCodeDateRepVO.setNewVisit(newUserNum);
            }
        }

        return Result.success(qrCodeDate);
    }

    @Override
    public Result getQrGroupChart(QrCodeStatisticsVO qrCodeStatisticsVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<QrGroupDateRepVO> qrGroupDate = qrCodeStatisticsMapper.getQrGroupDate(qrCodeStatisticsVO);
        if(CollectionUtils.isNotEmpty(qrGroupDate)){
            List<QrGroupDateRepVO> qrGroupNewUserDate = qrCodeStatisticsMapper.getQrGroupNewUserDate(qrCodeStatisticsVO);
            Map<String, Integer> newNumMap = qrGroupNewUserDate.stream().collect(Collectors.toMap(k -> k.getDate() + k.getGroupName(), k -> k.getNewVisitNum()));
            for(QrGroupDateRepVO qrGroupDateRepVO:qrGroupDate) {
                String  key=qrGroupDateRepVO.getDate()+qrGroupDateRepVO.getGroupName();
                Integer newNum = newNumMap.get(key)==null?0:newNumMap.get(key);
                qrGroupDateRepVO.setNewVisitNum(newNum);
            }
        }
        return Result.success(qrGroupDate);
    }

    @Override
    public Result getStatisticsTableOfQrCode(QrCodeStatisticsPageVO qrCodeStatisticsPageVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsPageVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer statisticsTableOfQrCodeCount = qrCodeStatisticsMapper.getStatisticsTableOfQrCodeCount(qrCodeStatisticsPageVO);
        PageUtils<StatisticsTableOfQrCodeVO> pageUtils=new PageUtils<>(qrCodeStatisticsPageVO.getPageSize(),qrCodeStatisticsPageVO.getPageIndex(),statisticsTableOfQrCodeCount);
        if(statisticsTableOfQrCodeCount==null){
            pageUtils.setLists(Collections.emptyList());
            return  Result.success(pageUtils);
        }
        List<StatisticsTableOfQrCodeVO> statisticsTableOfQrCodeVOList = qrCodeStatisticsMapper.getStatisticsTableOfQrCode(qrCodeStatisticsPageVO);
        List<Map<String, Object>> visitNewUser = qrCodeStatisticsMapper.getVisitNewUser(qrCodeStatisticsPageVO);
        Map<String, Integer> newUserMap = visitNewUser.stream().collect(Collectors.toMap(x -> x.get("visitpath").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO:statisticsTableOfQrCodeVOList){
            String qrURL = statisticsTableOfQrCodeVO.getQrURL();
            Integer newUser = newUserMap.get(qrURL);
            statisticsTableOfQrCodeVO.setNewVisit(newUser==null?0:newUser);
        }
        pageUtils.setLists(statisticsTableOfQrCodeVOList);
        return Result.success(pageUtils);
    }

    @Override
    public List<StatisticsTableOfQrCodeVO> downLoadStatisticsTableOfQrCode(QrCodeStatisticsVO qrCodeStatisticsVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsVO).size() > 0) return Collections.emptyList();
        List<StatisticsTableOfQrCodeVO> list=qrCodeStatisticsMapper.downLoadStatisticsTableOfQrCode(qrCodeStatisticsVO);
        QrCodeStatisticsPageVO qrCodeStatisticsPageVO=new QrCodeStatisticsPageVO();
        BeanUtils.copyProperties(qrCodeStatisticsVO,qrCodeStatisticsPageVO);
        List<Map<String, Object>> visitNewUser = qrCodeStatisticsMapper.getVisitNewUser(qrCodeStatisticsPageVO);
        Map<String, Integer> newUserMap = visitNewUser.stream().collect(Collectors.toMap(x -> x.get("visitpath").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(StatisticsTableOfQrCodeVO statisticsTableOfQrCodeVO:list){
            String qrURL = statisticsTableOfQrCodeVO.getQrURL();
            Integer newUser = newUserMap.get(qrURL);
            statisticsTableOfQrCodeVO.setNewVisit(newUser==null?0:newUser);
        }

        return list;
    }

    @Override
    public List<StatisticsTableOfQrGroupVO> downLoadStatisticsTableOfQrGroup(QrCodeStatisticsVO qrCodeStatisticsVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsVO).size() > 0) return Collections.emptyList();
        List<StatisticsTableOfQrGroupVO> list=qrCodeStatisticsMapper.getStatisticsTableOfQrGroupExcel(qrCodeStatisticsVO);
        QrCodeStatisticsPageVO qrCodeStatisticsPageVO=new QrCodeStatisticsPageVO();
        BeanUtils.copyProperties(qrCodeStatisticsVO,qrCodeStatisticsPageVO);
        List<Map<String, Object>> visitNewUser = qrCodeStatisticsMapper.getVisitNewUserGroup(qrCodeStatisticsPageVO);
        Map<String, Integer> newUserMap = visitNewUser.stream().collect(Collectors.toMap(x -> x.get("groupName").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(StatisticsTableOfQrGroupVO statisticsTableOfQrCodeVO:list){
            String groupName = statisticsTableOfQrCodeVO.getGroupName();
            Integer newUser = newUserMap.get(groupName);
            statisticsTableOfQrCodeVO.setNewVisit(newUser==null?0:newUser);
        }
        return list;
    }

    @Override
    public Result getStatisticsTableOfQrGroup(QrCodeStatisticsPageVO qrCodeStatisticsPageVO) {
        if (ovalUtils.validatorRequestParam(qrCodeStatisticsPageVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer statisticsTableOfQrCodeCount = qrCodeStatisticsMapper.getStatisticsTableOfQrGroupCount(qrCodeStatisticsPageVO);
        PageUtils<StatisticsTableOfQrGroupVO> pageUtils=new PageUtils<>(qrCodeStatisticsPageVO.getPageSize(),qrCodeStatisticsPageVO.getPageIndex(),statisticsTableOfQrCodeCount);
        if(statisticsTableOfQrCodeCount==null){
            pageUtils.setLists(Collections.emptyList());
            return  Result.success(pageUtils);
        }
        List<StatisticsTableOfQrGroupVO> statisticsTableOfQrCodeVOList = qrCodeStatisticsMapper.getStatisticsTableOfQrGroup(qrCodeStatisticsPageVO);
        List<Map<String, Object>> visitNewUser = qrCodeStatisticsMapper.getVisitNewUserGroup(qrCodeStatisticsPageVO);
        Map<String, Integer> newUserMap = visitNewUser.stream().collect(Collectors.toMap(x -> x.get("groupName").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(StatisticsTableOfQrGroupVO statisticsTableOfQrCodeVO:statisticsTableOfQrCodeVOList){
            String groupName = statisticsTableOfQrCodeVO.getGroupName();
            Integer newUser = newUserMap.get(groupName);
            statisticsTableOfQrCodeVO.setNewVisit(newUser==null?0:newUser);
        }
        pageUtils.setLists(statisticsTableOfQrCodeVOList);
        return Result.success(pageUtils);
    }
}
