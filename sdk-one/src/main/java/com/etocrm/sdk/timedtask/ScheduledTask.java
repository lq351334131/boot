package com.etocrm.sdk.timedtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.entity.ip.IpPageVO;
import com.etocrm.sdk.entity.ip.IpVO;
import com.etocrm.sdk.entity.user.UserDeatil;
import com.etocrm.sdk.service.IpService;
import com.etocrm.sdk.util.OkHttpUtil;
import com.etocrm.sdk.util.TimeUtils;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ScheduledTask {

    private static final String IP_URL = "http://iploc.market.alicloudapi.com/v3/ip";
    @Autowired
    private IpService ipService;


    //@Scheduled(cron = "0/15 * * * * ? ") // 间隔5秒执行，0 5 0 * * ?
    //@Scheduled(cron = " 0 0 1 * * ?")
    public void getSed() {
      try{
          log.info("----ip定时任务开始执行-----");
          getIpList();
      }catch (Exception e){
          log.error(e.getMessage(),e);
      }
    }

    //@Scheduled(cron = "0/15 * * * * ? ") // 间隔5秒执行，0 5 0 * * ?
    public void insertUser() {
        try{
            log.info("----USERINFO-----");
            getUserList();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public  void getIpList(){
        String dt= TimeUtils.getYestDayString();
        IpPageVO ipPageVO=new IpPageVO();
        ipPageVO.setDt(dt);
        ipPageVO.setPageSize(3000);
        Integer totalSize = ipService.selectIpCount(ipPageVO);
        if(totalSize==null){
            return ;
        }
        int pageSize = ipPageVO.getPageSize();
        int totalPage = totalSize/pageSize;
        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = totalSize;
            }
        }
        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum - 1) * pageSize;
            ipPageVO.setPageIndex(starNum);
            List<Map<String, Object>> ipList = ipService.selectIp(ipPageVO);
            List<IpVO> ipList1 = getIpList(ipList);
            if(ipList1.size()>0){
                ipService.batchInsert(ipList1);
                getIpList();
            }
        }
    }

    public  List<IpVO>  getIpList(List<Map<String, Object>> ipList){
        List<IpVO>list=new ArrayList<>();
        for(Map<String, Object> map:ipList){
            String ip = (String) map.get("reqIp");
            String appkey = (String) map.get("appkey");
            String uu = (String) map.get("uu");

            String ipInfo = OkHttpUtil.get(IP_URL, ImmutableMap.of("ip", ip));
            if (!Strings.isNullOrEmpty(ipInfo)) {
                JSONObject ipobj = JSON.parseObject(ipInfo);
                if (ipobj.getString("status").equals("1")) {
                    String province = ipobj.getString("province");
                    String city = ipobj.getString("city");
                    if(StringUtils.isNotBlank(province)&&StringUtils.isNotBlank(city)){
                        IpVO ipVO=new IpVO();
                        ipVO.setCity(city);
                        ipVO.setProvince(province);
                        ipVO.setReqIp(ip);
                        ipVO.setAppkey(appkey);
                        ipVO.setUu(uu);
                        list.add(ipVO);
                    }
                }
            }
        }
        return list;
    }

    public  void getUserList(){
        String dt= TimeUtils.getYestDayString();
        IpPageVO ipPageVO=new IpPageVO();
        ipPageVO.setDt(dt);
        ipPageVO.setPageSize(3000);
        Integer totalSize = ipService.selectUserCount(ipPageVO);
        if(totalSize==null){
            return ;
        }
        int pageSize = ipPageVO.getPageSize();
        int totalPage = totalSize/pageSize;
        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = totalSize;
            }
        }
        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum - 1) * pageSize;
            ipPageVO.setPageIndex(starNum);
            List<UserDeatil> ipList = ipService.selectUser(ipPageVO);
            if(ipList.size()>0){
                ipService.batchUserInsert(ipList);
                getUserList();
            }
        }

    }
}
