package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.VO.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PageService {

    Result getPageNameList();

    Result getHomePage(PageAccessVo vo);

    Result getPageVisitHabitFrequency(PageVO vo);

    Result getPageList(PageListVO vo);

    List<PageListDownloadVO> downloadPageList(PageDownloadVO vo);

    Result getSinglePageParameter(String id);

    Result editPageParameter(PageEditVO vo);

    Result getPageParameterList(PageChannelVO vo);

    Result editChannel(PageChannelEditVO vo);

    Result getChannelId(String id);

    Result delChannel(String id);

    Result addChannel(PageChannelEditVO  vo);

    Result addPage(PageEditVO vo);

    Result importExcel(MultipartFile file);

    Result getVisitPageList(VisitPageVO vo);

    Result getVisitModulePage(ModelVO vo);

    Result getVisitPage(PageAccessVo vo);

    List<VisitPageResVO> downLoadVisitPageList(VisitVO vo);

    List<HomePageExcel> downLoadHomePageList(HomePageVO vo);

    Result getHomePageList(VisitPageVO vo);

    Result getPageVisitHabitDepth(PageVO vo);

    List<PageDepthVO>downLoadPageVisitHabitDepth(PageVO vo);

    Result getPageVisitHabitTime(PageVO vo);

    List<PageDepthVO> downLoadPageVisitHabitTime(PageVO vo);

    List<PageDepthVO> downLoadPageVisitHabitFrequency(PageVO vo);

    Result getPagesPathFirst(PageVO vo);

    Result getPagePathNode(PageNodeVO vo);
}
