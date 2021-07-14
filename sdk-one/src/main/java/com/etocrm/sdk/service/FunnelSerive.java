package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.funnel.*;

import java.util.List;

public interface FunnelSerive {

    Result getFunnelAnalysisList(FunnelVO vo);

    Result addFunnel(FunnelAddVO vo);

    Result delFunnel(String id);

    Result editFunnel(FunnelEditVO vo);

    Result getSingleFunnel(FunnelOneVO id);

    List<FunnelRepExcelVO> downloadFunnelAnalysisList(DownLoadFunnelExcel vo);

    Result getPageRate();
}
