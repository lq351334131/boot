package com.etocrm.sdk.entity.page;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "页面")
public class PageEditVO {

    private String appKey;

    private  String id;

    private  String moduleName;

    private  String pathName;

    private  Integer pathTypeId;

    private  String visitPath;
}
