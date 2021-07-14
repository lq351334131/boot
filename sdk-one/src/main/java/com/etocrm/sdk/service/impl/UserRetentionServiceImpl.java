package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.DataBroadMapper;
import com.etocrm.sdk.dao.UserRetentionMapper;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.user.*;
import com.etocrm.sdk.service.UserRetentionService;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import com.etocrm.sdk.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserRetentionServiceImpl implements UserRetentionService {

    @Autowired
    private DataBroadMapper dataBroadMapper;

    @Autowired
    private UserRetentionMapper userRetentionMapper;

    @Autowired
    private OvalUtils ovalUtils;


    @Override
    public Result getUserNewAddRetentionTableAndChartData(DataBroadVO dataBroadVO) {
        if(ovalUtils.validatorRequestParam(dataBroadVO).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        List<UserNewAddRepVO> retention = getRetention(1, dataBroadVO);
        return Result.success(retention);
    }

    @Override
    public Result getUserActiveRetentionTableAndChartData(DataBroadVO dataBroadVO) {
        if(ovalUtils.validatorRequestParam(dataBroadVO).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        List<UserNewAddRepVO> retention = getRetention(2, dataBroadVO);
        return Result.success(retention);
    }

    @Override
    public List<UserNewAddRepVO> getUserNewAddExcel(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Collections.emptyList();
        List<UserNewAddRepVO> retention = getRetention(1, dataBroadVO);
        return retention;
    }

    @Override
    public List<UserNewAddRepVO> getUserNewActiveExcel(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Collections.emptyList();
        List<UserNewAddRepVO> retention = getRetention(2, dataBroadVO);
        return retention;
    }

    @Override
    public Result getUserBackflowRetentionTableAndChartData(DataBroadVO dataBroadVO) {
        if(ovalUtils.validatorRequestParam(dataBroadVO).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        List<LostReturnRepVO> lostReturn = getLostReturn(dataBroadVO);
        return Result.success(lostReturn);
    }

    @Override
    public List<LostReturnRepVO> getUserBackflowRetentionExcel(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Collections.emptyList();
        return getLostReturn(dataBroadVO);
    }

    @Override
    public Result getUserPage(UserLostTypeVO userLostTypeVO) {
        if(ovalUtils.validatorRequestParam(userLostTypeVO).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        PageUtils<UserDatailVO> userDetails = getUserDetails(userLostTypeVO);
        return Result.success(userDetails);
    }

    @Override
    public List<UserDatailVO> downloadUserBackflowRetentionInfos(UserLostTypeDowVO userLostTypeVO) {
        if (ovalUtils.validatorRequestParam(userLostTypeVO).size() > 0) return Collections.emptyList();
        List<UserDatailVO> list=new ArrayList<>();
        SlientLostVO slientLostVO=new SlientLostVO();
        BeanUtils.copyProperties(userLostTypeVO,slientLostVO);
        String   date=userLostTypeVO.getCurDate();
        slientLostVO.setBeginDate(date);
        slientLostVO.setNinetyDay(TimeUtils.reduceDayone(date,-90));
        slientLostVO.setThirtyDay(TimeUtils.reduceDayone(date,-30));
        if(userLostTypeVO.getTypeId()==1){
            list = userRetentionMapper.getSilentUserPage(slientLostVO);
        }else if(userLostTypeVO.getTypeId()==2){
            list= userRetentionMapper.getLostUserPage(slientLostVO);
        }
        return list;
    }


    private List<UserNewAddRepVO> getRetention(Integer type, DataBroadVO dataBroadVO){
        List<UserNewAddRepVO> list=new ArrayList<>();
        DataBroadVO dataBroadVO1=null;
        try{
            List<Map<String, Object>> dateList = dataBroadMapper.getNewUserDate(dataBroadVO);
            //汇总表
           /* List<UserNewAddRepVO> totalNumList=new ArrayList();
            if(CollectionUtils.isEmpty(totalNumList)){
                //从上报获取数据
            }else{
                List<String> nameList = totalNumList.stream().map(UserNewAddRepVO::getCurDate).collect(Collectors.toList());
                list.addAll(totalNumList);
                for(Map<String, Object> day:dateList){
                    String date= (String) day.get("dt");
                    if(!nameList.contains(date)){
                        //从上报查询
                        List<UserNewAddRepVO> userNewAddRepVO = getUserNewAddRepVO(date, type);
                        list.addAll(userNewAddRepVO);
                    }
                }
            }*/
            UserNewAddRepVO userActiveDate = null;
            for(Map<String, Object> day:dateList){
                String date= (String) day.get("dt");
               // String datTime= TimeUtils.getDateToString(date);
                dataBroadVO1 =new DataBroadVO();
                BeanUtils.copyProperties(dataBroadVO,dataBroadVO1);
                dataBroadVO1.setBeginDate(date);
                dataBroadVO1.setEndDate(TimeUtils.getAddMonth(date));
                userActiveDate=new UserNewAddRepVO();
                if(type==1){
                    userActiveDate = userRetentionMapper.getUserNewAddRetention(dataBroadVO1);
                }else if(type==2){
                    userActiveDate=userRetentionMapper.getUserActiveDate(dataBroadVO1);
                }
                userActiveDate.setCurDate(date);
                list.add(userActiveDate);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return list;
    }

    private  List<LostReturnRepVO> getLostReturn(DataBroadVO dataBroadVO){
        List<LostReturnRepVO> lostReturnRepVOList=new ArrayList<>();
        try{
            List<Map<String, Object>> dateList = dataBroadMapper.getUserDate(dataBroadVO);
            LostReturnRepVO lostReturnRepVO=null;
            SlientLostVO slientLostVO=null;
            for(Map<String, Object> day:dateList){
                String date=(String) day.get("dt");
                slientLostVO=new SlientLostVO();
                slientLostVO.setBeginDate(date);
                slientLostVO.setAppKey(dataBroadVO.getAppKey());
                slientLostVO.setNinetyDay(TimeUtils.reduceDayone(date,-90));
                slientLostVO.setThirtyDay(TimeUtils.reduceDayone(date,-30));
                Integer silent = userRetentionMapper.getSilent(slientLostVO);
                Integer lost = userRetentionMapper.getLost(slientLostVO);
                //String datTime= TimeUtils.getDateToString(date);
                lostReturnRepVO=new LostReturnRepVO();
                lostReturnRepVO.setCurDate(date);
                lostReturnRepVO.setLostNum(silent);
                lostReturnRepVO.setReturnNum(lost);
                lostReturnRepVOList.add(lostReturnRepVO);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return  lostReturnRepVOList;
    }

    private PageUtils<UserDatailVO> getUserDetails(UserLostTypeVO userLostTypeVO){
        PageUtils<UserDatailVO> pageUtils=null;
        try {
            Integer typeId = userLostTypeVO.getTypeId();
            SlientLostVO slientLostVO=new SlientLostVO();
            BeanUtils.copyProperties(userLostTypeVO,slientLostVO);
            String   date=userLostTypeVO.getCurDate();
            slientLostVO.setBeginDate(date);
            slientLostVO.setNinetyDay(TimeUtils.reduceDayone(date,-90));
            slientLostVO.setThirtyDay(TimeUtils.reduceDayone(date,-30));
            if(typeId==1){
                List<UserDatailVO> silentUserPage = userRetentionMapper.getSilentUserPage(slientLostVO);
                pageUtils=new PageUtils(userLostTypeVO.getPageSize(),userLostTypeVO.getPageIndex(),0);
                pageUtils.setLists(silentUserPage);
            }else if(typeId==2){
                List<UserDatailVO> lostUserPage = userRetentionMapper.getLostUserPage(slientLostVO);
                pageUtils=new PageUtils(userLostTypeVO.getPageSize(),userLostTypeVO.getPageIndex(),0);
                pageUtils.setLists(lostUserPage);
            }
        }catch (Exception  e){
            log.error(e.getMessage(),e);
            pageUtils=new PageUtils(userLostTypeVO.getPageSize(),userLostTypeVO.getPageIndex(),0);
        }
        return pageUtils;
    }


    private   List<UserNewAddRepVO> getUserNewAddRepVO(String date, Integer type){
        UserNewAddRepVO userActiveDate = null;
        DataBroadVO dataBroadVO1=null;
        List<UserNewAddRepVO> list=new ArrayList<>();
        dataBroadVO1 =new DataBroadVO();
        dataBroadVO1.setBeginDate(date);
        dataBroadVO1.setEndDate(TimeUtils.getAddMonth(date));
        userActiveDate=new UserNewAddRepVO();
        if(type==1){
            userActiveDate = userRetentionMapper.getUserNewAddRetention(dataBroadVO1);
        }else if(type==2){
            userActiveDate=userRetentionMapper.getUserActiveDate(dataBroadVO1);
        }
        userActiveDate.setCurDate(date);
        list.add(userActiveDate);
        return list;

    }

}
