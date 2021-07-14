package com.etocrm.sdk.entity.ip;

import lombok.Data;

@Data
public class IpReqVO {

    private String reqIp;

    private String province;

    private String city;

    private String dt;
}
