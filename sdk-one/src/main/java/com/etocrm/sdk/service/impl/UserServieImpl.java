package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.DataBroadMapper;
import com.etocrm.sdk.dao.UserMapper;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.databroad.PortalDetailDatasRepVO;
import com.etocrm.sdk.entity.share.SharePageRepVO;
import com.etocrm.sdk.entity.user.*;
import com.etocrm.sdk.mysqldao.MysqlMapper;
import com.etocrm.sdk.mysqldao.UserDataMapper;
import com.etocrm.sdk.service.DataBroadService;
import com.etocrm.sdk.service.TotalService;
import com.etocrm.sdk.service.UserSerice;
import com.etocrm.sdk.util.DecimalFormatUtils;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import com.etocrm.sdk.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServieImpl implements UserSerice {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OvalUtils ovalUtils;
    

    @Autowired
    private DataBroadMapper dataBroadMapper;


    @Autowired
    private DataBroadService dataBroadService;

    @Autowired
    private MysqlMapper mysqlMapper;


    @Autowired
    private UserDataMapper userDataMapper;

    @Autowired
    private TotalService totalService;


    @Override
    public Result getUserRegionData(UserVO userVO, String groupName) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<UserRegionRepVO> result = new ArrayList<>();
        try {
            UserTypeVO vo = new UserTypeVO();
            BeanUtils.copyProperties(userVO, vo);
            vo.setGroupName(groupName);
            getReginList(groupName, vo, result);
            return Result.success(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
    }

    @Override
    public List<UserRegionRepVO> getUserExcelData(UserVO userVO, String groupName) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Collections.emptyList();
        List<UserRegionRepVO> result = new ArrayList<>();
        try {
            UserTypeVO vo = new UserTypeVO();
            BeanUtils.copyProperties(userVO, vo);
            vo.setGroupName(groupName);
            getReginList(groupName, vo, result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }

    }

    @Override
    public Result getCity(UserVO userVO, String city) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<CityVO> result = new ArrayList<>();
        try {
            UserTypeVO vo = new UserTypeVO();
            BeanUtils.copyProperties(userVO, vo);
            vo.setGroupName(city);
            getCityList(city, vo, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(result);
    }

    @Override
    public List<CityVO> getCityList(UserVO userVO, String city) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Collections.emptyList();
        List<CityVO> result = new ArrayList<>();
        try {
            UserTypeVO vo = new UserTypeVO();
            BeanUtils.copyProperties(userVO, vo);
            vo.setGroupName(city);
            getCityList(city, vo, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
        return result;
    }

    @Override
    public Result getUserPhoneBrand(UserVO userVO, String brand) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<BrankVO> result = new ArrayList<>();
        try {
            UserTypeVO vo = new UserTypeVO();
            BeanUtils.copyProperties(userVO, vo);
            vo.setGroupName(brand);
            getBrandList(brand, vo, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(result);
    }

    @Override
    public List<BrankVO> getBrandListExcel(UserVO userVO, String brand) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Collections.emptyList();
        List<BrankVO> result = new ArrayList<>();
        try {
            UserTypeVO vo = new UserTypeVO();
            BeanUtils.copyProperties(userVO, vo);
            vo.setGroupName(brand);
            getBrandList(brand, vo, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
        return result;
    }

    @Override
    public Result getSystemList(UserVO userVO, String platform) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<SystemVO> result = new ArrayList<>();
        try {
            UserTypeVO userTypeVO = new UserTypeVO();
            BeanUtils.copyProperties(userVO, userTypeVO);
            userTypeVO.setGroupName(platform);
            getPlatform(platform, userTypeVO, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.PARAMETERS_NULL);
        }
        return Result.success(result);
    }

    @Override
    public List<SystemVO> getSystemListExcel(UserVO userVO, String platform) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Collections.emptyList();
        List<SystemVO> result = new ArrayList<>();
        try {
            UserTypeVO userTypeVO = new UserTypeVO();
            BeanUtils.copyProperties(userVO, userTypeVO);
            userTypeVO.setGroupName(platform);
            getPlatform(platform, userTypeVO, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();

        }
        return result;
    }

    @Override
    public Result getUser(UserQueryVO userQueryVO) {
        if (ovalUtils.validatorRequestParam(userQueryVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        try {
            PageUtils<UserDatailVO> province = getUserDetails("province", userQueryVO);
            return Result.success(province);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);

    }

    @Override
    public Result getUserByBrand(UserQueryVO userQueryVO) {
        if (ovalUtils.validatorRequestParam(userQueryVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        try {
            PageUtils<UserDatailVO> province = getUserDetails("brand", userQueryVO);
            return Result.success(province);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
    }

    @Override
    public Result getUserByCity(UserQueryVO userQueryVO) {
        if (ovalUtils.validatorRequestParam(userQueryVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        try {
            PageUtils<UserDatailVO> province = getUserDetails("city", userQueryVO);
            return Result.success(province);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
    }

    @Override
    public Result getUserStatisticsSummary(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        UserSummaryVO totalDataRepVO = userDataMapper.getUserStatisticsSummary(dataBroadVO);
        if(totalDataRepVO!=null) {
            Integer visNum = dataBroadMapper.getVisUserNum(dataBroadVO);
            visNum = visNum == null ? 0 : visNum;
            totalDataRepVO.setTotalVisitors(visNum);
            String stopTimestr = visNum == 0 ? "00:00:00" : TimeUtils.getGapTime(totalDataRepVO.getAvgStopTime() / visNum);
            totalDataRepVO.setAvgStayTimes(stopTimestr);
        }
        return Result.success(totalDataRepVO);
    }

    @Override
    public Result getUserStatisticsDateDate(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        try {
            List<UserDateRepVO> dataList = getDataList(dataBroadVO);
            return Result.success(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
    }

    @Override
    public List<UserDateRepVO> dowloadGetUserStatisticsDateDate(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Collections.emptyList();
        try {
            List<UserDateRepVO> dataList = getDataList(dataBroadVO);
            return dataList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public Result getUserBySystem(UserQueryVO userQueryVO) {
        if (ovalUtils.validatorRequestParam(userQueryVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        PageUtils<UserDatailVO> province = getUserDetails("platform", userQueryVO);
        return Result.success(province);
    }

    @Override
    public Result getUserActiveStatisticsChart(UserPageVO userPageVO) {
        if (ovalUtils.validatorRequestParam(userPageVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        try{
            PageUtils<UserDatailVO> activeListPage = getActiveListPage(userPageVO);
            return Result.success(activeListPage);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
         }
    }

    @Override
    public Result getUserActiveStatisticsTable(UserVO userVO) {
        try{
            UserPageVO userPageVO=new UserPageVO();
            BeanUtils.copyProperties(userVO,userPageVO);
            List<UserActiveRepVO> userActive = getActiveList(userPageVO);
            return Result.success(userActive);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
    }

    @Override
    public List<UserActiveRepVO> getUserActiveexcel(UserVO userVO) {
        if (ovalUtils.validatorRequestParam(userVO).size() > 0) return Collections.emptyList();
        try{
            UserPageVO userPageVO=new UserPageVO();
            BeanUtils.copyProperties(userVO,userPageVO);
            List<UserActiveRepVO> dateList = getActiveList(userPageVO);
             return  dateList;
            //return getUserActive(userVO);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Collections.emptyList();
        }
    }

    @Override
    public Result getUserStatisticsGroupByDateSummary(UserPageVO userPageVO) {
        if (ovalUtils.validatorRequestParam(userPageVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer count = mysqlMapper.getUserStaticDataCount(userPageVO);
        PageUtils<SharePageRepVO> pageUtils=new PageUtils<>(userPageVO.getPageSize(),userPageVO.getPageIndex(),count);
/*
        if(count==null||count==0){
            pageUtils.setLists(Collections.emptyList());
           return Result.success(pageUtils);
        }*/
        List<Map<String,Object>>  list=mysqlMapper.getUserStaticData(userPageVO);
        List<UserStaticTotalData> result=new  ArrayList();
        UserStaticTotalData userStaticTotalData=null;
        for(Map<String,Object> map:list){
            userStaticTotalData=new UserStaticTotalData();
            String dt=map.get("dt").toString();
            String pv=map.get("pv").toString();
            String uv=map.get("uv").toString();
            Integer open=(Integer) map.get("openNum");
            Long time=Long.valueOf (map.get("avgStopTime").toString());

            Integer bounce=(Integer) map.get("bounce");
            String newUser=map.get("newUser").toString();
            userStaticTotalData.setOpenCountTimesGroupByDate(getUserStaticDate(dt,open+""));
            userStaticTotalData.setTotalCountTimesGroupByDate(getUserStaticDate(dt,pv));
            userStaticTotalData.setTotalVisitorsGroupByDate(getUserStaticDate(dt,uv));
            String time1 = open == 0 ? "00:00:00" : TimeUtils.getGapTime(time/open);
            userStaticTotalData.setAvgStayTimesGroupByDate(getUserStaticDate(dt,time1));
            String percent = DecimalFormatUtils.getPercent(bounce, open.intValue());
            userStaticTotalData.setBounceRateGroupByDate(getUserStaticDate(dt,percent));
            userStaticTotalData.setNewUsersGroupByDate(getUserStaticDate(dt,newUser));
            result.add(userStaticTotalData);
        }
        if(count==0){
            DataBroadVO dataBroadVO=new DataBroadVO();
            BeanUtils.copyProperties(userPageVO,dataBroadVO);
            count = userMapper.getUserStatisticsGroupByDateCount(dataBroadVO);
            pageUtils=new PageUtils<>(userPageVO.getPageSize(),userPageVO.getPageIndex(),count);
            if(count==null||count==0){
                pageUtils.setLists(Collections.emptyList());
                return Result.success(pageUtils);
            }
            list=userMapper.getUserStatisticsGroupByDateSummary1(userPageVO);
            List<UserStaticDate> newUser = userMapper.getUserStatisticsGroupByDateSummary2(userPageVO);
            Map<String, String> newUserMap = newUser.stream().collect(Collectors.toMap(k -> k.getDateTime(), v -> v.getCount()));
            List<UserStaticDate> time = userMapper.getUserStatisticsGroupByDateStop(userPageVO);
            Map<String, Integer> timeMap = time.stream().collect(Collectors.toMap(k -> k.getDateTime(), v -> Integer.valueOf(v.getCount().toString())));
            List<UserStaticDate> exitList=userMapper.getUserStatisticsGroupByDateTiaochu(userPageVO);
            Map<String, Integer> exitMap = exitList.stream().collect(Collectors.toMap(k -> k.getDateTime(), v -> Integer.valueOf(v.getCount().toString())));
            for(Map<String,Object> map:list){
                userStaticTotalData=new UserStaticTotalData();
                String dateTime = (String) map.get("dateTime");
                BigInteger visitNum = (BigInteger) map.get("visitNum");
                BigInteger userNum = (BigInteger) map.get("userNum");
                BigInteger openNum = (BigInteger) map.get("openNum");
                //访问次数
               userStaticTotalData.setTotalCountTimesGroupByDate(getUserStaticDate(dateTime, visitNum.toString()));
                //人数
                userStaticTotalData.setTotalVisitorsGroupByDate(getUserStaticDate(dateTime, userNum.toString()));
                //打开次数
                userStaticTotalData.setOpenCountTimesGroupByDate(getUserStaticDate(dateTime, openNum.toString()));

                String  newUserstr = newUserMap.get(dateTime) == null ? "0" : newUserMap.get(dateTime);
                userStaticTotalData.setNewUsersGroupByDate(getUserStaticDate(dateTime, newUserstr));
                Integer timeNum = timeMap.get(dateTime);
                String timeString = openNum.intValue() == 0 ? "00:00:00" : TimeUtils.getGapTime(timeNum/openNum.intValue());
                userStaticTotalData.setAvgStayTimesGroupByDate(getUserStaticDate(dateTime, timeString));
                Integer bound = exitMap.get(dateTime)==null?0:exitMap.get(dateTime);
                String percent = DecimalFormatUtils.getPercent(bound, openNum.intValue());
                userStaticTotalData.setBounceRateGroupByDate(getUserStaticDate(dateTime, percent));
                result.add(userStaticTotalData);
            }
            totalService.insertApiTotal(dataBroadVO.getBeginDate(),dataBroadVO.getEndDate());
        }
        pageUtils.setLists(result);
        return Result.success(pageUtils);



    }

    @Override
    public Result getUserDetails(UserDetailsVO userDetailsVO) {
        if (ovalUtils.validatorRequestParam(userDetailsVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        return Result.success(userMapper.getUserDetail(userDetailsVO));
    }

    private void getReginList(String groupName, UserTypeVO userTypeVO, List<UserRegionRepVO> result) {
        try {
            List<Map<String, Object>> userImg = userMapper.getUserImg(userTypeVO);
            List<Map<String, Object>> userImgNewUser = userMapper.getUserImgNewUser(userTypeVO);
            List<Map<String, Object>> visoUser = userMapper.getUserImgVisoUser(userTypeVO);
            List<Map<String, Object>> userImgExit = userMapper.getUserImgExit(userTypeVO);
            List<Map<String, Object>> open = userMapper.getUserImgOpen(userTypeVO);
            UserRegionRepVO userRegionRepVO = null;
            for (Map<String, Object> map : userImg) {
                userRegionRepVO = new UserRegionRepVO();
                String key = (String) map.get(groupName);
                userRegionRepVO.setRegin(key);
                Integer newuserNum = getnewUserNum(key, groupName, userImgNewUser);
                userRegionRepVO.setNewUserNum(newuserNum);
                Map<String, Integer> visorUser = getVisorUser(key, groupName, visoUser);
                userRegionRepVO.setUserNum(visorUser.get("userNum"));
                userRegionRepVO.setVisitNum(visorUser.get("visitNum"));
                Integer open1 = getOpen(key, groupName, open);
                userRegionRepVO.setOpenNum(open1 == null ? 0 : open1);
                Integer exit = getExit(key, groupName, userImgExit);
                String percent = DecimalFormatUtils.getPercent(exit, open1);
                userRegionRepVO.setExitRate(percent + "%");
                userTypeVO.setValue(key);
                String stopTime = getStopTime(userTypeVO, open1);
                userRegionRepVO.setAvgStopTime(stopTime);
                result.add(userRegionRepVO);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private Integer getnewUserNum(String key, String groupName, List<Map<String, Object>> list) {
        log.info(JSONObject.toJSONString(list));
        Integer num = 0;
        Map<String, Integer> newMap = list.stream().collect(
                Collectors.toMap(x -> x.get(groupName).toString(),
                        x -> Integer.valueOf(x.get("num").toString())));
        Integer s = (Integer) newMap.get(key);
        if (!StringUtils.isEmpty(s)) {
            num = s;
        }
        return num;
    }

    private Map<String, Integer> getVisorUser(String key, String groupName, List<Map<String, Object>> list) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("userNum", 0);
        map.put("visitNum", 0);
        Map<String, String> visoUserMap = list.stream().collect(
                Collectors.toMap(x -> x.get(groupName).toString(),
                        x -> String.valueOf(x.get("visNum").toString() + "-" + x.get("useNum").toString())));
        String string = visoUserMap.get(key);
        if (!StringUtils.isEmpty(string)) {
            String[] split = string.split("-");
            Integer visNum = Integer.valueOf(split[0]);
            Integer useNum = Integer.valueOf(split[1]);
            map.put("userNum", useNum);
            map.put("visitNum", visNum);
        }
        return map;
    }

    private Integer getOpen(String key, String groupName, List<Map<String, Object>> list) {
        log.info("getOpen");
        Map<String, Integer> openMap = list.stream().collect(
                Collectors.toMap(x -> x.get(groupName).toString(),
                        x -> Integer.valueOf(x.get("num").toString())));
        Integer num = openMap.get(key);
        if (num == null) {
            num = 0;
        }
        return num;
    }

    private Integer getExit(String key, String groupName, List<Map<String, Object>> list) {
        log.info("getOpen");
        Map<String, Integer> openMap = list.stream().collect(
                Collectors.toMap(x -> x.get(groupName).toString(),
                        x -> Integer.valueOf(x.get("num").toString())));
        Integer num = openMap.get(key);
        Integer exit = 0;
        if (num != null) {
            exit = num;
        }
        return exit;
    }

    private void getCityList(String groupName, UserTypeVO userTypeVO, List<CityVO> result) {
        List<Map<String, Object>> userImg = userMapper.getUserImg(userTypeVO);
        List<Map<String, Object>> userImgNewUser = userMapper.getUserImgNewUser(userTypeVO);
        List<Map<String, Object>> visoUser = userMapper.getUserImgVisoUser(userTypeVO);
        List<Map<String, Object>> userImgExit = userMapper.getUserImgExit(userTypeVO);
        List<Map<String, Object>> open = userMapper.getUserImgOpen(userTypeVO);
        CityVO cityVO = null;
        for (Map<String, Object> map : userImg) {
            cityVO = new CityVO();
            String key = (String) map.get(groupName);
            cityVO.setRegin(key);
            Integer integer = getnewUserNum(key, groupName, userImgNewUser);
            cityVO.setNewUserNum(integer);
            Map<String, Integer> visorUser = getVisorUser(key, groupName, visoUser);
            cityVO.setUserNum(visorUser.get("userNum"));
            cityVO.setVisitNum(visorUser.get("visitNum"));
            Integer open1 = getOpen(key, groupName, open);
            cityVO.setOpenNum(open1);
            Integer exit = getExit(key, groupName, userImgExit);
            Integer openNum = cityVO.getOpenNum();
            String percent = DecimalFormatUtils.getPercent(exit, openNum);
            cityVO.setExitRate(percent);
            userTypeVO.setValue(key);
            String stopTime = getStopTime(userTypeVO, open1);
            cityVO.setAvgStopTime(stopTime);
            result.add(cityVO);
        }
    }

    private void getBrandList(String groupName, UserTypeVO userTypeVO, List<BrankVO> result) {
        List<Map<String, Object>> userImg = userMapper.getUserImg(userTypeVO);
        List<Map<String, Object>> userImgNewUser = userMapper.getUserImgNewUser(userTypeVO);
        List<Map<String, Object>> visoUser = userMapper.getUserImgVisoUser(userTypeVO);
        List<Map<String, Object>> userImgExit = userMapper.getUserImgExit(userTypeVO);
        List<Map<String, Object>> open = userMapper.getUserImgOpen(userTypeVO);

        BrankVO brankVO = null;
        for (Map<String, Object> map : userImg) {
            brankVO = new BrankVO();
            String key = (String) map.get(groupName);
            brankVO.setPhoneBrand(key);
            Integer integer = getnewUserNum(key, groupName, userImgNewUser);
            brankVO.setNewUserNum(integer);
            Map<String, Integer> visorUser = getVisorUser(key, groupName, visoUser);
            brankVO.setUserNum(visorUser.get("userNum"));
            brankVO.setVisitNum(visorUser.get("visitNum"));
            Integer open1 = getOpen(key, groupName, open);
            brankVO.setOpenNum(open1);
            Integer exit = getExit(key, groupName, userImgExit);
            Integer openNum = brankVO.getOpenNum();
            String percent = DecimalFormatUtils.getPercent(exit, openNum);
            brankVO.setExitRate(percent);
            userTypeVO.setValue(key);
            String stopTime = getStopTime(userTypeVO, openNum);
            brankVO.setAvgStopTime(stopTime);

            result.add(brankVO);
        }
    }

    private void getPlatform(String groupName, UserTypeVO userTypeVO, List<SystemVO> result) {
        List<Map<String, Object>> userImg = userMapper.getUserImg(userTypeVO);
        List<Map<String, Object>> newuserList = userMapper.getUserImgNewUser(userTypeVO);
        List<Map<String, Object>> visoUserList = userMapper.getUserImgVisoUser(userTypeVO);
        List<Map<String, Object>> userImgExit = userMapper.getUserImgExit(userTypeVO);
        List<Map<String, Object>> open = userMapper.getUserImgOpen(userTypeVO);
        SystemVO systemVO = null;
        for (Map<String, Object> map : userImg) {
            systemVO = new SystemVO();
            String key = (String) map.get(groupName);
            systemVO.setClientPlat(key);
            Integer integer = getnewUserNum(key, groupName, newuserList);
            systemVO.setNewUserNum(integer);
            Map<String, Integer> visorUser = getVisorUser(key, groupName, visoUserList);
            systemVO.setUserNum(visorUser.get("userNum"));
            systemVO.setVisitNum(visorUser.get("visitNum"));
            Integer open1 = getOpen(key, groupName, open);
            systemVO.setOpenNum(open1);
            Integer exit = getExit(key, groupName, userImgExit);
            Integer openNum = systemVO.getOpenNum();
            String percent = DecimalFormatUtils.getPercent(exit, openNum);
            systemVO.setExitRate(percent);
            userTypeVO.setValue(key);
            String stopTime = getStopTime(userTypeVO, open1);
            systemVO.setAvgStopTime(stopTime);
            result.add(systemVO);
        }
    }

    private PageUtils<UserDatailVO> getUserDetails(String cloum, UserQueryVO userQueryVO) {
        List<UserDatailVO> result = new ArrayList<>();
        UserQueryColVO userQueryColVO = new UserQueryColVO();
        BeanUtils.copyProperties(userQueryVO, userQueryColVO);
        userQueryColVO.setColum(cloum);
        result = userMapper.getUserPage(userQueryColVO);
        Integer count = userMapper.getUserCount(userQueryColVO);
        PageUtils<UserDatailVO> pageUtils = new PageUtils(userQueryColVO.getPageSize(), userQueryVO.getPageIndex(), count);
        pageUtils.setLists(result);
        return pageUtils;
    }

    private List<UserDateRepVO> getPortalDetailDatas(DataBroadVO dataBroadVO) {
        List<UserDateRepVO> result = new ArrayList<>();
        List<UserDateRepVO> portalDetailDatas = userMapper.getPortalDetailDatas(dataBroadVO);
        List<PortalDetailDatasRepVO> newUserList = dataBroadMapper.getTotalnewUserDate(dataBroadVO);
        List<Map<String, Object>> exitDate = userMapper.getExitDate(dataBroadVO);
        Map<String, Integer> exitDateMap = exitDate.stream().collect(
                Collectors.toMap(x -> x.get("dt").toString(),
                        x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> newUserMap = newUserList.stream().collect(Collectors.toMap(PortalDetailDatasRepVO::getDate, PortalDetailDatasRepVO::getNewUserNum));

        for(UserDateRepVO userDateRepVO:portalDetailDatas){
            String dt = userDateRepVO.getDate();
            DataBroadVO dataBroadVO2=new DataBroadVO();
            dataBroadVO2.setAppKey(dataBroadVO.getAppKey());
            dataBroadVO2.setBeginDate(dt);
            Integer openNum = userDateRepVO.getOpenNum();
            String time="00:00:00";
            if(openNum != 0){
                Integer dayStopTime = userMapper.getDayStopTime(dataBroadVO2);
                time = TimeUtils.getGapTime(dayStopTime/openNum);
            }
            userDateRepVO.setAvgStopTime(time);
            Integer exit =exitDateMap.get(dt)==null?0:exitDateMap.get(dt);
            String percent = DecimalFormatUtils.getPercent(exit, userDateRepVO.getOpenNum());
            userDateRepVO.setExitRate(percent);
            userDateRepVO.setNewUserNum(0);
            if(newUserMap.get(dt)!=null)userDateRepVO.setNewUserNum(newUserMap.get(dt));
            result.add(userDateRepVO);
        }
        return result;
    }

    private List<UserActiveRepVO> getUserActive(UserVO userVO) {
        List<UserActiveRepVO>   userActiveDate = userMapper.getUserActiveDate(userVO);
        UserVO userVO1 = null;
        for (UserActiveRepVO userActiveRepVO : userActiveDate) {
              getActive(userActiveRepVO,userVO1,userVO);
        }
        return userActiveDate;
    }

    private void getActive(UserActiveRepVO userActiveRepVO, UserVO userVO1, UserVO userVO) {
        userVO1 = new UserVO();
        BeanUtils.copyProperties(userVO, userVO1);
        String   endDate=userActiveRepVO.getCurDate();
        userVO1.setBeginDate(TimeUtils.reduceDayone(endDate, -7));
        userVO1.setEndDate(endDate);
        Integer userActive = userMapper.getUserActive(userVO1);
        userActive= userActive==null?0:userActive;
        userActiveRepVO.setWau(userActive);
        LocalDate beginDateTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = beginDateTime.minusMonths(1);
        String month = localDate.format(fmt);
        userVO1.setBeginDate(month);
        Integer userActive1 = userMapper.getUserActive(userVO1);
        userActive1= userActive1==null?0:userActive1;
        userActiveRepVO.setMau(userActive1);
        Integer dau = userActiveRepVO.getDau();
        String wau = DecimalFormatUtils.getPercent(dau, userActiveRepVO.getWau());
        String mau = DecimalFormatUtils.getPercent(dau, userActiveRepVO.getMau());
        userActiveRepVO.setDwRate(wau);
        userActiveRepVO.setDmRate(mau);
    }

    private String getStopTime(UserTypeVO userTypeVO, Integer open) {
        Integer stopTime = userMapper.getStopTime(userTypeVO);
        String time = open == 0 ? "00:00:00" : TimeUtils.getGapTime(stopTime/open);
        return time;
    }

    private List<UserDateRepVO> getDataList(DataBroadVO dataBroadVO){
        List<UserDateRepVO> dateList = mysqlMapper.getDateList(dataBroadVO);
        if(dateList.size()==0){
            dateList = getPortalDetailDatas(dataBroadVO);
            totalService.insertApiTotal(dataBroadVO.getBeginDate(),dataBroadVO.getEndDate());
        }
        return dateList;
//        if(dataBroadVO.getType()!=null){
//            if(dataBroadVO.getType()==1||dataBroadVO.getType()==2||dataBroadVO.getType()==3||dataBroadVO.getType()==4){
//                String dt = TimeUtils.getYestDayString();
//                dataBroadVO.setEndDate(dt);
//                Integer type=dataBroadVO.getType();
//                if (type == 1) {
//                    dataBroadVO.setBeginDate(dt);
//                } else if (type == 2) {
//                    String seven = TimeUtils.reduceDayone(dt, -7);
//                    dataBroadVO.setBeginDate(seven);
//                } else if (type == 3) {
//                    String seven = TimeUtils.reduceDayone(dt, -15);
//                    dataBroadVO.setBeginDate(seven);
//                } else if (type == 4) {
//                    dataBroadVO.setBeginDate(TimeUtils.getReduceMonth(dt));
//                }
//                List<UserDateRepVO> dateList = mysqlMapper.getDateList(dataBroadVO);
//                return  dateList;
//            }
//        }
//        List<UserDateRepVO> userDateRepLsit = getPortalDetailDatas(dataBroadVO);
    }

    private List<UserActiveRepVO> getActiveList(UserPageVO userPageVO){
      /*  if(userPageVO.getType()!=null){
            if(userPageVO.getType()==1||userPageVO.getType()==2||userPageVO.getType()==3||userPageVO.getType()==4){
                String dt = TimeUtils.getYestDayString();
                userPageVO.setEndDate(dt);
                Integer type=userPageVO.getType();
                if (type == 1) {
                    userPageVO.setBeginDate(dt);
                } else if (type == 2) {
                    String seven = TimeUtils.reduceDayone(dt, -7);
                    userPageVO.setBeginDate(seven);
                } else if (type == 3) {
                    String seven = TimeUtils.reduceDayone(dt, -15);
                    userPageVO.setBeginDate(seven);
                } else if (type == 4) {
                    userPageVO.setBeginDate(TimeUtils.getReduceMonth(dt));
                }
                List<UserActiveRepVO> dateList = mysqlMapper.getActiveList(userPageVO);
                return  dateList;
            }
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userPageVO,userVO);
        List<UserActiveRepVO> userActive = getUserActive(userVO);*/
        List<UserActiveRepVO> dateList = mysqlMapper.getActiveList(userPageVO);
        if(dateList.size()==0){
            UserVO userVO=new UserVO();
            BeanUtils.copyProperties(userPageVO,userVO);
            dateList = getUserActive(userVO);
            totalService.insertApiTotal(userPageVO.getBeginDate(),userPageVO.getEndDate());
        }
        return  dateList;
    }
    private  PageUtils<UserDatailVO> getActiveListPage(UserPageVO userPageVO){
        Integer activieCount = mysqlMapper.getActivieCount(userPageVO);
        PageUtils<UserDatailVO> pageUtils = new PageUtils(userPageVO.getPageSize(), userPageVO.getPageIndex(), activieCount);
        if(activieCount==null||activieCount==0){
            UserVO userVO=new UserVO();
            BeanUtils.copyProperties(userPageVO,userVO);
            List<UserActiveRepVO> userActiveDate = userMapper.getUserActiveDate(userVO);
            pageUtils = new PageUtils(userPageVO.getPageSize(), userPageVO.getPageIndex(), userActiveDate.size());
            if(CollectionUtils.isEmpty(userActiveDate)){
                return  pageUtils;
            }
            List<UserActiveRepVO> userActiveDatePage = userMapper.getUserActiveDatePage(userPageVO);
            UserVO userVO1 = null;
            for (UserActiveRepVO userActiveRepVO : userActiveDatePage) {
                getActive(userActiveRepVO,userVO1,userVO);
            }
            pageUtils.setLists(userActiveDatePage);
            totalService.insertApiTotal(userPageVO.getBeginDate(),userPageVO.getEndDate());
            return pageUtils;
        }
        List<UserActiveRepVO> userActiveDatePage = mysqlMapper.getActiviePage(userPageVO);
        pageUtils.setLists(userActiveDatePage);
        return  pageUtils;
        /*if(userPageVO.getType()!=null){
            if(userPageVO.getType()==1||userPageVO.getType()==2||userPageVO.getType()==3||userPageVO.getType()==4){
                String dt = TimeUtils.getYestDayString();
                userPageVO.setEndDate(dt);
                Integer type=userPageVO.getType();
                if (type == 1) {
                    userPageVO.setBeginDate(dt);
                } else if (type == 2) {
                    String seven = TimeUtils.reduceDayone(dt, -7);
                    userPageVO.setBeginDate(seven);
                } else if (type == 3) {
                    String seven = TimeUtils.reduceDayone(dt, -15);
                    userPageVO.setBeginDate(seven);
                } else if (type == 4) {
                    userPageVO.setBeginDate(TimeUtils.getReduceMonth(dt));
                }
                Integer activieCount = mysqlMapper.getActivieCount(userPageVO);
                PageUtils<UserDatailVO> pageUtils = new PageUtils(userPageVO.getPageSize(), userPageVO.getPageIndex(), activieCount);
                if(activieCount==null||activieCount==0){
                    return  pageUtils;
                }
                List<UserActiveRepVO> userActiveDatePage = mysqlMapper.getActiviePage(userPageVO);
                pageUtils.setLists(userActiveDatePage);
                return  pageUtils;
            }
        }*/
        /*UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userPageVO,userVO);
        List<UserActiveRepVO> userActiveDate = userMapper.getUserActiveDate(userVO);
        PageUtils<UserDatailVO> pageUtils = new PageUtils(userPageVO.getPageSize(), userPageVO.getPageIndex(), userActiveDate.size());
        if(CollectionUtils.isEmpty(userActiveDate)){
            return  pageUtils;
        }
        List<UserActiveRepVO> userActiveDatePage = userMapper.getUserActiveDatePage(userPageVO);
        UserVO userVO1 = null;
        for (UserActiveRepVO userActiveRepVO : userActiveDatePage) {
            getActive(userActiveRepVO,userVO1,userVO);
        }
        pageUtils.setLists(userActiveDatePage);
        return pageUtils;*/

    }

    private UserStaticDate getUserStaticDate(String date, String count){
        UserStaticDate userStaticDate=new UserStaticDate();
        userStaticDate.setDateTime(date);
        userStaticDate.setCount(count);
        return  userStaticDate;
    }



}
