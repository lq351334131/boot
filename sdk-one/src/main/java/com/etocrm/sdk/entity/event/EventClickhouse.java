package com.etocrm.sdk.entity.event;

import lombok.Data;

import java.util.Date;

@Data
public class EventClickhouse {

    private String id;

    private String eventKey;

    private String eventName;

    private Integer type;


    private String k;

    private String name;

    private String  eventId;

    private Date createTime;

}
