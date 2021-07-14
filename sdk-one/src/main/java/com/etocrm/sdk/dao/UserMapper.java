package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.user.*;

import java.util.List;
import java.util.Map;

public interface UserMapper {

     List<Map<String,Object>> getUserImg(UserVO userVO);

     List<Map<String,Object>> getUserImgNewUser(UserVO userVO);

     List<Map<String,Object>> getUserImgVisoUser(UserVO userVO);

     List<Map<String,Object>> getUserImgExit(UserVO userVO);

     List<Map<String,Object>> getUserImgOpen(UserVO userVO);

     //List<Map<String,Object>> getUserImgNewUserTotal(UserVO userVO);

     List<Map<String,Object>> getUserImgVisoUserTotal(UserVO userVO);

     List<UserDatailVO> getUserPage(UserQueryColVO userVO);

     Integer getUserCount(UserQueryColVO userVO);

     List<Map<String,Object>> getUserVisDate(DataBroadVO dataBroadVO);

     List<Map<String,Object>> getExitDate(DataBroadVO dataBroadVO);

     List<UserActiveRepVO> getUserActiveDate(UserVO userVO);

     Integer getUserActive(UserVO userVO);

     Integer getStopTime(UserTypeVO userTypeVO);

     Integer getDayStopTime(DataBroadVO dataBroadVO);

     List<UserActiveRepVO> getUserActiveDatePage(UserPageVO userPageVO);

     List<UserDateRepVO> getPortalDetailDatas(DataBroadVO dataBroadVO);

     List<Map<String,Object>> getUserStatisticsGroupByDateSummary1(UserPageVO userPageVO);

     List<UserStaticDate> getUserStatisticsGroupByDateSummary2(UserPageVO userPageVO);

     List<UserStaticDate> getUserStatisticsGroupByDateTiaochu(UserPageVO userPageVO);

     List<UserStaticDate> getUserStatisticsGroupByDateStop(UserPageVO userPageVO);

     Integer getUserStatisticsGroupByDateCount(DataBroadVO dataBroadVO);

     List<UserDetailsReqVO> getUserDetail(UserDetailsVO userDetailsVO);


}
