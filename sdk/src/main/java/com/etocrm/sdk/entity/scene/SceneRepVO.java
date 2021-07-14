package com.etocrm.sdk.entity.scene;

import com.etocrm.sdk.entity.databroad.TotalDataRepVO;
import lombok.Data;

import java.util.List;

@Data
public class SceneRepVO {

    private TotalDataRepVO  totalDataRepVO;

    private List<SceneTotalDetailDomainRepVO> sceneTotalDetailDomain;

    private List<ChartDetailDomainRepVO> chartDetailDomain;

    private List<DateDetailData> dateDetailData;

    private List<SceneDetailData> sceneDetailData;
}
