package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.scene.*;
import com.etocrm.sdk.entity.user.UserDatailVO;

import java.util.List;

public interface SceneService {

    Result dataGet(DataBroadVO dataBroadVO);

    List<SceneTotalDetailDomainRepVO> getexcel(DataBroadVO dataBroadVO);

    Result dataGetBySceneID(SceneDateVO sceneDateVO);

    List<UserDatailVO> getSceneIdexcel(SceneExcelVO sceneExcelVO);

    Result dataDetailGet(SceneDataVO sceneDataVO);

    Result getUserGet(SceneUserVO sceneUserVO);

    Result getSceneTotal(DataBroadVO dataBroadVO);

    Result dataGetBySceneID(SceneIdVO sceneIdVO);

}
