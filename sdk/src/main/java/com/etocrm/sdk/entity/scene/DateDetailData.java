package com.etocrm.sdk.entity.scene;

import lombok.Data;

@Data
public class DateDetailData {

    private String scene;

    private String sceneName;

    private Long newUserNum;

    private Long userNum;

    private Long visitNum;

    private Long openNum;

    private Long distanceValue;

    private String avgStopTime;

    private String exitRate;

    private String date;
}
