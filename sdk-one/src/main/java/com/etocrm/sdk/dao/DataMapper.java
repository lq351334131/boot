package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.data.UserDataTypeVO;
import com.etocrm.sdk.entity.data.UserDataVO;

import java.util.List;
import java.util.Map;

public interface DataMapper {

    Integer getNewUser(UserDataVO userDataVO);

    Map<String,Integer> getVisNum(UserDataVO userDataVO);

    Integer getOpen(UserDataVO userDataVO);

    Integer getStopTime(UserDataVO userDataVO);

    //Integer getExit(UserDataVO userDataVO);

    //Integer getNewRegister(UserDataVO userDataVO);

    //Integer getOldReg(UserDataVO userDataVO);

    List<Map<String,Object>> getDataList(UserDataTypeVO userDataVO);

    List<Map<String, Object>> getKeyAll(String dateDay);

    List<Map<String, Object>> getYestDay(Map<String, Object> map);

    Integer getNewmember(UserDataVO userDataVO);


}
