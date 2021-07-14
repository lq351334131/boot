package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.databroad.*;

import java.util.List;
import java.util.Map;

public interface DataBroadMapper {

    Integer getNewUser(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getVisNum(DataBroadVO dataBroadVO);

    Integer getOpen(DataBroadVO dataBroadVO);

    Integer getExit(DataBroadVO dataBroadVO);

    List<PathDataRepVO> getTopVisNum(DataBroadVO dataBroadVO);

    //List<Map<String, Object>> getTopExite(DataBroadVO dataBroadVO);

    List<ScenDataRepVO> getTopSence(DataBroadVO DataBroadVO);

    //List<Map<String, Object>> getTopSenceExite(DataBroadVO dataBroadVO);

    List<RegDataRepVO> getTopProvince(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getDate(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getNewUserDate(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getUserDate(DataBroadVO dataBroadVO);

    List<Map<String, Object>>getOpenDate(DataBroadVO dataBroadVO);

    List<QRReqVO> getQrData(DataBroadVO dataBroadVO);

    List<QRReqVO> getQrDataNewUser(DataBroadLIstVO dataBroadLIstVO);

    TotalDataRepVO getTotalDataRepVO(DataBroadVO dataBroadVO);

    List<PortalDetailDatasRepVO> getPortalDetailDatas(DataBroadVO dataBroadVO);

    Integer getBounce(DataBroadVO dataBroadVO);

    Integer getTopExitVisNum(DataBroadLIstVO dataBroadLIstVO);

    Integer getTopOpen(DataBroadLIstVO dataBroadLIstVO);

    Integer getTime(DataBroadLIstVO dataBroadLIstVO);

    Integer getTotalnewUser(DataBroadVO dataBroadVO);

    List<PortalDetailDatasRepVO> getTotalnewUserDate(DataBroadVO dataBroadVO);

    Integer getVisUserNum(DataBroadVO dataBroadVO);

}
