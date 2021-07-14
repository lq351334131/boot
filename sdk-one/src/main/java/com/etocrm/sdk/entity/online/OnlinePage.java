package com.etocrm.sdk.entity.online;

import lombok.Data;

@Data
public class OnlinePage {

    private Integer pageSize=10;

    private Integer pageIndex;

    private String dt;
}
