package com.etocrm.sdk.dao;


import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.event.*;

import java.util.List;
import java.util.Map;

public interface EventMapper {

    void insert(EventAddVO eventListVO);

    void insertParam(List<EventParam> eventParam);

    void deleteId(String id);

    void deleteParamId(String id);

    void update(EventAddVO eventListVO);

    void updateParam(EventParam eventParam);

    List<EventListPageVO> getPage(EventVO eventVO);

    Integer getEventCount(EventVO eventVO);

    List<EventListVO> getEventExcel(DownLoadExcel downLoadExcel);

    List<EventClickhouse> getId(String id);

    List<AnalysisListVO> getEventAnalysisListPage(EventAnalysisVO eventAnalysisVO);

    Integer getEventAnalysisCount(EventAnalysisVO eventAnalysisVO);

    List<EventDateRepVO> getEventId(EventIDPO eventIDPO);

    EventIDPO getTvId(String id);

    List<EventParamListVO> getEventParamList(Map<String, Object> map);

    List<EventParamListVO> getEventParamList(ParamIDVO paramIDVO);

    List<EventUserRepVO> getUserList(EventUserTVVO eventUserTVVO);

    Integer getUserCount(EventUserTVVO eventUserTVVO);

    List<EventUserRepVO> getUserListExcel(EventUserExcelVO eventUserExcelVO);

    List<AnalysisListVO> getEventAnalysisListExcel(EventAnalysisExcelVO eventAnalysisExcelVO);

    Map<String,Object> getSearch(DataBroadVO dataBroadVO);
}
