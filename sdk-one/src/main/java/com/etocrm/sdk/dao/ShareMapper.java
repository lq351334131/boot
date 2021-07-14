package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.share.*;
import com.etocrm.sdk.entity.user.UserDatailVO;

import java.util.List;
import java.util.Map;

public interface ShareMapper {

    ShareUserVO getUserShare(DataBroadVO dataBroadVO);

    Integer getUserSharebackflow(DataBroadVO dataBroadVO);

    Integer getUserShareAdd(DataBroadVO dataBroadVO);

    List<ShareNumVO> getShareNumList(DataBroadVO dataBroadVO);

    List<ShareGenderVO> getShareGenderList(DataBroadVO dataBroadVO);

    List<ShareCityVO> getAreaShareLeaderboard(DataBroadVO dataBroadVO);

    List<SharePageTop10VO>  getSharePageTop10(DataBroadVO dataBroadVO);

    List<ShareNumVO> getShareUserPlusNumList(DataBroadVO dataBroadVO);

    List<SharePageRepVO> downLoadPageShareList(PageShareListVO pageShareListVO);

    List<SharePageRepVO> getPageShare(SharePage sharePage);

    Integer getPageShareCount(SharePage sharePage);

    List<SharePageRepVO> getPageSharebackoradd(PageShareListTypeVO pageShareListTypeVO);

    List<ShareNumVO> getShareTurnbackList(DataBroadVO dataBroadVO);

    List<Map<String,Object>> getShareCityList(SharepathVO sharepathVO);

    List<Map<String,Object>> getShareCityaddList(SharepathVO sharepathVO);

    List<SharePageTop10VO>getBesharedTop10Num(BesharedTop10VO besharedTop10VO);

    List<UserCityRepVO> getAreaShareListExcel(UserCitySeaVO userCitySeaVO);

    Integer getUserCityCount(UserCitySeaVO userCitySeaVO);

    List<UserCityRepVO>  getAreaShareListList(UserCitySeaPageVO userCitySeaPageVO);

    List<DateShareVO> getDateUser(DataBroadVO dataBroadVO);

    //List<Map<String,Object>> getDateUserback(DataBroadVO dataBroadVO);

    //List<Map<String,Object>> getDateUseradd(DataBroadVO dataBroadVO);

    List<DateShareVO> getDateUserPage(DateSharePageVO dataBroadVO);

    //List<Map<String,Object>> getDateUserbackPage(DateSharePageVO dataBroadVO);

    List<Map<String,Object>> getDateUseraddPage(DateSharePageVO dataBroadVO);

    Integer getShareAreaUserCount(UserSearchVO userSearchVO);

    List<UserDatailVO> getShareAreaUserList(UserSearchVO userSearchVO);

    List<String> getShareUrlList(DataBroadVO dataBroadVO);

    List<Map<String,Object>> getDateUserBuckPage(DateSharePageVO dateSharePageVO);

    List<Map<String,Object>> getDateUseraddAll(DataBroadVO DataBroadVO);

    List<Map<String,Object>> getDateUserBuckAll(DataBroadVO DataBroadVO);

    Integer getDateUserCount(DateSharePageVO dateSharePageVO);


    List<ShareUser> getShareUserList(ShareUserListVO shareUserListVO);

    Integer getShareUserCount(ShareUserListVO shareUserListVO);
}
