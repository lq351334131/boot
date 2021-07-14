package com.etocrm.sdk.entity.scene;

import lombok.Data;

@Data
public class ChartDetailDomainRepVO {

    private Integer scene;

    private String sceneName;

    private Integer newUserNum;

    private Integer userNum;

    private Integer visitNum;

    private Integer openNum;

    private Integer totalNewUseridCount;

    private Integer totalUseridCount;

    private Integer totalUseridVisitCount;

    private Integer totalTotalSimpleBakCount;

    //次时长
    private Integer distanceValue;

    private String avgStopTime;

    private String exitRate;
}
