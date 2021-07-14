package com.etocrm.sdk.entity.share;

import lombok.Data;

import java.util.List;

@Data
public class ShareCityReqVO {

    private List<ShareCityVO>  datas;

    private List<ShareCityVOTopVO> echartsData;


}
