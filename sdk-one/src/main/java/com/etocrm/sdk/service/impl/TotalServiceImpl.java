package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.dao.TotalMapper;
import com.etocrm.sdk.entity.total.ToTalPO;
import com.etocrm.sdk.mysqldao.MysqlMapper;
import com.etocrm.sdk.service.TotalService;
import com.etocrm.sdk.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TotalServiceImpl implements TotalService {

    @Autowired
    private TotalMapper totalMapper;

    @Autowired
    private MysqlMapper mysqlMapper;

    @Override
    public void insertTotal() {
        List<ToTalPO> toTalPO=getToTalPOList();
        //List<ToTalPO> toTalPO=getTotalPo();
        if(toTalPO.size()>0){
            mysqlMapper.insertTotal(toTalPO);
            log.info("----数据指标统计写入mysql-----");
        }

    }

    private List<ToTalPO> getToTalPOList() {
        try {
            String dt =TimeUtils.getYestDayString();//"2021-02-19" ;//
            Map<String, Object> map = new HashMap<>();
            map.put("dt", dt);
            List<ToTalPO> list= totalMapper.getTotalAll(map);
            if (CollectionUtils.isEmpty(list)) return list;
            //新用户值
            // Map<String, Integer> newUser = getNum(totalList2, "newUser");
            List<Map<String, Object>> totalList3 = totalMapper.getBounceRate(map);
            //跳出页
            Map<String, Integer> bounceNum = getNum(totalList3, "bounceNum");

            List<Map<String, Object>> totalList4 = totalMapper.getNewUser(map);
            Map<String, Integer> newUserMap = getNum(totalList4, "newUser");

            //日活跃
            List<Map<String, Object>> dauList = getActive(dt, TimeUtils.reduceDayone(dt, -1));
            Map<String, Integer> dauMap = getNum(dauList, "num");
            List<Map<String, Object>> wduList = getActive(dt, TimeUtils.reduceDayone(dt, -7));
            Map<String, Integer> wduMap = getNum(wduList, "num");
            List<Map<String, Object>> mduList = getActive(dt, TimeUtils.getReduceMonth(dt));
            Map<String, Integer> mauMap = getNum(mduList, "num");
            for (ToTalPO totalEntity : list) {
                String appKey =  totalEntity.getAppkey();
                totalEntity.setDt(dt);
                Integer newUserNum = newUserMap.get(appKey) == null ? 0 : newUserMap.get(appKey);
                totalEntity.setNewUser(newUserNum);
                int open = totalEntity.getOpenNum();
                Integer boundPage = bounceNum.get(appKey);
                totalEntity.setBounce(boundPage == null ? 0 : boundPage);
                map.put("appKey", appKey);
                Long time = 0L;
                if (open > 0) {
                    time = totalMapper.getTime(map);
                }
                totalEntity.setAvgStopTime(time);
                totalEntity.setDau(dauMap.get(appKey) == null ? 0 : dauMap.get(appKey));
                totalEntity.setWau(wduMap.get(appKey) == null ? 0 : wduMap.get(appKey));
                totalEntity.setMau(mauMap.get(appKey) == null ? 0 : mauMap.get(appKey));
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }


    private List<ToTalPO> getTotalPo(){
        List<ToTalPO> list = new ArrayList<>();
        try {
            ToTalPO totalPo = null;
            String dt = TimeUtils.getYestDayString();
            Map<String, Object> map = new HashMap<>();
            map.put("dt", dt);
            List<Map<String, Object>> totalList = totalMapper.getTotal(map);
            if(CollectionUtils.isEmpty(totalList))return list;
            List<Map<String, Object>> totalList2 = totalMapper.getOpenNum(map);
            //新用户值
           // Map<String, Integer> newUser = getNum(totalList2, "newUser");

            //打开参数
            Map<String, Integer> openNum = getNum(totalList2, "openNum");
            //退出页
            Map<String, Integer> exitpage = getNum(totalList2, "exitpage");

            List<Map<String, Object>> totalList3 = totalMapper.getBounceRate(map);
            //跳出页
            Map<String, Integer> bounceNum = getNum(totalList3, "bounceNum");

            List<Map<String, Object>> totalList4 = totalMapper.getNewUser(map);
            Map<String, Integer> newUserMap = getNum(totalList4, "newUser");

            //分享人数、分享次数
            List<Map<String, Object>> totalList5 = totalMapper.getUserShare(map);
            Map<String, Integer> shareNumMap = getNum(totalList5, "shareNum");
            Map<String, Integer> sharePeopMap = getNum(totalList5, "shareUser");
            //日活跃
            List<Map<String, Object>> dauList = getActive(dt,TimeUtils.reduceDayone(dt,-1));
            Map<String, Integer> dauMap = getNum(dauList, "num");
            List<Map<String, Object>> wduList = getActive(dt,TimeUtils.reduceDayone(dt,-7));
            Map<String, Integer> wduMap = getNum(wduList, "num");
            List<Map<String, Object>> mduList = getActive(dt,TimeUtils.getReduceMonth(dt));
            Map<String, Integer> mauMap = getNum(mduList, "num");
            List<Map<String, Object>> totalList6 = totalMapper.getNewmember(map);
            Map<String, Integer> newmemberMap = getNum(totalList6, "num");
            for (Map<String, Object> totalEntity : totalList) {
                totalPo = new ToTalPO();
                String appKey = (String) totalEntity.get("appkey");
                BigInteger pv = (BigInteger) totalEntity.get("pv");
                BigInteger uv = (BigInteger) totalEntity.get("uv");
                totalPo.setAppkey(appKey);
                totalPo.setDt(dt);
                totalPo.setPv(pv.intValue());
                totalPo.setUv(uv.intValue());
                Integer newUserNum = newUserMap.get(appKey)==null?0:newUserMap.get(appKey);
                totalPo.setNewUser(newUserNum);
                int open = openNum.get(appKey) == null ? 0 : openNum.get(appKey);
                totalPo.setOpenNum(open);
                Integer boundPage = bounceNum.get(appKey);
                totalPo.setBounce(boundPage==null?0:boundPage);
                int exit = exitpage.get(appKey) == null ? 0 : exitpage.get(appKey);
                totalPo.setExit(exit);
                map.put("appKey",appKey);
                Long time = 0L;
                if(open>0){
                    time=totalMapper.getTime(map);
                }
                totalPo.setAvgStopTime(time);
                Integer shareNum = shareNumMap.get(appKey)==null?0:shareNumMap.get(appKey);
                totalPo.setShareNum(shareNum);
                Integer sharePeop = sharePeopMap.get(appKey)==null?0:sharePeopMap.get(appKey);
                totalPo.setShareUser(sharePeop);
                totalPo.setDau(dauMap.get(appKey)==null?0:dauMap.get(appKey));
                totalPo.setWau(wduMap.get(appKey)==null?0:wduMap.get(appKey));
                totalPo.setMau(mauMap.get(appKey)==null?0:mauMap.get(appKey));
                totalPo.setNewmember(newmemberMap.get(appKey)==null?0:newmemberMap.get(appKey));
                list.add(totalPo);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return  list;
    }

    public  Map<String,Integer> getNum( List<Map<String, Object>> list,String key ){
        Map<String, Integer> totalMap = list.stream().collect(
                Collectors.toMap(x -> x.get("appkey").toString(),
                        x -> Integer.valueOf(x.get(key).toString())));
        return totalMap;
    }
    private  List<Map<String, Object>>  getActive(String dt,String beginDt){
        Map<String, Object> map = new HashMap<>();
        map.put("dt", dt);
        map.put("begDt",beginDt);
        List<Map<String, Object>> active = totalMapper.getActive(map);
        return active;
    }

    private List<ToTalPO> getToTalPOList(String beginDate, String endDate) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            List<ToTalPO> list= totalMapper.getTotalDateAll(map);
            if (CollectionUtils.isEmpty(list)) return list;
            //新用户值
            // Map<String, Integer> newUser = getNum(totalList2, "newUser");
            List<Map<String, Object>> totalList3 = totalMapper.getBounceDateRate(map);
            //跳出页
            Map<String, Integer> bounceNum = getDtNum(totalList3, "bounceNum");

            List<Map<String, Object>> totalList4 = totalMapper.getNewUserDate(map);
            Map<String, Integer> newUserMap = getDtNum(totalList4, "newUser");

            //日活跃
           /* List<Map<String, Object>> dauList = getActive(dt, TimeUtils.reduceDayone(dt, -1));
            Map<String, Integer> dauMap = getNum(dauList, "num");
            List<Map<String, Object>> wduList = getActive(dt, TimeUtils.reduceDayone(dt, -7));
            Map<String, Integer> wduMap = getNum(wduList, "num");
            List<Map<String, Object>> mduList = getActive(dt, TimeUtils.getReduceMonth(dt));
            Map<String, Integer> mauMap = getNum(mduList, "num");*/
            for (ToTalPO totalEntity : list) {
                String appKey =  totalEntity.getAppkey();
                String dt = totalEntity.getDt();
                totalEntity.setDt(dt);
                String  key=appKey+"_"+dt;
                Integer newUserNum = newUserMap.get(key) == null ? 0 : newUserMap.get(key);
                totalEntity.setNewUser(newUserNum);
                int open = totalEntity.getOpenNum();
                Integer boundPage = bounceNum.get(key);
                totalEntity.setBounce(boundPage == null ? 0 : boundPage);
                map.put("appKey", appKey);
                Long time = 0L;
                if (open > 0) {
                    time = totalMapper.getTime(map);
                }
                totalEntity.setAvgStopTime(time);
                map.put("dt",dt);
                map.put("begDt",dt);
                Integer day = totalMapper.getDateActive(map);
                totalEntity.setDau(day);
                map.put("begDt",TimeUtils.reduceDayone(dt, -7));
                Integer wau = totalMapper.getDateActive(map);
                totalEntity.setWau(wau);
                map.put("begDt",TimeUtils.getReduceMonth(dt));
                Integer mau = totalMapper.getDateActive(map);
                totalEntity.setMau(mau);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }

    public  Map<String,Integer> getDtNum( List<Map<String, Object>> list,String key ){
        Map<String, Integer> totalMap = list.stream().collect(
                Collectors.toMap(x -> x.get("appkey_dt").toString(),
                        x -> Integer.valueOf(x.get(key).toString())));
        return totalMap;
    }

    @Override
    public void insertApiTotal(String beginDate,String endDate) {
        List<ToTalPO> toTalPO=getToTalPOList(beginDate,endDate);
        if(toTalPO.size()>0){
            mysqlMapper.insertTotal(toTalPO);
            log.info("----api数据写入-----");
        }

    }



}
