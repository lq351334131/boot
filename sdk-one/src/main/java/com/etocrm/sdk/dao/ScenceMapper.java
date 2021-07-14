package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.scene.*;
import com.etocrm.sdk.entity.user.UserDatailVO;
import com.etocrm.sdk.entity.user.UserLostReturnRepVO;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface ScenceMapper {

    List<Map<String, Object>> getSceneAll(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getNewUser(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getVisNum(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getOpen(DataBroadVO dataBroadVO);

    BigInteger getStopTime(SceneType dataBroadVO);

    List<Map<String, Object>> getSceneDateAll(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getSceneTopfive(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getNewUserDate(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getVisNumDate(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getOpenDate(DataBroadVO dataBroadVO);

    List<UserLostReturnRepVO> getUserScenePage(SceneDateVO sceneDateVO);

    Integer getUserSenceCount(SceneDateVO sceneDateVO);

    List<UserDatailVO> getUserSceneId(SceneExcelVO sceneExcelVO);

    List<Map<String, Object>> getScenePage(SceneDataVO sceneDataVO);

    Integer getNewUserScene(SceneDataTypeVO sceneDataTypeVO);

    List<Map<String, Object>> getVisNumScene(SceneDataTypeVO sceneDataTypeVO);

    Integer getOpenScene(SceneDataTypeVO sceneDataTypeVO);

    Integer getUserGetCount(SceneUserVO sceneUserVO);

    List<UserDatailVO> getUserGet(SceneUserVO sceneUserVO);

    List<Map<String,Object>> getSceneDetailInfo();

    List<Map<String, Object>> getExitDate(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getVisExitNum(DataBroadVO dataBroadVO);

    Integer getVisExitScene(SceneDataTypeVO sceneDataTypeVO);

    List<Map<String,Object>> getSceneStopTime(DataBroadVO dataBroadVO);

    void batchInsert(List<SceneCkVO> sceneCkVO);

    SceneTotalVO getUserShareScene(DataBroadVO dataBroadVO);

    Integer getUserSharebackflow(DataBroadVO dataBroadVO);

    Integer getUserShareAdd(DataBroadVO dataBroadVO);

    List<Map<String, Object>> getScene(SceneIdVO sceneIdVO);

    List<Map<String, Object>> getSceneIdDate(SceneIdVO sceneIdVO);



}

