package com.etocrm.sdk.entity.VO;

import lombok.Data;

@Data
public class PageEditVO {

    private String appKey;

    private  String id;

    private  String moduleName;

    private  String pathName;

    private  Integer pathTypeId;

    private  String visitPath;
}
