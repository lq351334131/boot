package com.etocrm.sdk.service;


import com.etocrm.sdk.base.Result;



import java.util.List;

public interface EventService {

      Result getEventList(com.etocrm.sdk.entity.eventVO.EventVO vo);

      List<com.etocrm.sdk.entity.eventVO.EventListVO> getEvent(com.etocrm.sdk.entity.eventVO.DownLoadExcel vo);

      Result downLoadEventList();

      Result getEventNameList(String appKey,String tv,String tl);

      Result getEventAnalysisList(com.etocrm.sdk.entity.eventVO.EventAnalysisVO vo);

      Result getSingleEvent(String appKey, String id);

      Result editEvent(com.etocrm.sdk.entity.eventVO.EventEditVO body);

      Result addEvent(com.etocrm.sdk.entity.eventVO.EventEditVO body);

      Result getEventAnalysisDetails(com.etocrm.sdk.entity.eventVO.EventAnalysisDeVo vo);

      Result getEventParamDetails(com.etocrm.sdk.entity.eventVO.EventParamDetails vo);

      Result delEvent(String id);
}
