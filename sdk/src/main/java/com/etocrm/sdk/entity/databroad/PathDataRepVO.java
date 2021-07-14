package com.etocrm.sdk.entity.databroad;

import lombok.Data;

@Data
public class PathDataRepVO {

    //访问页面
    private String visitPath;

    private Long userNum;

    private Long visitNum;

    private String exitRate;

    private String avgDate;
}
