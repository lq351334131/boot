package com.etocrm.sdk.entity.scene;

import lombok.Data;

@Data
public class ChartDetailDomainRepVO {

    private String scene;

    private String sceneName;

    private Long newUserNum;

    private Long userNum;

    private Long visitNum;

    private Long openNum;

    private Long totalNewUseridCount;

    private Long totalUseridCount;

    private Long totalUseridVisitCount;

    private Long totalTotalSimpleBakCount;

    //次时长
    private Long distanceValue;

    private String avgStopTime;

    private String exitRate;
}
