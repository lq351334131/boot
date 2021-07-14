package com.etocrm.sdk.entity.funnel;

import lombok.Data;

import java.util.List;

@Data
public class FunnelSingleRepVO {

    private  String id;

    private String funnelName;

    private Integer typeId;

    private String appKey;

    private String createTime;

    private List<FunnelSetpsIdVO> funnelSetps;
}
