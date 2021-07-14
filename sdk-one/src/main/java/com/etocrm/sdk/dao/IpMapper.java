package com.etocrm.sdk.dao;

import com.etocrm.sdk.entity.ip.IpPageVO;
import com.etocrm.sdk.entity.ip.IpReqVO;
import com.etocrm.sdk.entity.ip.IpVO;
import com.etocrm.sdk.entity.user.UserDeatil;

import java.util.List;
import java.util.Map;

public interface IpMapper {

    void batchInsert(List<IpVO> ipVOList);

    List<Map<String,Object>> selectIp(IpPageVO ipPageVO);

    Integer selectIpCount(IpPageVO ipPageVO);

    List<UserDeatil> selectUser(IpPageVO ipPageVO);

    Integer selectUserCount(IpPageVO ipPageVO);

    void batchUserInsert(List<UserDeatil> userDeatilList);

    void batchInsert1(List<IpReqVO> ipVOList);

    List<Map<String,Object>> selectIp1(IpPageVO ipPageVO);

    Integer selectIpCount1(IpPageVO ipPageVO);









}
