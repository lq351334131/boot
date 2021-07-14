package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.DataMapper;
import com.etocrm.sdk.entity.data.*;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.total.ToTalPO;
import com.etocrm.sdk.mysqldao.MysqlMapper;
import com.etocrm.sdk.service.DataService;
import com.etocrm.sdk.service.TotalService;
import com.etocrm.sdk.util.DecimalFormatUtils;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.TimeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private OvalUtils ovalUtils;

    @Autowired
    private MysqlMapper mysqlMapper;

    @Autowired
    private TotalService totalService;

    /**
     *
     * @Description :用户数据概览-头部
     * @author xing.liu
     * @date 2020/11/4
     **/
    @Override
    public Result getUserBindDataTotal(UserDataVO userDataVO) {
        Result total = getTotal(userDataVO);
        return total;
        /*if(ovalUtils.validatorRequestParam(userDataVO).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        if(userDataVO.getType1()!=null){
            if(userDataVO.getType1()==1||userDataVO.getType1()==2||userDataVO.getType1()==3||userDataVO.getType1()==4) {
                Result total = getTotal(userDataVO);
                return total;
            }
        }*/
//        Integer newUser = dataMapper.getNewUser(userDataVO);
//        newUser=newUser==null?0:newUser;
//        TotalVO newUserNum = new TotalVO(1,"新用户数",newUser+"","-1");
//        Map<String,Integer> accNum = dataMapper.getVisNum(userDataVO);
//        TotalVO totalVO = new TotalVO(2, "访问次数", accNum.get("visNum") + "", "-1");
//        TotalVO accUserNum = new TotalVO(3,"访问人数",accNum.get("accUserNum")+"","-1");
//        Integer open = dataMapper.getOpen(userDataVO);
//        TotalVO openVO = new TotalVO(4,"打开次数",open+"","-1");
//        Integer stopTime = dataMapper.getStopTime(userDataVO);
//        String stopTimestr = open==0?"00:00:00": TimeUtils.getGapTime(stopTime/ open);
//        TotalVO stopBean = new TotalVO(5,"次均停留时长",stopTimestr,"-1");
//        // TotalVO stopBean = new TotalVO(5,"次均停留时长","00:00:00","-1");
//        Integer newmember = dataMapper.getNewmember(userDataVO);
//        newmember=newmember==null?0:newmember;
        //TotalVO newsReg = new TotalVO(6,"新增会员数",newmember+"","-1");
        //userDataVO.setEndDate("");
        // userDataVO.setBeginDate("");
        //Integer memberall= dataMapper.getNewmember(userDataVO);
        //memberall=memberall==null?0:memberall;
        //TotalVO olderReg = new TotalVO(7,"累计会员数",memberall+"","-1");
        //  // 绑定率 = 新增会员数 / 新用户数
        //String percent = DecimalFormatUtils.getPercent(newmember, newUser);
        //TotalVO bind = new TotalVO(8,"绑定率 ",percent,"-1");
        //List<TotalVO> list = Lists.newArrayList(newUserNum,totalVO,accUserNum,openVO,stopBean,newsReg,olderReg,bind);
       // List<TotalVO> list = Lists.newArrayList(newUserNum,totalVO,accUserNum,openVO,stopBean);
       // return Result.success(list);
    }

    @Override
    public Result getUserBindDataList(UserDataVO userDataVO) {
        if(ovalUtils.validatorRequestParam(userDataVO).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        UserDataRepVO userDataRepVO=new UserDataRepVO();
        List<Map<String, Object>> dataList = mysqlMapper.getDataList(userDataVO);
        if(dataList.size()>0) {
            userDataRepVO.setItem1(getTotal("newUser", dataList));
            userDataRepVO.setItem2(getTotal("pv", dataList));
            userDataRepVO.setItem3(getTotal("uv", dataList));
            userDataRepVO.setItem4(getTotal("openNum", dataList));
            //userDataRepVO.setItem5(getTotal("newmember", dataList));
            return Result.success(userDataRepVO);
        }else{
            userDataRepVO.setItem1(getDataType(1,userDataVO));
            userDataRepVO.setItem2(getDataType(2,userDataVO));
            userDataRepVO.setItem3(getDataType(3,userDataVO));
            userDataRepVO.setItem4(getDataType(4,userDataVO));
            //新增会员数
            //userDataRepVO.setItem5(getDataType(5,userDataVO));
            //净增会员数 = 新增会员数 - 解绑会员数(未完成)张锋哪里不知道
            totalService.insertApiTotal(userDataVO.getBeginDate(),userDataVO.getEndDate());
            return Result.success(userDataRepVO);
        }
       /* if(userDataVO.getType1()!=null){
            Integer type = userDataVO.getType1();
            String dt = TimeUtils.getYestDayString();
            userDataVO.setEndDate(dt);
            if (type == 1) {
                userDataVO.setBeginDate(dt);
            } else if (type == 2) {
                String seven = TimeUtils.reduceDayone(dt, -7);
                userDataVO.setBeginDate(seven);
            } else if (type == 3) {
                String seven = TimeUtils.reduceDayone(dt, -15);
                userDataVO.setBeginDate(seven);
            } else if (type == 4) {
                userDataVO.setBeginDate(TimeUtils.getReduceMonth(dt));
            }

        }

        userDataRepVO.setItem1(getDataType(1,userDataVO));
        userDataRepVO.setItem2(getDataType(2,userDataVO));
        userDataRepVO.setItem3(getDataType(3,userDataVO));
        userDataRepVO.setItem4(getDataType(4,userDataVO));
        //新增会员数
        //userDataRepVO.setItem5(getDataType(5,userDataVO));
        //净增会员数 = 新增会员数 - 解绑会员数(未完成)张锋哪里不知道

        return Result.success(userDataRepVO);*/
    }

    @Override
    public Result getUserVisitData() {
        String  day=TimeUtils.getYestDayString();
        UserDataVO userDataVO=new UserDataVO();
        userDataVO.setEndDate(day);
        List<YestDayRepVO> dataList = mysqlMapper.getYestDataList(userDataVO);
        if(dataList.size()>0) {
            for (YestDayRepVO yestDayRepVO : dataList) {
                Integer openNum = yestDayRepVO.getOpenNum();
                String percent = DecimalFormatUtils.getPercent(Integer.valueOf(yestDayRepVO.getExitRate()), openNum);
                String time = openNum == 0 ? "00:00:00" : TimeUtils.getGapTime(Integer.valueOf(yestDayRepVO.getAvgStopTime()) / openNum);
                yestDayRepVO.setAvgStopTime(time);
                yestDayRepVO.setExitRate(percent);
            }
            return Result.success(dataList);
        }

        List<YestDayRepVO> list=new ArrayList<>();
        List<Map<String, Object>> keyAll = dataMapper.getKeyAll(day);
        if(CollectionUtils.isEmpty(keyAll)){
            return  Result.success(list);
        }
        Map<String,Object> mapparam=new HashMap<>();
        mapparam.put("yestDay",day);
        mapparam.put("type",1);
        List<Map<String, Object>> newyestDay = dataMapper.getYestDay(mapparam);
        mapparam.put("type",2);
        List<Map<String, Object>> useryestDay = dataMapper.getYestDay(mapparam);
        mapparam.put("type",3);
        List<Map<String, Object>> openyestDay = dataMapper.getYestDay(mapparam);
        //跳出率
        mapparam.put("type",4);
        List<Map<String, Object>> exiyestDay = dataMapper.getYestDay(mapparam);

        Map<String, Integer> totalMap = newyestDay.stream().collect(
                Collectors.toMap(x -> x.get("k").toString(),
                        x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> ototalMap = openyestDay.stream().collect(
                Collectors.toMap(x -> x.get("k").toString(),
                        x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> etotalMap = exiyestDay.stream().collect(
                Collectors.toMap(x -> x.get("k").toString(),
                        x -> Integer.valueOf(x.get("num").toString())));
        Map<String, String> netotalMap = useryestDay.stream().collect(
                Collectors.toMap(x -> x.get("k").toString(),
                        x -> String.valueOf(x.get("num").toString()+"-"+x.get("num1").toString())));
        //访问人数、访问次数
        YestDayRepVO y=null;
        for(Map<String, Object> map:keyAll){
            y=new YestDayRepVO();
            String key=(String)map.get("k");
            y.setAppKey(key);
            Integer newUserNUm = totalMap.get(key);
            newUserNUm=newUserNUm==null?0:newUserNUm;
            y.setNewUserNum(newUserNUm);
            Integer oNUm = ototalMap.get(key);
            oNUm=oNUm==null?0:oNUm;
            y.setOpenNum(oNUm);
            Integer eNum = etotalMap.get(key);
            y.setExitRate("0.00");
            if(eNum!=null){
                //跳出率等于=跳出/打开
                String percent = DecimalFormatUtils.getPercent(eNum, oNUm);
                y.setExitRate(percent);
            }
            String s = netotalMap.get(key);
            if(!StringUtils.isEmpty(s)){
                String[] split = s.split("-");
                String vNum = split[0];
                String uNum=split[1];
                y.setUserNum(Integer.valueOf(uNum));
                y.setVisitNum(Integer.valueOf(vNum));
            }else{
                y.setUserNum(0);
                y.setVisitNum(0);
            }
            y.setAvgStopTime(getYestDay(key,y));
            list.add(y);
        }
        totalService.insertApiTotal(day,day);
        return Result.success(list);
    }

    @Override
    public String getStopTime(DataBroadVO dataBroadVO){
        String time="00:00:00";
        try {
            UserDataVO userDataVO = new UserDataVO();
            BeanUtils.copyProperties(dataBroadVO, userDataVO);
            DataListVO dataListVO = new DataListVO();
            dataListVO.setAppKey(dataBroadVO.getAppKey());
            List<DataListVO> dataList = new ArrayList<>();
            userDataVO.setAppKeys(dataList);
            dataList.add(dataListVO);
            userDataVO.setAppKeys(dataList);
            Integer stopTime = dataMapper.getStopTime(userDataVO);
            Integer open = dataMapper.getOpen(userDataVO);
            time = open == 0 ? "00:00:00" : TimeUtils.getGapTime(stopTime/open);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return time;
    }

    private  List<SubItemsRepVO> getDataType(Integer type, UserDataVO userDataVO){
        UserDataTypeVO typeVO=new UserDataTypeVO();
        BeanUtils.copyProperties(userDataVO,typeVO);
        typeVO.setType(type);
        List<Map<String, Object>> dataList = dataMapper.getDataList(typeVO);
        Map<String,List<DataRepVO>> map= Maps.newLinkedHashMap();
        for(Map<String, Object> dataMap:dataList){
            String dt= (String) dataMap.get("dt");
            //String dt = TimeUtils.getDateToString(dtDate);
            //dataMap.get("num")返回java.math.BigInteger,
            BigInteger bigInteger=(BigInteger)dataMap.get("num");
            Long num=bigInteger.longValue();
            String k = (String)dataMap.get("k");
            List<DataRepVO> maps = map.get(k);
            if(CollectionUtils.isEmpty(maps)){
                maps= Lists.newArrayList();
                DataRepVO dataRepVO=new DataRepVO();
                dataRepVO.setDate(dt);
                dataRepVO.setValue(num);
                maps.add(dataRepVO);
                map.put(k,maps);
            }else{
                DataRepVO dataRepVO=new DataRepVO();
                dataRepVO.setDate(dt);
                dataRepVO.setValue(num);
                maps.add(dataRepVO);
            }
        }
        List<SubItemsRepVO> result=new ArrayList<>();
        map.forEach((k,v)->{
            SubItemsRepVO subItemsRepVO=new SubItemsRepVO();
            subItemsRepVO.setAppKey(k);
            subItemsRepVO.setSubItems(v);
            result.add(subItemsRepVO);

        });

        return  result;
    }

    private String getYestDay(String key, YestDayRepVO yestDayRepVO){
        DataBroadVO dataBroadVO=new DataBroadVO();
        dataBroadVO.setAppKey(key);
        String yestDay = TimeUtils.getYestDayString();
        dataBroadVO.setEndDate(yestDay);
        dataBroadVO.setBeginDate(yestDay);
        UserDataVO userDataVO = getUserDataVO(dataBroadVO);
        Integer stopTime = dataMapper.getStopTime(userDataVO);
        Integer openNum = yestDayRepVO.getOpenNum();
        String  stopTimestr=openNum==0?"00:00:00":TimeUtils.getGapTime(stopTime/openNum);
        return stopTimestr;
    }

    @Override
    public String getOpenStopTime(DataBroadVO dataBroadVO, Integer open){
        String time="00:00:00";
        try {
            UserDataVO userDataVO = getUserDataVO(dataBroadVO);
            Integer stopTime = dataMapper.getStopTime(userDataVO);
            time = open == 0 ? "00:00:00" : TimeUtils.getGapTime(stopTime/open);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return time;
    }

    public UserDataVO getUserDataVO(DataBroadVO dataBroadVO){
        UserDataVO userDataVO = new UserDataVO();
        BeanUtils.copyProperties(dataBroadVO, userDataVO);
        DataListVO dataListVO = new DataListVO();
        dataListVO.setAppKey(dataBroadVO.getAppKey());
        List<DataListVO> dataList = new ArrayList<>();
        userDataVO.setAppKeys(dataList);
        dataList.add(dataListVO);
        userDataVO.setAppKeys(dataList);
        return  userDataVO;
    }

    private List<SubItemsRepVO> getTotal(String column, List<Map<String, Object>> dataList ){
        Map<String,List<DataRepVO>> map1= Maps.newLinkedHashMap();
        for(Map<String, Object> dataMap:dataList){
            String dt1= (String) dataMap.get("dt");
            BigDecimal bigInteger=(BigDecimal)dataMap.get(column);
            Long num=bigInteger.longValue();
            String k = (String)dataMap.get("appkey");
            List<DataRepVO> maps = map1.get(k);
            if(CollectionUtils.isEmpty(maps)){
                maps= Lists.newArrayList();
                DataRepVO dataRepVO=new DataRepVO();
                dataRepVO.setDate(dt1);
                dataRepVO.setValue(num);
                maps.add(dataRepVO);
                map1.put(k,maps);
            }else{
                DataRepVO dataRepVO=new DataRepVO();
                dataRepVO.setDate(dt1);
                dataRepVO.setValue(num);
                maps.add(dataRepVO);
            }
        }
        List<SubItemsRepVO> result=new ArrayList<>();
        map1.forEach((k,v)->{
            SubItemsRepVO subItemsRepVO=new SubItemsRepVO();
            subItemsRepVO.setAppKey(k);
            subItemsRepVO.setSubItems(v);
            result.add(subItemsRepVO);

        });
        return result;
    }

    private Result  getTotal(UserDataVO userDataVO) {
        ToTalPO toTalPO = mysqlMapper.getBindDataList(userDataVO);
        if (toTalPO != null) {
            TotalVO newUserNum = new TotalVO(1, "新用户数", toTalPO.getNewUser() + "", "-1");
            TotalVO totalVO = new TotalVO(2, "访问次数", toTalPO.getPv() + "", "-1");
            Map<String,Integer> accNum = dataMapper.getVisNum(userDataVO);
            TotalVO accUserNum = new TotalVO(3, "访问人数", accNum.get("accUserNum") + "", "-1");
            TotalVO openVO = new TotalVO(4, "打开次数", toTalPO.getOpenNum() + "", "-1");
            String stopTimestr = toTalPO.getOpenNum() == 0 ? "00:00:00" : TimeUtils.getGapTime(toTalPO.getAvgStopTime() / toTalPO.getOpenNum());
            TotalVO stopBean = new TotalVO(5, "次均停留时长", stopTimestr, "-1");
            TotalVO newsReg = new TotalVO(6,"新增会员数",toTalPO.getNewmember()+"","-1");
            ToTalPO toTalPO2 = mysqlMapper.getNewMember(userDataVO);
            TotalVO olderReg = new TotalVO(7,"累计会员数",toTalPO2.getNewmember()+"","-1");
            //  // 绑定率 = 新增会员数 / 新用户数
            String percent = DecimalFormatUtils.getPercent(toTalPO.getNewmember(), toTalPO.getNewUser());
            TotalVO bind = new TotalVO(8,"绑定率 ",percent,"-1");
            List<TotalVO> list = Lists.newArrayList(newUserNum, totalVO, accUserNum, openVO, stopBean);
            //List<TotalVO> list = Lists.newArrayList(newUserNum,totalVO,accUserNum,openVO,stopBean,newsReg,olderReg,bind);
            return Result.success(list);
        }else{
            Integer newUser = dataMapper.getNewUser(userDataVO);
            newUser=newUser==null?0:newUser;
            TotalVO newUserNum = new TotalVO(1,"新用户数",newUser+"","-1");
            Map<String,Integer> accNum = dataMapper.getVisNum(userDataVO);
            TotalVO totalVO = new TotalVO(2, "访问次数", accNum.get("visNum") + "", "-1");
            TotalVO accUserNum = new TotalVO(3,"访问人数",accNum.get("accUserNum")+"","-1");
            Integer open = dataMapper.getOpen(userDataVO);
            TotalVO openVO = new TotalVO(4,"打开次数",open+"","-1");
            Integer stopTime = dataMapper.getStopTime(userDataVO);
            String stopTimestr = open==0?"00:00:00": TimeUtils.getGapTime(stopTime/ open);
            TotalVO stopBean = new TotalVO(5,"次均停留时长",stopTimestr,"-1");
        // TotalVO stopBean = new TotalVO(5,"次均停留时长","00:00:00","-1");
        //Integer newmember = dataMapper.getNewmember(userDataVO);
        //newmember=newmember==null?0:newmember;
            //TotalVO newsReg = new TotalVO(6,"新增会员数",newmember+"","-1");
            //userDataVO.setEndDate("");
            // userDataVO.setBeginDate("");
            //Integer memberall= dataMapper.getNewmember(userDataVO);
            //memberall=memberall==null?0:memberall;
            //TotalVO olderReg = new TotalVO(7,"累计会员数",memberall+"","-1");
            //  // 绑定率 = 新增会员数 / 新用户数
            //String percent = DecimalFormatUtils.getPercent(newmember, newUser);
            //TotalVO bind = new TotalVO(8,"绑定率 ",percent,"-1");
            //List<TotalVO> list = Lists.newArrayList(newUserNum,totalVO,accUserNum,openVO,stopBean,newsReg,olderReg,bind);
             List<TotalVO> list = Lists.newArrayList(newUserNum,totalVO,accUserNum,openVO,stopBean);
             totalService.insertApiTotal(userDataVO.getBeginDate(),userDataVO.getEndDate());
            return Result.success(list);
        }
    }


}
