package com.etocrm.sdk.entity.funnel;

import lombok.Data;

@Data
//添加步骤
public class FunnelSetpsVO {

   private String stepName;

   private Integer stepType;

   private String peId;

   private String funnelId;

}
