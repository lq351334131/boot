package com.etocrm.sdk.entity.event;

import lombok.Data;

@Data
public class DownLoadExcel {

    private String appkey;

    private String  eventKey;

    private String  eventName;
}
