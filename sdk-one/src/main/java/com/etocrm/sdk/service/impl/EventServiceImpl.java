package com.etocrm.sdk.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.EventMapper;
import com.etocrm.sdk.dao.OnlineEventMapper;
import com.etocrm.sdk.entity.campaign.CampaignVO;
import com.etocrm.sdk.entity.campaign.CampionTimeVO;
import com.etocrm.sdk.entity.campaign.DetailVO;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.event.*;
import com.etocrm.sdk.entity.online.OnlinePage;
import com.etocrm.sdk.entity.online.OnlineTotalData;
import com.etocrm.sdk.entity.online.VisitpathVO;
import com.etocrm.sdk.entity.page.PageListResVO;
import com.etocrm.sdk.service.EventService;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import com.etocrm.sdk.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class EventServiceImpl  implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private OvalUtils ovalUtils;


    @Autowired
    private OnlineEventMapper onlineEventMapper;


    @Override
    public Result addEvent(EventAddVO eventAddVO) {
        if(ovalUtils.validatorRequestParam(eventAddVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        try{
            String id=UUID.randomUUID().toString().replace("-", "");
            eventAddVO.setId(id);
            eventMapper.insert(eventAddVO);
            for(EventParam eventParam:eventAddVO.getEventParam()){
                eventParam.setEventId(id);
            }
            eventMapper.insertParam(eventAddVO.getEventParam());
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Result.success();
    }

    @Override
    public Result deleteEvent(String id) {
        if (StringUtils.isEmpty(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        JSONObject j = JSONObject.parseObject(id);
        String id1 = j.getString("id");
         eventMapper.deleteId(id1);
         eventMapper.deleteParamId(id1);
        return Result.success();
    }

    @Override
    public Result editEvent(EventAddVO eventAddVO) {
        if(StringUtils.isEmpty(eventAddVO.getId())||ovalUtils.validatorRequestParam(eventAddVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        try{
            for(EventParam eventParam:eventAddVO.getEventParam()){
                String id = eventParam.getId();
                if(StringUtils.isNotEmpty(id)){
                    eventMapper.updateParam(eventParam);
                }else{
                    log.error("事件详细ID不为空");
                    return Result.success(ResponseCode.PARAMETERS_NULL);
                }
            }
            eventMapper.update(eventAddVO);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.ES_EXCEPTION);
        }
        return Result.success();
    }

    @Override
    public Result getEventList(EventVO eventVO) {
        if (ovalUtils.validatorRequestParam(eventVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        PageUtils<PageListResVO> pageUtil = new PageUtils<>(eventVO.getPageSize(), eventVO.getPageIndex(), 0);
        try {
            Integer pageCount = eventMapper.getEventCount(eventVO);
            pageUtil = new PageUtils<>(eventVO.getPageSize(), eventVO.getPageIndex(), pageCount);
            eventVO.setPageIndex(pageUtil.getPageIndex());
            if (pageCount > 0) {
                List<EventListPageVO> pageListPage = eventMapper.getPage(eventVO);
                pageUtil.setLists(pageListPage);
            } else {
                pageUtil.setLists(Collections.emptyList());
            }

        } catch (Exception e) {
            log.error(e.getMessage(),e);

        }
        return Result.success(pageUtil);
    }

    @Override
    public List<EventListVO> downloadPageTemplate(DownLoadExcel downLoadExcel) {
        if(ovalUtils.validatorRequestParam(downLoadExcel).size()>0)return Collections.emptyList();
        return  eventMapper.getEventExcel(downLoadExcel);
    }

    @Override
    public Result getEventNameList(DownLoadExcel downLoadExcel) {
        if (ovalUtils.validatorRequestParam(downLoadExcel).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        List<EventListVO> eventExcel = eventMapper.getEventExcel(downLoadExcel);
        return Result.success(eventExcel);
    }

    @Override
    public Result getSingleEvent(String id) {
        if (StringUtils.isEmpty(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        JSONObject j = JSONObject.parseObject(id);
        String id1 = j.getString("id");
        if(StringUtils.isEmpty(id)){
            return Result.error(ResponseCode.PARAMETERS_NULL);
        }
        List<EventClickhouse> eventList = eventMapper.getId(id1);
        EventAddVO eventAddVO=new EventAddVO();
        List<EventParam> eventParamList=new ArrayList<>();
        EventParam eventParam=null;
        for(EventClickhouse eventClickhouse:eventList){
            if(eventClickhouse.getEventId().equals("0")){
                eventAddVO.setId(eventClickhouse.getId());
                eventAddVO.setEventKey(eventClickhouse.getEventKey());
                eventAddVO.setEventName(eventClickhouse.getEventName());
            }else{
                eventParam=new EventParam();
                BeanUtils.copyProperties(eventClickhouse,eventParam);
                eventParamList.add(eventParam);
            }
        }
        eventAddVO.setEventParam(eventParamList);
        return Result.success(eventAddVO);
    }

    @Override
    public Result getEventAnalysisList(EventAnalysisVO eventAnalysisVO) {
        if (ovalUtils.validatorRequestParam(eventAnalysisVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        PageUtils<PageListResVO> pageUtil = new PageUtils<>(eventAnalysisVO.getPageSize(), eventAnalysisVO.getPageIndex(), 0);
        try {
            Integer pageCount = eventMapper.getEventAnalysisCount(eventAnalysisVO);
            pageUtil = new PageUtils<>(eventAnalysisVO.getPageSize(), eventAnalysisVO.getPageIndex(), pageCount);
            eventAnalysisVO.setPageIndex(pageUtil.getPageIndex());
            if (pageCount > 0) {
                List<AnalysisListVO> pageListPage = eventMapper.getEventAnalysisListPage(eventAnalysisVO);
                pageUtil.setLists(pageListPage);
            } else {
                pageUtil.setLists(Collections.emptyList());
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return Result.success(pageUtil);
    }

    @Override
    public Result getEventAnalysisDetails(EventIDVO eventIDVO) {
        if (ovalUtils.validatorRequestParam(eventIDVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        EventIDPO eventIDPOVO=new EventIDPO();
        BeanUtils.copyProperties(eventIDVO,eventIDPOVO);
        EventIDPO tvId = eventMapper.getTvId(eventIDVO.getEventId());
        if(tvId!=null){
            eventIDPOVO.setTl(tvId.getTl());
            eventIDPOVO.setTv(tvId.getTv());
            List<EventDateRepVO> eventId = eventMapper.getEventId(eventIDPOVO);
            return Result.success(eventId);
        }
        return Result.success(Collections.emptyList());
    }

    @Override
    public Result getEventParamList(ParamIDVO paramIDVO) {
        if (ovalUtils.validatorRequestParam(paramIDVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        List<EventParamListVO> eventParamList = eventMapper.getEventParamList(paramIDVO);
        return Result.success(eventParamList);
    }

    @Override
    public Result getUserList(EventUserVO eventUserVO) {
        if (ovalUtils.validatorRequestParam(eventUserVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        EventIDPO eventIDPO = eventMapper.getTvId(eventUserVO.getEventId());
        PageUtils<EventUserRepVO> pageUtil = new PageUtils<>(eventUserVO.getPageSize(), eventUserVO.getPageIndex(), 0);
        try {
            EventUserTVVO eventUserTVVO=new EventUserTVVO();
            BeanUtils.copyProperties(eventUserVO,eventUserTVVO);
            Integer pageCount = eventMapper.getUserCount(eventUserTVVO);
            pageUtil = new PageUtils<>(eventUserTVVO.getPageSize(), eventUserTVVO.getPageIndex(), pageCount);
            eventUserTVVO.setPageIndex(pageUtil.getPageIndex());
            if (pageCount > 0) {
                List<EventUserRepVO> pageListPage = eventMapper.getUserList(eventUserTVVO);
                pageUtil.setLists(pageListPage);
            } else {
                pageUtil.setLists(Collections.emptyList());
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return Result.success(pageUtil);
       /* EventUserTVVO eventUserTVVO=new EventUserTVVO();
        eventUserTVVO.setTl(eventIDPO.getTl());
        eventUserTVVO.setTv(eventIDPO.getTv());
        List<EventUserRepVO> userList = eventMapper.getUserList(eventUserTVVO);
        return Result.success(userList);*/
    }

    @Override
    public Result getEventParamDetails(ParamSearchVO paramSearchVO) {
        if (ovalUtils.validatorRequestParam(paramSearchVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        EventIDPO eventIDPO = eventMapper.getTvId(paramSearchVO.getEventId());
        paramSearchVO.setTl(eventIDPO.getTl());
        paramSearchVO.setTv(eventIDPO.getTv());
        //1：数值，2：分类
        //上报te中数据:[{"k":"te","v":"下载壁纸","t":"string"},{"k":"imgId","v":"117","t":"number"}]
        //"t":"string"分类，t":"number"数值
        if(paramSearchVO.getType()==1){
            //

        }else if(paramSearchVO.getType()==2){
            //2：分类

        }


        return null;
    }

    @Override
    public List<EventUserRepVO> downLoadUserList(EventUserExcelVO eventUserExcelVO) {
        if(ovalUtils.validatorRequestParam(eventUserExcelVO).size()>0)return Collections.emptyList();
        return  eventMapper.getUserListExcel(eventUserExcelVO);
    }

    @Override
    public List<AnalysisListVO> getEventAnalysisListExcel(EventAnalysisExcelVO eventAnalysisExcelVO) {
        if(ovalUtils.validatorRequestParam(eventAnalysisExcelVO).size()>0)return Collections.emptyList();
        return  eventMapper.getEventAnalysisListExcel(eventAnalysisExcelVO);
    }


    @Override
    public Result batchShopInsert() {
        try{
            String dt= TimeUtils.getYestDayString();
            OnlinePage onlinePage=new OnlinePage();
            onlinePage.setDt(dt);
            onlineEventMapper.batchShopInsert(onlinePage);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Result.success();
    }

    @Override
    public Result batchAddCartInsert() {
        try{
            String dt= TimeUtils.getYestDayString();
            OnlinePage onlinePage=new OnlinePage();
            onlinePage.setDt(dt);
            onlineEventMapper.batchAddCartInsert(onlinePage);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Result.success();
    }
    @Override
    public Result batchCustInsert() {
        try{
            String dt= TimeUtils.getYestDayString();
            OnlinePage onlinePage=new OnlinePage();
            onlinePage.setDt(dt);
            onlineEventMapper.batchCustInsert(onlinePage);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Result.success();
    }
    @Override
    public Result batchSearchInsert() {
        try{
            String dt= TimeUtils.getYestDayString();
            OnlinePage onlinePage=new OnlinePage();
            onlinePage.setDt(dt);
            onlineEventMapper.batchSearchInsert(onlinePage);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Result.success();
    }

    @Override
    public Result getShop(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        OnlineTotalData onlineTotalData= onlineEventMapper.getShop(dataBroadVO);
        return Result.success(onlineTotalData);
    }
    @Override
    public Result getShopUrl(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<VisitpathVO> list= onlineEventMapper.getShopUrl(dataBroadVO);
        return Result.success(list);
    }

    @Override
    public Result getSearchTotal(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        return Result.success(onlineEventMapper.getSearchTotal(dataBroadVO));
    }
    /**
     *
     * @Description 电商热搜
     * @author xing.liu
     * @date 2021/5/25
     **/
    @Override
    public Result getSearch(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        return Result.success(onlineEventMapper.getSearch(dataBroadVO));
    }

    @Override
    public Result getAddcart(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        return Result.success(onlineEventMapper.getAddcart(dataBroadVO));
    }

    @Override
    public Result getAddcartProduct(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        return Result.success(onlineEventMapper.getAddcartProduct(dataBroadVO));
    }

    @Override
    public Result getCampion(CampaignVO campionVO) {
        if (ovalUtils.validatorRequestParam(campionVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        if(campionVO.getType()==1 &&StringUtils.isBlank(campionVO.getVisitpath()))return Result.error(ResponseCode.PARAMETERS_NULL);
        //CampionReqVO campionReqVO=new CampionReqVO();
        //campionReqVO.setTotalVO(onlineEventMapper.getCampionTotal(campionVO));
        //campionReqVO.setParams(onlineEventMapper.getParamList(campionVO));
        //campionReqVO.setDetails(onlineEventMapper.getParamDeatil(campionVO));
        return Result.success(onlineEventMapper.getCampionTotal(campionVO));
    }

    @Override
    public Result getParamList(CampaignVO campionVO) {
        if (ovalUtils.validatorRequestParam(campionVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        if(campionVO.getType()==1 &&StringUtils.isBlank(campionVO.getVisitpath()))return Result.error(ResponseCode.PARAMETERS_NULL);

        //CampionReqVO campionReqVO=new CampionReqVO();
        //campionReqVO.setTotalVO(onlineEventMapper.getCampionTotal(campionVO));
        //campionReqVO.setParams(onlineEventMapper.getParamList(campionVO));
        //campionReqVO.setDetails(onlineEventMapper.getParamDeatil(campionVO));
        return Result.success(onlineEventMapper.getParamList(campionVO));
    }

    @Override
    public Result getParamDeatil(CampaignVO campionVO) {
        if (ovalUtils.validatorRequestParam(campionVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        if(campionVO.getType()==1 &&StringUtils.isBlank(campionVO.getVisitpath()))return Result.error(ResponseCode.PARAMETERS_NULL);
        //CampionReqVO campionReqVO=new CampionReqVO();
        //campionReqVO.setTotalVO(onlineEventMapper.getCampionTotal(campionVO));
        //campionReqVO.setParams(onlineEventMapper.getParamList(campionVO));
        //campionReqVO.setDetails(onlineEventMapper.getParamDeatil(campionVO));
        return Result.success(onlineEventMapper.getParamDeatil(campionVO));
    }

    //@Scheduled(cron = "0/15 * * * * ? ") // 间隔5秒执行，0 5 0 * * ?
    public void get(){
         getCam();
    }


    public  void  getCam(){
        List<DetailVO> result=new ArrayList<>();
        String dt= TimeUtils.getYestDayString();
        CampionTimeVO ipPageVO=new CampionTimeVO();
        ipPageVO.setBeginTime("2020-09-07 15:00:00");
        ipPageVO.setEndTime("2020-09-07 16:00:00");
        ipPageVO.setPageSize(3000);
        Integer totalSize = onlineEventMapper.getCount(ipPageVO);
        if(totalSize==null){
            return  ;
        }
        int pageSize = ipPageVO.getPageSize();
        int totalPage = totalSize/pageSize;
        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = totalSize;
            }
        }
        //List<DetailVO> detailVOList = onlineEventMapper.getCamList(ipPageVO);

        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum - 1) * pageSize;
            ipPageVO.setPageIndex(starNum);
            List<DetailVO> detailVOList = onlineEventMapper.getCamList(ipPageVO);
            //请求对方接口发送数据


        }

    }




}
