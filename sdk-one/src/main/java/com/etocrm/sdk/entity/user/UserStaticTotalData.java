package com.etocrm.sdk.entity.user;

import lombok.Data;

@Data
public class UserStaticTotalData {

    private UserStaticDate newUsersGroupByDate;

    /**
     *
     * @Description 每日访问人数集合
     * @author xing.liu
     * @date 2021/5/28
     **/
    private UserStaticDate totalVisitorsGroupByDate;

    /**
     *
     * @Description 访问参数
     * @author xing.liu
     * @date 2021/5/28
     **/
    private UserStaticDate totalCountTimesGroupByDate;

    private UserStaticDate openCountTimesGroupByDate;

    private UserStaticDate avgStayTimesGroupByDate;

    private UserStaticDate bounceRateGroupByDate;


}
