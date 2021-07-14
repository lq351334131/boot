package com.etocrm.sdk.timedtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.entity.ip.IpPageVO;
import com.etocrm.sdk.entity.ip.IpReqVO;
import com.etocrm.sdk.service.IpService;
import com.etocrm.sdk.util.OkHttpUtil;
import com.etocrm.sdk.util.TimeUtils;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
class IpTask{

    private static final String IP_URL = "http://iploc.market.alicloudapi.com/v3/ip";
    @Autowired
    private IpService ipService;

    ExecutorService executorService= Executors.newFixedThreadPool(8);

    @Scheduled(cron = " 0 30 0 * * ?")
    public void getSed() {
        Long startTime=System.currentTimeMillis();
        try{
            log.info("----ip定时任务开始执行-----");
            getIpList();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        Long endTime=System.currentTimeMillis();
        log.info("批量获取IP所耗时间："+(endTime-startTime));
    }

    public  void getIpList(){
        String dt= TimeUtils.getYestDayString();
        IpPageVO ipPageVO=new IpPageVO();
        ipPageVO.setDt(dt);
        ipPageVO.setPageSize(3000);
        Integer totalSize = ipService.selectIpCount1(ipPageVO);
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
            List<Map<String, Object>> ipList = ipService.selectIp1(ipPageVO);
            List<IpReqVO> ipList1 = getIpList(ipList);
            if(ipList1.size()>0){
                ipService.batchInsert1(ipList1);
            }
        }
    }



    public  List<IpReqVO>  getIpList(List<Map<String, Object>> ipList){
        List<IpReqVO>list=new ArrayList<>();
        if(ipList.size()==0){
            return list;
        }
        CountDownLatch countDownLatch = new CountDownLatch(ipList.size());
        for(Map<String, Object> map:ipList){
            executorService.execute(()->{
                try {
                    String ip = (String) map.get("reqIp");
                    String ipInfo = OkHttpUtil.get(IP_URL, ImmutableMap.of("ip", ip));
                    if (!Strings.isNullOrEmpty(ipInfo)) {
                        JSONObject ipobj = JSON.parseObject(ipInfo);
                        if (ipobj.getString("status").equals("1")) {
                            String province = ipobj.getString("province");
                            String city = ipobj.getString("city");
                            if(StringUtils.isNotBlank(province)&&StringUtils.isNotBlank(city)){
                                IpReqVO ipVO=new IpReqVO();
                                ipVO.setCity(city);
                                ipVO.setProvince(province);
                                ipVO.setReqIp(ip);
                                String dt= TimeUtils.getYestDayString();
                                ipVO.setDt(dt);
                                list.add(ipVO);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("计数器减数异常："+e.getMessage(),e);
                }finally {
                    countDownLatch.countDown();
                }
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("计数器异常"+e.getMessage(),e);
        }
        return list;
    }
}
