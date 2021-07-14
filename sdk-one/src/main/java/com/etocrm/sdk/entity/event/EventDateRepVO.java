package com.etocrm.sdk.entity.event;

import lombok.Data;

@Data
public class EventDateRepVO {

    private String date;

    private Integer userNum;

    private Integer eventNum;

    private Integer avgEventNum;
}
