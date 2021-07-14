package com.etocrm.sdk.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserSummaryVO {

    private  Integer newUsersCount;

    private Integer  totalVisitors;

    private Integer  totalCountTimes;

    private Integer  openCountTimes;

    private String avgStayTimes;

    private String bounceRate;

    @JsonIgnore
    private Long avgStopTime;

}
