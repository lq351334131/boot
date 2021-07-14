package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.page.*;
import com.etocrm.sdk.entity.user.UserDatailVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PageService {

    Result importExcel(MultipartFile file);

    Result getPageList(PageListVO vo);

    Result getSinglePageParameter(String id);

    Result updatePage(PageListResVO pageListResVO);

    Result deleteId(String id);

    Result getPageParameterList(PageChannelVO vo);

    Result addChannel(PageChannelAddVO vo);

    Result editPageParameter(PageChannelEditVO pageChannelEditVO);

    Result getSinglePage(String id);

    Result getVisitPage(PageAccessVo vo);

    Result getVisitPageList(VisitPageVO visitPageVO);

    Result getHomePage(PageAccessVo vo);

    Result getHomePageList(VisitPageVO visitPageVO);

    List<HomePageExcel> downLoadHomePageList(VisitVO visitVO);

    List<VisitPageExcel> downLoadVisPageList(VisitVO visitVO);

    Result getUserList(PageUserVO userVO);

    List<UserDatailVO> downLoadUserList(PageUserVO userVO);

    Result getPageVisitHabitFrequency(PageVO pageVO);

    List<PageDepthVO> downLoadPageVisitHabitFrequency(PageVO pageVO);

    Result getPageVisitHabitDepth(PageVO pageVO);

    List<PageDepthVO> downLoadPageVisitHabitDepth(PageVO pageVO);

    Result getPageVisitHabitTime(PageVO pageVO);

    List<PageDepthVO> downLoadPageVisitHabitTime(PageVO pageVO);

    Result getPageNameList();

    Result getPageParameterDataList(ParamDataSearchVO paramDataSearchVO);

    List<ParamDataRepVO> downloadPageParameterDataList(ParamDataSearchExcelVO paramDataSearchExcelVO);

    Result getPageParameterUserList(PageParameterPageSearchVO pageParameterPageSearchVO);

    List<UserDatailVO> downloadPageParameterUserList(PageParameterUserSeaVO pageParameterUserSeaVO);
}
