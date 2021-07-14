package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.dao.IpMapper;
import com.etocrm.sdk.entity.ip.IpPageVO;
import com.etocrm.sdk.entity.ip.IpReqVO;
import com.etocrm.sdk.entity.ip.IpVO;
import com.etocrm.sdk.entity.user.UserDeatil;
import com.etocrm.sdk.service.IpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IpServiceImpl implements IpService {

    @Autowired
    private IpMapper ipMapper;
    @Override
    public void batchInsert(List<IpVO> ipVOList) {
        ipMapper.batchInsert(ipVOList);
    }


    @Override
    public List<Map<String, Object>> selectIp(IpPageVO ipPageVO){
        return ipMapper.selectIp(ipPageVO);
    }

    @Override
    public Integer selectIpCount(IpPageVO ipPageVO) {
        return ipMapper.selectIpCount(ipPageVO);
    }

    @Override
    public List<UserDeatil> selectUser(IpPageVO ipPageVO) {
        return ipMapper.selectUser(ipPageVO);
    }

    @Override
    public Integer selectUserCount(IpPageVO ipPageVO) {
        return ipMapper.selectUserCount(ipPageVO);
    }

    @Override
    public void batchUserInsert(List<UserDeatil> userDeatilList) {
        try{
            ipMapper.batchUserInsert(userDeatilList);
        }catch (Exception e){
           log.error( e.getMessage(),e);
        }
    }

    @Override
    public void batchInsert1(List<IpReqVO> ipVOList) {
        try{
            ipMapper.batchInsert1(ipVOList);
        }catch (Exception e){
            log.error( e.getMessage(),e);
        }
    }

    @Override
    public Integer selectIpCount1(IpPageVO ipPageVO) {
        return ipMapper.selectIpCount1(ipPageVO);
    }

    @Override
    public List<Map<String, Object>> selectIp1(IpPageVO ipPageVO) {
        return ipMapper.selectIp1(ipPageVO);
    }
}
