package com.etocrm.sdk.service;


import com.etocrm.sdk.base.Result;

import com.etocrm.sdk.entity.EventVO.*;

import java.util.List;

public interface EventService {

      Result getEventList(EventVO vo);

      List<EventListVO> getEvent(DownLoadExcel vo);

      Result downLoadEventList();

      Result getEventNameList(String appKey,String tv,String tl);

      Result getEventAnalysisList(EventAnalysisVO vo);

      Result getSingleEvent(String appKey, String id);

      Result editEvent(EventEditVO body);

      Result addEvent(EventEditVO body);

      Result getEventAnalysisDetails(EventAnalysisDeVo vo);

      Result getEventParamDetails(EventParamDetails vo);

      Result delEvent(String id);
}
