package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.scene.SceneTotalDetailDomainRepVO;
import com.etocrm.sdk.entity.scene.SceneVO;

import java.util.List;

public interface SceneService {

    Result dataGet(SceneVO vo);

    List<SceneTotalDetailDomainRepVO> getexcel(SceneVO vo);
}
