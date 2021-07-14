package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.ScenceMapper;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.scene.*;
import com.etocrm.sdk.entity.total.ToTalPO;
import com.etocrm.sdk.entity.user.UserDatailVO;
import com.etocrm.sdk.entity.user.UserLostReturnRepVO;
import com.etocrm.sdk.mysqldao.MysqlMapper;
import com.etocrm.sdk.service.DataBroadService;
import com.etocrm.sdk.service.SceneService;
import com.etocrm.sdk.util.DecimalFormatUtils;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import com.etocrm.sdk.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SceneServiceImpl  implements SceneService {

    @Autowired
    private ScenceMapper scenceMapper;

    @Autowired
    private DataBroadService dataBroadService;

    @Autowired
    private OvalUtils ovalUtils;

    @Autowired
    private MysqlMapper mysqlMapper;

    @Override
    public Result dataGet(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        SceneRepVO sceneRepVO=new SceneRepVO();
        sceneRepVO.setTotalDataRepVO(dataBroadService.getTotalDataRepVO(dataBroadVO));
        sceneRepVO.setSceneTotalDetailDomain(getSceneTotalDetailDomain(dataBroadVO,1));
        //top5
       // sceneRepVO.setChartDetailDomain(getChartDetailDomainRepVO(dataBroadVO));
        //sceneRepVO.setSceneDetailData(getSceneDetailData(dataBroadVO));
        sceneRepVO.setDateDetailData(getDateDetailData(dataBroadVO,null));
        return Result.success(sceneRepVO);
    }

    @Override
    public List<SceneTotalDetailDomainRepVO> getexcel(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Collections.emptyList();
        List<SceneTotalDetailDomainRepVO> sceneTotalDetailDomain = getSceneTotalDetailDomain(dataBroadVO, 1);
        return sceneTotalDetailDomain;
    }

    @Override
    public Result dataGetBySceneID(SceneDateVO sceneDateVO) {
        if (ovalUtils.validatorRequestParam(sceneDateVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer count=scenceMapper.getUserSenceCount(sceneDateVO);
        if(count>0){
            List<UserLostReturnRepVO> userLostReturnRepVOList = scenceMapper.getUserScenePage(sceneDateVO);
            PageUtils<UserLostReturnRepVO> pageUtils=new PageUtils<>(sceneDateVO.getPageSize(),sceneDateVO.getPageIndex(),count);
            pageUtils.setLists(userLostReturnRepVOList);
            return Result.success(pageUtils);
        }else{
            PageUtils<UserLostReturnRepVO> pageUtils=new PageUtils<>(sceneDateVO.getPageSize(),sceneDateVO.getPageIndex(),0);
            return Result.success(pageUtils);
        }
    }

    @Override
    public List<UserDatailVO> getSceneIdexcel(SceneExcelVO sceneExcelVO) {
        if (ovalUtils.validatorRequestParam(sceneExcelVO).size() > 0) return Collections.emptyList();
        List<UserDatailVO> userSceneIdList = scenceMapper.getUserSceneId(sceneExcelVO);
        return userSceneIdList;
    }

    @Override
    public Result dataDetailGet(SceneDataVO sceneDataVO) {
        if (ovalUtils.validatorRequestParam(sceneDataVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        DataBroadVO dataBroadVO=new DataBroadVO();
        BeanUtils.copyProperties(sceneDataVO,dataBroadVO);
        PageUtils<SceneTotalDetailDomainRepVO> pageUtil = new PageUtils<>(sceneDataVO.getPageSize(), sceneDataVO.getPageIndex(), 0);
        try {
            List<Map<String, Object>> sceneAll = scenceMapper.getSceneAll(dataBroadVO);
            if(CollectionUtils.isEmpty(sceneAll)){
                return  Result.success(pageUtil);
            }
            pageUtil.setTotalNum(sceneAll.size());
            List<Map<String, Object>> scenePage = scenceMapper.getScenePage(sceneDataVO);
            SceneTotalDetailDomainRepVO sceneTotalDetailDomainRepVO=null;
            List<SceneTotalDetailDomainRepVO> list=new ArrayList<>();
            Map<Integer, Long> stopTime2 = getStopTime(dataBroadVO);
            for (Map<String, Object> map : scenePage) {
                sceneTotalDetailDomainRepVO=new SceneTotalDetailDomainRepVO();
                Integer scene=(Integer)map.get("scene");
                String sceneName=(String)map.get("sceneName");
                sceneTotalDetailDomainRepVO.setScene(scene);
                sceneTotalDetailDomainRepVO.setSceneName(sceneName);
                SceneDataTypeVO sceneDataTypeVO=new SceneDataTypeVO();
                BeanUtils.copyProperties(sceneDataVO,sceneDataTypeVO);
                sceneDataTypeVO.setSceneId(scene);
                Integer newUserNum = scenceMapper.getNewUserScene(sceneDataTypeVO);
                if (newUserNum != null) {
                    sceneTotalDetailDomainRepVO.setNewUserNum(newUserNum);
                }
                Integer openScene = scenceMapper.getOpenScene(sceneDataTypeVO);
                if(openScene==null){
                    sceneTotalDetailDomainRepVO.setOpenNum(0);
                }else{
                    sceneTotalDetailDomainRepVO.setOpenNum(openScene);
                }
                List<Map<String, Object>> visNumScene = scenceMapper.getVisNumScene(sceneDataTypeVO);
                if(CollectionUtils.isNotEmpty(visNumScene)){
                    Map<String, Object> map1 = visNumScene.get(0);
                    BigInteger visNum = (BigInteger)map1.get("visNum");
                    BigInteger accUserNum = (BigInteger) map1.get("accUserNum");

                    sceneTotalDetailDomainRepVO.setVisitNum(visNum.intValue());
                    sceneTotalDetailDomainRepVO.setUserNum(accUserNum.intValue());

                }
                Integer visExitScene = scenceMapper.getVisExitScene(sceneDataTypeVO);
                visExitScene= visExitScene==null?0:visExitScene;
                String percent = DecimalFormatUtils.getPercent(visExitScene, sceneTotalDetailDomainRepVO.getOpenNum());
                sceneTotalDetailDomainRepVO.setExitRate(percent);
                Long stopTime =stopTime2.get(scene);
                stopTime=stopTime==null?0l:stopTime;
                Integer openNum1=sceneTotalDetailDomainRepVO.getOpenNum();
                String stopTime1 = openNum1 == 0 ? "00:00:00" : TimeUtils.getGapTime(stopTime.longValue() / openNum1);
                sceneTotalDetailDomainRepVO.setAvgStopTime(stopTime1);
                list.add(sceneTotalDetailDomainRepVO);
            }
            pageUtil.setLists(list);

        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.Fail);
        }
        return Result.success(pageUtil);
    }

    @Override
    public Result getUserGet(SceneUserVO sceneUserVO) {
        if (ovalUtils.validatorRequestParam(sceneUserVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer pageCount = scenceMapper.getUserGetCount(sceneUserVO);
        PageUtils<UserDatailVO> pageUtil = new PageUtils<>(sceneUserVO.getPageSize(), sceneUserVO.getPageIndex(), pageCount);
        if (pageCount > 0) {
            List<UserDatailVO> pageListPage = scenceMapper.getUserGet(sceneUserVO);
            pageUtil.setLists(pageListPage);
        } else {
            pageUtil.setLists(Collections.emptyList());
        }
        return Result.success(pageUtil);
    }

    @Override
    public Result getSceneTotal(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        SceneTotalVO sceneTotalVO = scenceMapper.getUserShareScene(dataBroadVO);
        if(sceneTotalVO!=null){
            //回流量、分享新增人数（回流量pageshow之间的关系）
            Integer userSharebackflow = scenceMapper.getUserSharebackflow(dataBroadVO);
            sceneTotalVO.setTurnbackTotal(userSharebackflow==null?0:userSharebackflow);
            String rate=DecimalFormatUtils.getPercent(sceneTotalVO.getTurnbackTotal(),sceneTotalVO.getShareNumTotal());
            sceneTotalVO.setTurnbackPropTotal(rate);
            Integer userPlusNumTotal=scenceMapper.getUserShareAdd(dataBroadVO);
            sceneTotalVO.setUserPlusNumTotal(userPlusNumTotal==null?0:userPlusNumTotal);
        }
        return Result.success(sceneTotalVO);
    }

    @Override
    public Result dataGetBySceneID(SceneIdVO sceneIdVO) {
        if (ovalUtils.validatorRequestParam(sceneIdVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        DataSceneIDVO dataSceneIDVO=new DataSceneIDVO();
        dataSceneIDVO.setTotalData(getTotalBean(sceneIdVO));
        List<SceneTotalDetailDomain> list=getSceneIdTotalDetailDomain(sceneIdVO);
        dataSceneIDVO.setSceneTotalDetailDomain(list);
        DataBroadVO dataBroadVO=new DataBroadVO();
        BeanUtils.copyProperties(sceneIdVO,dataBroadVO);
        List<DateDetailData> dateDetailData = getDateDetailData(dataBroadVO,sceneIdVO.getSceneId());
        dataSceneIDVO.setSceneDateDetailDomain(dateDetailData);
        return Result.success(dataSceneIDVO);
    }

    private List<DateDetailData> getDateDetailData(DataBroadVO dataBroadVO, Integer scenId) {
        List<DateDetailData> list=new ArrayList<>();
        List<Map<String, Object>> sceneAll=new ArrayList<>();
        try {
            if(scenId==null){
              sceneAll =scenceMapper.getSceneDateAll(dataBroadVO);
            }else{
                SceneIdVO sceneIdVO=new SceneIdVO();
                sceneIdVO.setSceneId(scenId);
                BeanUtils.copyProperties(dataBroadVO,sceneIdVO);
                sceneAll =scenceMapper.getSceneIdDate(sceneIdVO);
            }

            List<Map<String, Object>> newUserList = scenceMapper.getNewUserDate(dataBroadVO);
            List<Map<String, Object>> openList = scenceMapper.getOpenDate(dataBroadVO);
            List<Map<String, Object>> visList = scenceMapper.getVisNumDate(dataBroadVO);
            List<Map<String, Object>> exitList = scenceMapper.getExitDate(dataBroadVO);
            Map<String, Integer> newUserMap = newUserList.stream().collect(
                    Collectors.toMap(x -> x.get("scene").toString(),
                            x -> Integer.valueOf(x.get("num").toString())));
            Map<String, Integer> openNumMap = openList.stream().collect(
                    Collectors.toMap(x -> x.get("scene").toString(),
                            x -> Integer.valueOf(x.get("num").toString())));
            Map<String, String> visMap = visList.stream().collect(
                    Collectors.toMap(x -> x.get("scene").toString(),
                            x -> String.valueOf(x.get("visNum").toString() + "-" + x.get("accUserNum").toString())));

            Map<String, Integer> exitMap = exitList.stream().collect(
                    Collectors.toMap(x -> x.get("scene").toString(),
                            x -> Integer.valueOf(x.get("exit").toString())));
            DateDetailData dateDetailData=null;
            Map<Integer, Long> stopTime2 = getStopTime(dataBroadVO);
            for (Map<String, Object> map : sceneAll) {
                dateDetailData=new DateDetailData();
                String key=(String)map.get("scene");
                String sceneName=(String)map.get("sceneName");

                String[] split1 = key.split("=");
                Integer scene=Integer.valueOf(split1[1]);
                String date=String.valueOf(split1[0]);
                dateDetailData.setScene(scene);
                dateDetailData.setSceneName(sceneName);
                dateDetailData.setDate(date);
                Integer oNUm = newUserMap.get(key);
                dateDetailData.setNewUseridCount(oNUm==null?0:oNUm);
                Integer openNum=openNumMap.get(key);
                openNum=openNum==null?0:openNum;
                dateDetailData.setTotalSimpleBakCount(openNum);
                String s = visMap.get(key);
                if (StringUtils.isNotBlank(s)) {
                    String[] split = s.split("-");
                    Integer visNum= Integer.valueOf(split[0]);
                    Integer accUserNum= Integer.valueOf(split[1]);
                    dateDetailData.setUseridVisitCount(visNum);
                    dateDetailData.setUseridCount(accUserNum);
                }
                Integer exitNum= exitMap.get(key);
                exitNum=exitNum==null?0:exitNum;
                String percent = DecimalFormatUtils.getPercent(exitNum, openNum);
                dateDetailData.setBounceRate(percent);
                Long stopTime = stopTime2.get(scene);
                stopTime=stopTime==null?0l:stopTime;
                Integer openNum1=dateDetailData.getTotalSimpleBakCount();
                String stopTime1 = openNum1 == 0 ? "00:00:00" : TimeUtils.getGapTime(stopTime.longValue() / openNum1);
                dateDetailData.setAvgDate(stopTime1);
                list.add(dateDetailData);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return list;

    }

    private List<SceneDetailData> getSceneDetailData(DataBroadVO dataBroadVO) {
        List<Map<String, Object>> sceneAll = scenceMapper.getSceneAll(dataBroadVO);
        List<SceneDetailData> sceneDetailDataList=new ArrayList<>();
        SceneDetailData sceneDetailData=null;
        for(Map<String, Object> map:sceneAll){
            sceneDetailData=new SceneDetailData();
            sceneDetailData.setScene((Integer)map.get("scene"));
            sceneDetailData.setSceneName((String)map.get("sceneName"));
            sceneDetailDataList.add(sceneDetailData);
        }
        return sceneDetailDataList;

    }

    private List<SceneTotalDetailDomainRepVO> getSceneTotalDetailDomain(DataBroadVO dataBroadVO, Integer type) {
        List<SceneTotalDetailDomainRepVO> list=new ArrayList<>();
        try {
            List<Map<String, Object>> sceneAll = new ArrayList<>();
            if(type==1){
                sceneAll = scenceMapper.getSceneAll(dataBroadVO);
            }else if(type==2){
                sceneAll = scenceMapper.getSceneTopfive(dataBroadVO);
            }
            List<Map<String, Object>> newUserList = scenceMapper.getNewUser(dataBroadVO);
            List<Map<String, Object>> openList = scenceMapper.getOpen(dataBroadVO);
            List<Map<String, Object>> visList = scenceMapper.getVisNum(dataBroadVO);
            //跳出率
            List<Map<String, Object>> exitList = scenceMapper.getVisExitNum(dataBroadVO);

            Map<Integer, Integer> newUserMap = newUserList.stream().collect(
                    Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                            x -> Integer.valueOf(x.get("num").toString())));
            Map<Integer, Integer> openNumMap = openList.stream().collect(
                    Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                            x -> Integer.valueOf(x.get("num").toString())));
            Map<Integer, String> visMap = visList.stream().collect(
                    Collectors.toMap(x ->Integer.valueOf(x.get("scene").toString()),
                            x -> String.valueOf(x.get("visNum").toString() + "-" + x.get("accUserNum")
                                    )));
            Map<Integer, Integer> exitNumMap = exitList.stream().collect(
                    Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                            x -> Integer.valueOf(x.get("exit").toString())));
            SceneTotalDetailDomainRepVO sceneTotalDetailDomainRepVO=null;
            for (Map<String, Object> map : sceneAll) {
                sceneTotalDetailDomainRepVO=new SceneTotalDetailDomainRepVO();
                Integer key=(Integer)map.get("scene");
                sceneTotalDetailDomainRepVO.setScene(key);
                String sceneName=(String)map.get("sceneName");
                sceneTotalDetailDomainRepVO.setSceneName(sceneName);

                Integer oNUm = newUserMap.get(key);
                oNUm=oNUm==null?0:oNUm;
                sceneTotalDetailDomainRepVO.setNewUserNum(oNUm);
                Integer openNum=openNumMap.get(key);
                openNum=openNum==null?0:openNum;
                sceneTotalDetailDomainRepVO.setOpenNum(openNum);

                String s = visMap.get(key);
                if (StringUtils.isNotBlank(s)) {
                    String[] split = s.split("-");
                    Integer visNum= Integer.valueOf(split[0]);
                    Integer accUserNum= Integer.valueOf(split[1]);
                    sceneTotalDetailDomainRepVO.setVisitNum(visNum);
                    sceneTotalDetailDomainRepVO.setUserNum(accUserNum);
                }else{
                    sceneTotalDetailDomainRepVO.setVisitNum(0);
                    sceneTotalDetailDomainRepVO.setUserNum(0);
                }
                Integer exitNum=exitNumMap.get(key)==null?0:exitNumMap.get(key);
                String percent = DecimalFormatUtils.getPercent(exitNum, openNum);
                sceneTotalDetailDomainRepVO.setExitRate(percent);
                SceneType sceneType=new SceneType();
                BeanUtils.copyProperties(dataBroadVO,sceneType);
                BigInteger stopTime = scenceMapper.getStopTime(sceneType);
                Integer openNum1=sceneTotalDetailDomainRepVO.getOpenNum();
                String stopTime1 = openNum1 == 0 ? "00:00:00" : TimeUtils.getGapTime(stopTime.longValue() / openNum1);
                sceneTotalDetailDomainRepVO.setAvgStopTime(stopTime1);
                list.add(sceneTotalDetailDomainRepVO);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return list;
    }

    private List<ChartDetailDomainRepVO> getChartDetailDomainRepVO(DataBroadVO dataBroadVO) {
        List<SceneTotalDetailDomainRepVO> sceneTotalDetailDomain = getSceneTotalDetailDomain(dataBroadVO,2);
        List<ChartDetailDomainRepVO>  result=new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(sceneTotalDetailDomain)) {
                int totalNewUseridCount = sceneTotalDetailDomain.stream().mapToInt(SceneTotalDetailDomainRepVO::getNewUserNum).sum();
                int totalUseridCount = sceneTotalDetailDomain.stream().mapToInt(SceneTotalDetailDomainRepVO::getUserNum).sum();
                int totalUseridVisitCount = sceneTotalDetailDomain.stream().mapToInt(SceneTotalDetailDomainRepVO::getVisitNum).sum();
                int totalTotalSimpleBakCount = sceneTotalDetailDomain.stream().mapToInt(SceneTotalDetailDomainRepVO::getOpenNum).sum();
                ChartDetailDomainRepVO chartDetailDomainRepVO=null;
                for(SceneTotalDetailDomainRepVO sceneTotalDetailDomainRepVO:sceneTotalDetailDomain){
                    chartDetailDomainRepVO=new ChartDetailDomainRepVO();
                    BeanUtils.copyProperties(sceneTotalDetailDomainRepVO,chartDetailDomainRepVO);
                    chartDetailDomainRepVO.setTotalNewUseridCount(totalNewUseridCount);
                    chartDetailDomainRepVO.setTotalUseridCount(totalUseridCount);
                    chartDetailDomainRepVO.setTotalUseridVisitCount(totalUseridVisitCount);
                    chartDetailDomainRepVO.setTotalTotalSimpleBakCount(totalTotalSimpleBakCount);
                    result.add(chartDetailDomainRepVO);

                }
                /*result.forEach(ChartDetailDomainRepVO->{
                    ChartDetailDomainRepVO.setTotalNewUseridCount(totalNewUseridCount);
                    ChartDetailDomainRepVO.setTotalUseridCount(totalUseridCount);
                    ChartDetailDomainRepVO.setTotalUseridVisitCount(totalUseridVisitCount);
                    ChartDetailDomainRepVO.setTotalTotalSimpleBakCount(totalTotalSimpleBakCount);
                });*/
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);

        }
        return  result;
    }

    private  Map<Integer, Long> getStopTime(DataBroadVO dataBroadVO){
        List<Map<String, Object>> sceneStopTime = scenceMapper.getSceneStopTime(dataBroadVO);
        Map<Integer, Long> sceneStopTimeMap = sceneStopTime.stream().collect(
                Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                        x -> Long.valueOf(x.get("stopTime").toString())));
        return sceneStopTimeMap;
    }

    private TotalSceneData getTotalBean(SceneIdVO sceneIdVO){
        DataBroadVO dataBroadVO=new DataBroadVO();
        BeanUtils.copyProperties(sceneIdVO, dataBroadVO);
        ToTalPO totalData = mysqlMapper.getYesList(dataBroadVO);
        TotalSceneData totalSceneData=new TotalSceneData();
        if (totalData != null) {
            totalSceneData.setNewUseridCount(totalData.getNewUser());
            totalSceneData.setUseridVisitCount(totalData.getPv());
            totalSceneData.setUseridCount(totalData.getUv());
            totalSceneData.setTotalSimpleBakCount(totalData.getOpenNum());
            Integer boundPage = totalData.getBounce();
            boundPage = boundPage == null ? 0 : boundPage;//跳出率
            String percent = DecimalFormatUtils.getPercent(boundPage, totalData.getOpenNum().intValue());
            totalSceneData.setBounceRate(percent + "%");
            Integer openNum = totalData.getOpenNum();
            String time = openNum == 0 ? "00:00:00" : TimeUtils.getGapTime(totalData.getAvgStopTime() / openNum);
            totalSceneData.setAvgDate(time);
        }
        return totalSceneData;
    }

    private List<SceneTotalDetailDomain> getSceneIdTotalDetailDomain(SceneIdVO sceneIdVO) {
        List<SceneTotalDetailDomain> list=new ArrayList<>();
        try {
            List<Map<String, Object>> sceneAll  = scenceMapper.getScene(sceneIdVO);

            DataBroadVO dataBroadVO=new DataBroadVO();
            BeanUtils.copyProperties(sceneIdVO,dataBroadVO);
            List<Map<String, Object>> newUserList = scenceMapper.getNewUser(dataBroadVO);
            List<Map<String, Object>> openList = scenceMapper.getOpen(dataBroadVO);
            List<Map<String, Object>> visList = scenceMapper.getVisNum(dataBroadVO);
            //跳出率
            List<Map<String, Object>> exitList = scenceMapper.getVisExitNum(dataBroadVO);

            Map<Integer, Integer> newUserMap = newUserList.stream().collect(
                    Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                            x -> Integer.valueOf(x.get("num").toString())));
            Map<Integer, Integer> openNumMap = openList.stream().collect(
                    Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                            x -> Integer.valueOf(x.get("num").toString())));
            Map<Integer, String> visMap = visList.stream().collect(
                    Collectors.toMap(x ->Integer.valueOf(x.get("scene").toString()),
                            x -> String.valueOf(x.get("visNum").toString() + "-" + x.get("accUserNum")
                            )));
            Map<Integer, Integer> exitNumMap = exitList.stream().collect(
                    Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                            x -> Integer.valueOf(x.get("exit").toString())));
            SceneTotalDetailDomain sceneTotalDetailDomainRepVO=null;
            for (Map<String, Object> map : sceneAll) {
                sceneTotalDetailDomainRepVO=new SceneTotalDetailDomain();
                Integer key=(Integer)map.get("scene");
                sceneTotalDetailDomainRepVO.setScene(key);
                String sceneName=(String)map.get("sceneName");
                sceneTotalDetailDomainRepVO.setSceneName(sceneName);

                Integer oNUm = newUserMap.get(key);
                oNUm=oNUm==null?0:oNUm;
                sceneTotalDetailDomainRepVO.setNewUseridCount(oNUm);
                Integer openNum=openNumMap.get(key);
                openNum=openNum==null?0:openNum;
                sceneTotalDetailDomainRepVO.setTotalSimpleBakCount(openNum);

                String s = visMap.get(key);
                if (StringUtils.isNotBlank(s)) {
                    String[] split = s.split("-");
                    Integer visNum= Integer.valueOf(split[0]);
                    Integer accUserNum= Integer.valueOf(split[1]);
                    sceneTotalDetailDomainRepVO.setUseridVisitCount(visNum);
                    sceneTotalDetailDomainRepVO.setUseridCount(accUserNum);
                }
                Integer exitNum=exitNumMap.get(key)==null?0:exitNumMap.get(key);
                String percent = DecimalFormatUtils.getPercent(exitNum, openNum);
                sceneTotalDetailDomainRepVO.setBounceRate(percent);
                SceneType sceneType=new SceneType();
                BeanUtils.copyProperties(dataBroadVO,sceneType);
                sceneType.setSceneId(sceneIdVO.getSceneId());
                System.out.println(sceneIdVO.getSceneId());
                BigInteger stopTime = scenceMapper.getStopTime(sceneType);
                Integer openNum1=sceneTotalDetailDomainRepVO.getTotalSimpleBakCount();
                String stopTime1 = openNum1 == 0 ? "00:00:00" : TimeUtils.getGapTime(stopTime.longValue() / openNum1);
                sceneTotalDetailDomainRepVO.setAvgDate(stopTime1);
                list.add(sceneTotalDetailDomainRepVO);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return list;
    }



}
