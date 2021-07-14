package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.campaign.*;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.online.*;

import java.util.List;

public interface OnlineEventMapper {

    List<Shop> getShopList(OnlinePage onlinePage);

    Integer getShopCount(OnlinePage onlinePage);

    void batchShopInsert(OnlinePage onlinePage);

    void batchAddCartInsert(OnlinePage onlinePage);

    void batchCustInsert(OnlinePage onlinePage);

    void batchSearchInsert(OnlinePage onlinePage);

    OnlineTotalData getShop(DataBroadVO dataBroadVO);

    List<VisitpathVO> getShopUrl(DataBroadVO dataBroadVO);

    List<SearchVO> getSearchTotal(DataBroadVO dataBroadVO);

    Integer getSearch(DataBroadVO dataBroadVO);

    OnlineTotalData getAddcart(DataBroadVO dataBroadVO);

    List<AddCartVO> getAddcartProduct(DataBroadVO dataBroadVO);

    TotalVO getCampionTotal(CampaignVO campionVO);

    List<ParamList> getParamList(CampaignVO campionVO);

    List<DetailVO> getParamDeatil(CampaignVO campionVO);

    List<ParamList> getP(CampaignVO campionVO);

    Integer getCount(CampionTimeVO campionTimeVO);

    List<DetailVO> getCamList(CampionTimeVO campionTimeVO);








}
