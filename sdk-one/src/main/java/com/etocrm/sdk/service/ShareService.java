package com.etocrm.sdk.service;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.share.*;

import java.util.List;

public interface ShareService {

    Result getUserShare(DataBroadVO dataBroadVO);

    Result getShareNumList(DataBroadVO dataBroadVO);

    Result getShareGenderList(DataBroadVO dataBroadVO);

    Result getAreaShareLeaderboard(DataBroadVO dataBroadVO);

    Result getSharePageTop10(DataBroadVO dataBroadVO);

    Result getShareUserPlusNumList(DataBroadVO dataBroadVO);

    List<SharePageRepVO> downLoadPageShareList(PageShareListVO pageShareListVO);

    Result getPageShare(SharePage sharePage);

    Result getShareTurnbackList(DataBroadVO dataBroadVO);

    List<ShareCityVO> downLoadAreaShareLeaderboard(DataBroadVO dataBroadVO);

    List<UserCityRepVO> downLoadAreaShareList(UserCitySeaVO userCitySeaVO);

    Result getAreaShareList(UserCitySeaPageVO userCitySeaPageVO);

    Result getUserShareList(DataBroadVO dataBroadVO);

    Result getSharePageUserList(DateSharePageVO dateSharePageVO);

    Result getShareAreaUserList(UserSearchVO userSearchVO);

    Result getShareUserList(ShareUserListVO shareUserListVO);
}
