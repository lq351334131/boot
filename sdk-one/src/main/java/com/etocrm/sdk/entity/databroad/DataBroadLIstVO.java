package com.etocrm.sdk.entity.databroad;

import lombok.Data;

import java.util.List;

@Data
public class DataBroadLIstVO extends  DataBroadVO {

    private List<String> page;

    private  String visitpath;

    private Integer scene;

}
