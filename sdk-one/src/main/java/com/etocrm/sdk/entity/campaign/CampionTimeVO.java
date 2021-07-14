package com.etocrm.sdk.entity.campaign;

import lombok.Data;

@Data
public class CampionTimeVO {

    private String  beginTime;

    private String endTime;

    private Integer pageSize=100;

    private Integer pageIndex=1;


}
