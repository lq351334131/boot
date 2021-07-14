package com.etocrm.sdk.entity.ip;

import lombok.Data;

@Data
public class IpPageVO {

    private Integer pageSize=10;

    private Integer pageIndex;

    private String dt;


}
