package com.etocrm.sdk.entity.scene;

import lombok.Data;

import java.util.List;

@Data
public class DataSceneIDVO {

    private TotalSceneData totalData;

    private List<SceneTotalDetailDomain> sceneTotalDetailDomain;

    private List<DateDetailData> sceneDateDetailDomain;


}
