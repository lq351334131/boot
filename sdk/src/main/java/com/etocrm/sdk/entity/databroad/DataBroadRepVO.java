package com.etocrm.sdk.entity.databroad;

import lombok.Data;

import java.util.List;

@Data
public class DataBroadRepVO {

    private TotalDataRepVO  totalData;

    //页面访问
    private List<PathDataRepVO>   pathDatas;

    private List<PortalDetailDatasRepVO> portalDetailDatas;

    private List<RegDataRepVO> regDatas;

    private List<ScenDataRepVO> sceneDatas;

    private List<QRReqVO> qrDatas;
}
