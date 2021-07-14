package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.user.*;

import java.util.List;

public interface UserRetentionMapper {

    UserNewAddRepVO getUserActiveDate(DataBroadVO dataBroadVO);

    UserNewAddRepVO getUserNewAddRetention(DataBroadVO dataBroadVO);

    Integer getSilent(SlientLostVO slientLostVO);

    Integer getLost(SlientLostVO slientLostVO);

    List<UserDatailVO> getSilentUserPage(SlientLostVO userLostTypeVO);

    List<UserDatailVO> getLostUserPage(SlientLostVO userLostTypeVO);

    List<UserDatailVO> getSilentUserExcel(UserLostTypeVO userLostTypeVO);

    List<UserDatailVO> getLostUserPageExcel(UserLostTypeVO userLostTypeVO);



}
