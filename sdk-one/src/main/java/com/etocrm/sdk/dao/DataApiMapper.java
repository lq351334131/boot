package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.dataapi.*;

import java.util.List;

public interface DataApiMapper {

    List<DataApiReqVO> getVisit(DataApiVO dataApiVO);

    List<TotalReqVO> getTotal(DataApiVO dataApiVO);

    List<EverydayReqVO> getEveryday(DataApiVO dataApiVO);

    List<RegReqVO> getReg(DataApiVO dataApiVO);

    List<SceneReqVO> getScene(DataApiVO dataApiVO);

    List<SceneReqVO> getQrCode(DataApiVO dataApiVO);

    List<EventReqVO> getEvent(DataApiVO dataApiVO);

    List<OnlineReqVO> getShop(DataApiVO dataApiVO);

    List<OnlineReqVO> getSearch(DataApiVO dataApiVO);

    List<DataApiReqVO> getShare(DataApiVO dataApiVO);

    List<TotalReqVO> getShareTotal(DataApiVO dataApiVO);

    List<EntryReqVO> getEntryTotal(DataApiVO dataApiVO);

    List<EntryReqVO> getEntryVisTotal(DataApiVO dataApiVO);

    List<OpenReqVO> getOpenTotal(DataApiVO dataApiVO);

    List<OpenDetailReqVO> getOpenDetail(DataApiVO dataApiVO);

}
