package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.campaign.CampaignVO;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.event.*;

import java.util.List;

public interface EventService {

    Result addEvent(EventAddVO eventAddVO);

    Result deleteEvent(String id);

    Result editEvent(EventAddVO eventAddVO);

    Result getEventList(EventVO eventVO);

    List<EventListVO> downloadPageTemplate(DownLoadExcel downLoadExcel);

    Result getEventNameList(DownLoadExcel downLoadExcel);

    Result getSingleEvent(String id);

    Result getEventAnalysisList(EventAnalysisVO eventAnalysisVO);

    Result getEventAnalysisDetails(EventIDVO eventIDVO);

    Result getEventParamList(ParamIDVO paramIDVO);

    Result getUserList(EventUserVO eventUserVO);

    Result getEventParamDetails(ParamSearchVO paramSearchVO);

    List<EventUserRepVO> downLoadUserList(EventUserExcelVO downLoadExcel);

    List<AnalysisListVO> getEventAnalysisListExcel(EventAnalysisExcelVO eventAnalysisExcelVO);

    Result batchShopInsert( );

    Result batchAddCartInsert();

    Result batchCustInsert();

    Result batchSearchInsert() ;

    Result getShop(DataBroadVO dataBroadVO);

    Result getShopUrl(DataBroadVO dataBroadVO);

    Result getSearchTotal(DataBroadVO dataBroadVO);

    Result getSearch(DataBroadVO dataBroadVO);

    Result getAddcart(DataBroadVO dataBroadVO);

    Result getAddcartProduct(DataBroadVO dataBroadVO);

    Result getCampion(CampaignVO campionVO);

    Result getParamList(CampaignVO campionVO);

    Result getParamDeatil(CampaignVO campionVO);
}
