package com.etocrm.sdk.mysqldao;

import com.etocrm.sdk.entity.data.UserDataVO;
import com.etocrm.sdk.entity.data.YestDayRepVO;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.total.ToTalPO;
import com.etocrm.sdk.entity.user.UserActiveRepVO;
import com.etocrm.sdk.entity.user.UserDateRepVO;
import com.etocrm.sdk.entity.user.UserPageVO;

import java.util.List;
import java.util.Map;

public interface MysqlMapper {

    Integer insertTotal(List<ToTalPO> toTalPO);

    ToTalPO getYesList(DataBroadVO dataBroadVO);

    List<Map<String,Object>> getDataList(UserDataVO userDataVO);

    ToTalPO getBindDataList(UserDataVO userDataVO);

    List<YestDayRepVO> getYestDataList(UserDataVO userDataVO);

    List<UserDateRepVO> getDateList(DataBroadVO dataBroadVO);

    List<UserActiveRepVO> getActiveList(UserPageVO userPageVO);

    Integer getActivieCount(UserPageVO userPageVO);

    List<UserActiveRepVO> getActiviePage(UserPageVO userPageVO);

    ToTalPO getNewMember(UserDataVO userDataVO);

    /**
     * 
     * @Description 用户分析界面统计汇总日期明细
     * @author xing.liu
     * @date 2021/5/28
     **/
    List<Map<String,Object>>  getUserStaticData(UserPageVO userPageVO);

    Integer getUserStaticDataCount(UserPageVO userPageVO);






}
