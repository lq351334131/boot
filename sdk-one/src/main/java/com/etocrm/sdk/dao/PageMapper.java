package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.page.*;
import com.etocrm.sdk.entity.user.UserDatailVO;

import java.util.List;
import java.util.Map;

public interface PageMapper {

    Integer batchInsert(List<PageListDownloadVO> list);

    List<PageListResVO> getPageListPage(PageListVO pageListVO);

    Integer getPageCount(PageListVO pageListVO);

    PageListResVO getPageId(String id);

    void updatePage(PageListResVO pageListResVO);

    void deleteId(String id);

    List<PageChannelListVO> getPageChannelId(PageChannelVO pageChannelVO);

    Integer getPageChannelCount(PageChannelVO pageChannelVO);

    void insert(PageChannelListVO pageChannelList);

    void updateChannel(PageChannelListVO pageChannelList);

    PageChannelListVO getPageChanelId(String id);

    VisitTotalVO getVisitPage(PageAccessVo pageAccessVo);

    List<VisitTotalPVO> getPageVistList(VisitPageVODTO visitPageVO);

    Integer getPageVistCount(VisitPageVODTO visitPageVO);

    HomeTotalVO getHomePage(PageAccessVo pageAccessVo);

    Integer getPageHomeCount(VisitPageVODTO visitPageVODTO);

    //List<HomeVO> getHomePageVistList(VisitPageVO visitPageVO);

    Map<String, Object> getStopTime(StopTimeVO stopTimeVO);

    List<PageListResVO> getPageList();

    List<VisitPageExcel> getPageVistListAll(VisitPageVO visitPageVO);

    List<UserDatailVO> getUserPage(PageUserVO userVO);

    Integer getUserCount(PageUserVO userVO);

    List<UserDatailVO> getUserPageAll(PageUserVO userVO);

    Integer getTotal(PageVO pageVO);

    List<Map<String, Object>> getPageVisitHabitFrequency(PageVO pageVO);

    List<PageDepthVO> getPageVisitHabitDepth(PageVO pageVO);

    Integer getPageVisitHabitDepthTotal(PageVO pageVO);

    Integer getOpen(PageVO pageVO);

    Integer getPageVisitHabitTime(PageVO pageVO);

    List<Map<String, Object>> getDuration();

    List<PageListResVO> getPageNameList();

    Integer getPageParameterDataCount(ParamDataSearchVO paramDataSearchVO);

    List<Map<String, Object>> getPageParameterDataList(ParamDataSearchVO paramDataSearchVO);

    List<Map<String, Object>> getParamSdk(ParamDataSearchVO paramDataSearchVO);

    List<UserDatailVO> getPageParameterUserListExcel(PageParameterUserSeaVO paramDataSearchVO);

    List<PageListResVO> getPageVisList(VisitPageVO visitPageVO);

    VisitTotalPVO getPageVistSharse(VisitPageVO visitPageVO);

    VisitTotalPVO getPageVistExit(VisitPageVO visitPageVO);

    List<HomeVO> getPageHomeList(VisitPageVODTO visitPageVODTO);

    List<HomePageExcel> getPageHomeExcel(VisitPageVODTO visitPageVODTO);

    Integer getHomeStopTime(StopTimeVO stopTimeVO);

    Integer getOpenTime(StopTimeVO stopTimeVO);

    List<Map<String, Object>> getHomeStopTimeExcel(VisitPageVODTO visitPageVODTO);

    List<Map<String, Object>> getVisStopTimeExcel(VisitPageVODTO visitPageVODTO);

    List<Map<String, Object>> getVisOpenExcel(VisitPageVODTO visitPageVODTO);

    List<Map<String, Object>> getVisExitExcel(VisitPageVODTO visitPageVODTO);

    List<Map<String, Object>> getParamName(Map<String, Object> map);

    List<Map<String, Object>> getExitName(Map<String, Object> map);

    List<Map<String, Object>> getPageParameterDataExcel(ParamDataSearchVO paramDataSearchVO);

    List<UserDatailVO> getPageParamUserListExcel(PageParameterUserSeaVO pageParameterUserSeaVO);

    List<Map<String, Object>> getPageshowTotal(StopTimeVO stopTimeVO);

    List<VisitPageExcel> getPageVisExecl(VisitVO visitPageVO);

    Long getPageshowHomeTotal(StopTimeVO stopTimeVO);

    List<PageListDownloadVO> getVis();

    List<Map<String, Object>> getPageParameterDataList1(ParamDataSearchVO paramDataSearchVO);

    List<Map<String, Object>> getPageParameterDataExcel1(ParamDataSearchVO paramDataSearchVO);

    Map<String,String> getParamOne(PageParameterSearchVO pageParameterSearchVO);

   List<UserDatailVO> getSdkParam(PageParameterSearchVO pageParameterSearchVO);

    Integer getSdkParamCount(PageParameterSearchVO pageParameterSearchVO);

    List<ParamDataRepVO> getPageParameterDataList3(ParamDataSearchVO paramDataSearchVO);

    List<Map<String,Object>> getPageParameterDataList4(ParamDataSearchVO paramDataSearchVO);

    Long  getPageTime(PageAccessVo pageAccessVo);

    List<HomeVO> getPageAccVisList(VisitPageVO visitPageVO);

    Integer  getPageAccVisCount(VisitPageVO visitPageVO);

    List<VisitTotalPVO> getPageVistListNew(VisitPageVO visitPageVO);

    Integer  getPageVistListCount(VisitPageVO visitPageVO);

    List<HomePageExcel> getPageHomeExcelNew(VisitVO visitVO);

    List<Map<String,Object>> getEntryTime(VisitVO visitVO);



}
