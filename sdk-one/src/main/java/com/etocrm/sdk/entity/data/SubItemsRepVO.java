package com.etocrm.sdk.entity.data;

import lombok.Data;

import java.util.List;

@Data
public class SubItemsRepVO {

    private String appKey;

    private  String  appName;

    private List<DataRepVO> subItems;
}
