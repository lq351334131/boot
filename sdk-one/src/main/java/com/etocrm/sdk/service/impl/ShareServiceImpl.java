package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.ShareMapper;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.share.*;
import com.etocrm.sdk.entity.user.UserDatailVO;
import com.etocrm.sdk.service.ShareService;
import com.etocrm.sdk.util.DecimalFormatUtils;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result getUserShare(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        ShareUserVO userShare = shareMapper.getUserShare(dataBroadVO);
        if(userShare!=null){
           //回流量、分享新增人数（回流量pageshow之间的关系）
            Integer userSharebackflow = shareMapper.getUserSharebackflow(dataBroadVO);
            userShare.setTurnbackTotal(userSharebackflow==null?0:userSharebackflow);
            String rate=DecimalFormatUtils.getPercent(userShare.getTurnbackTotal(),userShare.getShareNumTotal());
            userShare.setTurnbackPropTotal(rate);
            Integer userPlusNumTotal=shareMapper.getUserShareAdd(dataBroadVO);
            userShare.setUserPlusNumTotal(userPlusNumTotal==null?0:userPlusNumTotal);
        }
        return Result.success(userShare);
    }

    @Override
    public Result getShareNumList(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<ShareNumVO> shareNumVOLIst=shareMapper.getShareNumList(dataBroadVO);
        return Result.success(shareNumVOLIst);
    }

    @Override
    public Result getShareGenderList(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<ShareGenderVO> shareNumVOLIst=shareMapper.getShareGenderList(dataBroadVO);
        int  size=shareNumVOLIst.size();
        if(size>0){
            ShareGenderVO totalshareGenderVO = shareNumVOLIst.get(size - 1);
            for(ShareGenderVO shareGenderVO:shareNumVOLIst){
                shareGenderVO.setRate(DecimalFormatUtils.getPercent(shareGenderVO.getNum(),totalshareGenderVO.getNum()));
            }
            shareNumVOLIst.remove(size-1);
        }
        /*int sum = shareNumVOLIst.stream().mapToInt(ShareGenderVO::getNum).sum();
        for(ShareGenderVO shareGenderVO:shareNumVOLIst){
            shareGenderVO.setRate(DecimalFormatUtils.getPercent(shareGenderVO.getNum(),sum));
        }*/
        return Result.success(shareNumVOLIst);
    }

    @Override
    public Result getAreaShareLeaderboard(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<ShareCityVO> list=shareMapper.getAreaShareLeaderboard(dataBroadVO);
        if(list.size()==0)return Result.success();
        //EchartsData-前10名城市+其他(其他的取前十之后数据求和)
        //List<Map<String, Object>> shareUrlList = shareMapper.getShareUrlList(dataBroadVO);
        List<String> sharList = shareMapper.getShareUrlList(dataBroadVO);
        if(sharList.size()==0)return Result.success();
        List<String> cityList = list.stream().map(ShareCityVO::getCity).collect(Collectors.toList());
        SharepathVO sharepathVO=new SharepathVO();
        BeanUtils.copyProperties(dataBroadVO,sharepathVO);
        sharepathVO.setList(sharList);
        sharepathVO.setCityList(cityList);
        //回流量
        List<Map<String, Object>> shareCityList = shareMapper.getShareCityList(sharepathVO);
        List<Map<String, Object>> shareCityAddList = shareMapper.getShareCityaddList(sharepathVO);
        Map<String, Integer> userMap = shareCityList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> addUserMap = shareCityAddList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));

        for(ShareCityVO shareGenderVO:list){
            //回流量,回流量/shareGenderVO.getShareNum,分享新增（被分享的人且是新用户）
            String city = shareGenderVO.getCity();
            Integer userNum = userMap.get(city)==null?0:userMap.get(city);
            Integer addUNum= addUserMap.get(city)==null?0:addUserMap.get(city);;
            shareGenderVO.setTurnback(userNum);
            shareGenderVO.setUserPlusNum(addUNum);
            String percent = DecimalFormatUtils.getPercent(userNum, shareGenderVO.getShareNum());
            shareGenderVO.setTurnbackProp(percent);
        }
        //Map<String,Object> map=new HashMap<>();
        ShareCityReqVO shareCityReqVO=new ShareCityReqVO();
       // map.put("Datas",list);
        shareCityReqVO.setDatas(list);
        int size=list.size();
        List<ShareCityVOTopVO> qi=new ArrayList<>();
        int subSize=size>10?10:size;
        List<ShareCityVO> shareNumVOLIst=new ArrayList<>(list.subList(0,subSize));
        for(ShareCityVO shareCityVO:shareNumVOLIst){
            ShareCityVOTopVO shareCityVOTopVO=new ShareCityVOTopVO();
            shareCityVOTopVO.setCity(shareCityVO.getCity());
            shareCityVOTopVO.setPeopleNum(shareCityVO.getPeopleNum());
            qi.add(shareCityVOTopVO);
        }
        if(size>10){
            List<ShareCityVO> shareNumVOLIst2=new ArrayList<>(list.subList(10,size));
            int sum = shareNumVOLIst2.stream().mapToInt(ShareCityVO::getPeopleNum).sum();
            ShareCityVOTopVO shareCityVOTopVO=new ShareCityVOTopVO();
            shareCityVOTopVO.setCity("其他");
            shareCityVOTopVO.setPeopleNum(sum);
            qi.add(shareCityVOTopVO);
        }
        //map.put("EchartsData",qi);
        shareCityReqVO.setEchartsData(qi);
        return Result.success(shareCityReqVO);
    }

    @Override
    public Result getSharePageTop10(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<SharePageTop10VO> sharePageTop10 = shareMapper.getSharePageTop10(dataBroadVO);
        try{
            if(CollectionUtils.isNotEmpty(sharePageTop10)){
                List<String> urlList = sharePageTop10.stream().map(SharePageTop10VO::getUrl).collect(Collectors.toList());
                BesharedTop10VO besharedTop10VO=new BesharedTop10VO();
                BeanUtils.copyProperties(dataBroadVO,besharedTop10VO);
                besharedTop10VO.setList(urlList);
                besharedTop10VO.setType(0);
                List<SharePageTop10VO> turnbackTotalList = shareMapper.getBesharedTop10Num(besharedTop10VO);
                Map<String, Integer> turnbackTotalLMap = turnbackTotalList.stream().collect(Collectors.toMap(SharePageTop10VO::getUrl, SharePageTop10VO::getTurnbackTotal ));
               // besharedTop10VO.setType(1);
               // List<SharePageTop10VO> userPlusNumTotalList = shareMapper.getBesharedTop10Num(besharedTop10VO);
               // Map<String, Integer> userPlusNumTotalMap = userPlusNumTotalList.stream().collect(Collectors.toMap(SharePageTop10VO::getUrl,SharePageTop10VO::getUserPlusNumTotal ));
                for(SharePageTop10VO sharePageTop10VO:sharePageTop10){
                    String url = sharePageTop10VO.getUrl();
                    sharePageTop10VO.setTurnbackTotal(turnbackTotalLMap.get(url)==null?0:turnbackTotalLMap.get(url));
                    //sharePageTop10VO.setUserPlusNumTotal(userPlusNumTotalMap.get(url)==null?0:userPlusNumTotalMap.get(url));
                    String percent = DecimalFormatUtils.getPercent(sharePageTop10VO.getTurnbackTotal(), sharePageTop10VO.getShareNumTotal());
                    sharePageTop10VO.setTurnbackPropTotal(percent);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return  Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(sharePageTop10);
    }

    @Override
    public Result getShareUserPlusNumList(DataBroadVO dataBroadVO) {
        //被分享人数
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<ShareNumVO> sharePageTop10 = shareMapper.getShareUserPlusNumList(dataBroadVO);
        return Result.success(sharePageTop10);
    }

    @Override
    public List<SharePageRepVO> downLoadPageShareList(PageShareListVO pageShareListVO) {
        if (ovalUtils.validatorRequestParam(pageShareListVO).size() > 0) return Collections.emptyList();
        List<SharePageRepVO> list=shareMapper.downLoadPageShareList(pageShareListVO);
        if(list.size()==0)return list;
        SharePage sharePage=new SharePage();
        BeanUtils.copyProperties(pageShareListVO,sharePage);
        getPageList(list,sharePage);
        return list;
    }

    @Override
    public Result getPageShare(SharePage sharePage) {
        if (ovalUtils.validatorRequestParam(sharePage).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer pageShareCount = shareMapper.getPageShareCount(sharePage);
        PageUtils<SharePageRepVO> pageUtils=new PageUtils<>(sharePage.getPageSize(),sharePage.getPageIndex(),pageShareCount);
        if(pageShareCount==null){
            pageUtils.setLists(Collections.emptyList());
            return Result.success(pageUtils);
        }
        List<SharePageRepVO> pageList=shareMapper.getPageShare(sharePage);
        if(CollectionUtils.isNotEmpty(pageList)){
            getPageList(pageList,sharePage);
            pageUtils.setLists(pageList);
        }
        return Result.success(pageList);
    }



    @Override
    public Result getShareTurnbackList(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        //回流量
        return Result.success(shareMapper.getShareTurnbackList(dataBroadVO));
    }

    @Override
    public List<ShareCityVO> downLoadAreaShareLeaderboard(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Collections.emptyList();
        List<ShareCityVO> list=shareMapper.getAreaShareLeaderboard(dataBroadVO);
        if(CollectionUtils.isEmpty(list)){
           return Collections.emptyList();
        }
       /*
        List<Map<String, Object>> shareCityList = shareMapper.getShareCityList(dataBroadVO);
        List<Map<String, Object>> shareCityAddList = shareMapper.getShareCityaddList(dataBroadVO);
        Map<String, Integer> userMap = shareCityList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> addUserMap = shareCityAddList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(ShareCityVO shareGenderVO:list){
            //回流量,回流量/shareGenderVO.getShareNum,分享新增（被分享的人且是新用户）
            String city = shareGenderVO.getCity();
            Integer userNum = userMap.get(city);
            Integer addUNum= addUserMap.get(city);
            shareGenderVO.setTurnback(userNum);
            shareGenderVO.setUserPlusNum(addUNum);
            String percent = DecimalFormatUtils.getPercent(userNum, shareGenderVO.getShareNum());
            shareGenderVO.setTurnbackProp(percent);
            Integer userPlusNum = addUserMap.get(city);
            shareGenderVO.setUserPlusNum(userPlusNum);
        }*/
        return list;
    }

    @Override
    public List<UserCityRepVO> downLoadAreaShareList(UserCitySeaVO userCitySeaVO) {
        if (ovalUtils.validatorRequestParam(userCitySeaVO).size() > 0) return Collections.emptyList();
        List<UserCityRepVO> areaShareListExcel = shareMapper.getAreaShareListExcel(userCitySeaVO);
        if(CollectionUtils.isEmpty(areaShareListExcel)){
            return Collections.emptyList();
        }
        UserCitySeaPageVO userCitySeaPageVO=new UserCitySeaPageVO();
        BeanUtils.copyProperties(userCitySeaVO,userCitySeaPageVO);
        getUserCity(areaShareListExcel,userCitySeaPageVO);
        return areaShareListExcel;
    }

    @Override
    public Result getAreaShareList(UserCitySeaPageVO userCitySeaPageVO) {
        if (ovalUtils.validatorRequestParam(userCitySeaPageVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer userCityCount = shareMapper.getUserCityCount(userCitySeaPageVO);
        PageUtils<UserCityRepVO> pageUtils=new PageUtils<>(userCitySeaPageVO.getPageSize(),userCitySeaPageVO.getPageIndex(),userCityCount);
        if(userCityCount==null){
            pageUtils.setLists(Collections.emptyList());
            return Result.success(pageUtils);
        }
        List<UserCityRepVO> areaShareList= shareMapper.getAreaShareListList(userCitySeaPageVO);
        if(areaShareList.size()==0)Result.success(pageUtils);
        getUserCity(areaShareList,userCitySeaPageVO);
        /*List<String> cityList = areaShareList.stream().map(UserCityRepVO::getCity).collect(Collectors.toList());
        SharepathVO sharepathVO=new SharepathVO();
        BeanUtils.copyProperties(userCitySeaPageVO,sharepathVO);
        DataBroadVO  dataBroadVO=new DataBroadVO();
        BeanUtils.copyProperties(userCitySeaPageVO,dataBroadVO);
        List<String> sharList = shareMapper.getShareUrlList(dataBroadVO);
        sharepathVO.setList(sharList);
        sharepathVO.setCityList(cityList);
        //回流量
        List<Map<String, Object>> shareCityList = shareMapper.getShareCityList(sharepathVO);
        List<Map<String, Object>> shareCityAddList = shareMapper.getShareCityaddList(sharepathVO);
        Map<String, Integer> userMap = shareCityList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> addUserMap = shareCityAddList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(UserCityRepVO userCityRepVO:areaShareList){
            //回流量,回流量/shareGenderVO.getShareNum,分享新增（被分享的人且是新用户）
            String city = userCityRepVO.getCity();
            Integer userNum = userMap.get(city)==null?0:userMap.get(city);
            Integer addUNum= addUserMap.get(city)==null?0:addUserMap.get(city);;
            userCityRepVO.setTurnback(userNum);
            userCityRepVO.setUserPlusNum(addUNum);
            String percent = DecimalFormatUtils.getPercent(userNum, userCityRepVO.getShareNum());
            userCityRepVO.setTurnbackProp(percent);
        }
*/
        pageUtils.setLists(areaShareList);
        return Result.success(pageUtils);
    }

    @Override
    public  Result getUserShareList(DataBroadVO dataBroadVO){
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<DateShareVO> list=shareMapper.getDateUser(dataBroadVO);
        if(CollectionUtils.isEmpty(list)){
            return Result.success(Collections.emptyList());
        }
        List<Map<String, Object>> shareBuCityList = shareMapper.getDateUserBuckAll(dataBroadVO);
        List<Map<String, Object>> shareCityAddList = shareMapper.getDateUseraddAll(dataBroadVO);
        // Map<String, Integer> userMap = shareCityList.stream().collect(Collectors.toMap(x -> x.get("dt").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> addUserMap = shareCityAddList.stream().collect(Collectors.toMap(x -> x.get("dt").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> buMap = shareBuCityList.stream().collect(Collectors.toMap(x -> x.get("dt").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(DateShareVO dateShareVO:list){
            //回流量,回流量/shareGenderVO.getShareNum,分享新增（被分享的人且是新用户）
            String date = dateShareVO.getDate();
            Integer addUNum= addUserMap.get(date);
            dateShareVO.setUserPlusNum(addUNum==null?0:addUNum);
            Integer turnback = buMap.get(date)==null?0:buMap.get(date);
            dateShareVO.setTurnback(turnback);
            String percent = DecimalFormatUtils.getPercent(turnback, dateShareVO.getShareNum());
            dateShareVO.setTurnbackProp(percent);

        }
        return Result.success(list);
    }

    @Override
    public Result getSharePageUserList(DateSharePageVO dateSharePageVO) {
        if (ovalUtils.validatorRequestParam(dateSharePageVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer dateUserCount = shareMapper.getDateUserCount(dateSharePageVO);
        PageUtils<DateShareVO> pageUtil = new PageUtils<>(dateSharePageVO.getPageSize(), dateSharePageVO.getPageIndex(), dateUserCount);
        pageUtil.setPageIndex(pageUtil.getPageIndex());
        if (dateUserCount == 0 || dateUserCount==null) {
            pageUtil.setLists(Collections.emptyList());
            return Result.success(pageUtil);
        }
        List<DateShareVO> list=shareMapper.getDateUserPage(dateSharePageVO);
        if(CollectionUtils.isEmpty(list)){
            return Result.success(Collections.emptyList());
        }
        List<Map<String, Object>> shareBuCityList = shareMapper.getDateUserBuckPage(dateSharePageVO);
        List<Map<String, Object>> shareCityAddList = shareMapper.getDateUseraddPage(dateSharePageVO);
        // Map<String, Integer> userMap = shareCityList.stream().collect(Collectors.toMap(x -> x.get("dt").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> addUserMap = shareCityAddList.stream().collect(Collectors.toMap(x -> x.get("dt").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> buMap = shareBuCityList.stream().collect(Collectors.toMap(x -> x.get("dt").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(DateShareVO dateShareVO:list){
            //回流量,回流量/shareGenderVO.getShareNum,分享新增（被分享的人且是新用户）
            String date = dateShareVO.getDate();
            //Integer userNum = userMap.get(city);
            Integer addUNum= addUserMap.get(date);
            dateShareVO.setUserPlusNum(addUNum==null?0:addUNum);
            Integer turnback = buMap.get(date)==null?0:buMap.get(date);
            dateShareVO.setTurnback(turnback);
            String percent = DecimalFormatUtils.getPercent(turnback, dateShareVO.getShareNum());
            dateShareVO.setTurnbackProp(percent);
        }
        pageUtil.setLists(list);
        return Result.success(pageUtil);
    }

    @Override
    public Result getShareAreaUserList(UserSearchVO userSearchVO) {
        if (ovalUtils.validatorRequestParam(userSearchVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer pageShareCount = shareMapper.getShareAreaUserCount(userSearchVO);
        PageUtils<UserDatailVO> pageUtils=new PageUtils<>(userSearchVO.getPageSize(),userSearchVO.getPageIndex(),pageShareCount);
        if(pageShareCount==null){
            pageUtils.setLists(Collections.emptyList());
            return Result.success(pageUtils);
        }
        List<UserDatailVO> pageList=shareMapper.getShareAreaUserList(userSearchVO);
        if(CollectionUtils.isNotEmpty(pageList)){
            pageUtils.setLists(pageList);
        }
        return Result.success(pageList);
    }

    @Override
    public Result getShareUserList(ShareUserListVO shareUserListVO) {
        if (ovalUtils.validatorRequestParam(shareUserListVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer pageShareCount = shareMapper.getShareUserCount(shareUserListVO);
        PageUtils<UserDatailVO> pageUtils=new PageUtils<>(shareUserListVO.getPageSize(),shareUserListVO.getPageIndex(),pageShareCount);
        if(pageShareCount==null){
            pageUtils.setLists(Collections.emptyList());
            return Result.success(pageUtils);
        }
        List<ShareUser> pageList=shareMapper.getShareUserList(shareUserListVO);
        pageUtils.setLists(pageList);
        return Result.success(pageList);
    }

    private void getPageList(List<SharePageRepVO> pageList, SharePage sharePage) {
        List<String> urlList = pageList.stream().map(SharePageRepVO::getUrl).collect(Collectors.toList());
        PageShareListTypeVO pageShareListTypeVO=new PageShareListTypeVO();
        BeanUtils.copyProperties(sharePage,pageShareListTypeVO);
        pageShareListTypeVO.setList(urlList);
        //pageShareListTypeVO.setType(0);
        //List<SharePageRepVO> turnbackTotalList = shareMapper.getPageSharebackoradd(pageShareListTypeVO);
        //Map<String, Integer> turnbackTotalLMap = turnbackTotalList.stream().collect(Collectors.toMap(SharePageRepVO::getUrl,SharePageRepVO::getTurnback));
        pageShareListTypeVO.setType(1);
        List<SharePageRepVO> userPlusNumTotalList = shareMapper.getPageSharebackoradd(pageShareListTypeVO);
        Map<String, Integer> userPlusNumTotalMap = userPlusNumTotalList.stream().collect(Collectors.toMap(SharePageRepVO::getUrl, SharePageRepVO::getUserPlusNum ));
        Map<String, Integer> turnbackTotalLMap = userPlusNumTotalList.stream().collect(Collectors.toMap(SharePageRepVO::getUrl, SharePageRepVO::getTurnback));
        for(SharePageRepVO sharePageRepVO:pageList){
            String url = sharePageRepVO.getUrl();
            sharePageRepVO.setTurnback(turnbackTotalLMap.get(url)==null?0:turnbackTotalLMap.get(url));
            sharePageRepVO.setUserPlusNum(userPlusNumTotalMap.get(url)==null?0:userPlusNumTotalMap.get(url));
            String percent = DecimalFormatUtils.getPercent(sharePageRepVO.getTurnback(), sharePageRepVO.getShareNum());
            sharePageRepVO.setTurnbackProp(percent);
        }
    }

    private void  getUserCity(List<UserCityRepVO> areaShareList, UserCitySeaPageVO userCitySeaPageVO){
        List<String> cityList = areaShareList.stream().map(UserCityRepVO::getCity).collect(Collectors.toList());
        SharepathVO sharepathVO=new SharepathVO();
        BeanUtils.copyProperties(userCitySeaPageVO,sharepathVO);
        DataBroadVO dataBroadVO=new DataBroadVO();
        BeanUtils.copyProperties(userCitySeaPageVO,dataBroadVO);
        List<String> sharList = shareMapper.getShareUrlList(dataBroadVO);
        sharepathVO.setList(sharList);
        sharepathVO.setCityList(cityList);
        //回流量
        List<Map<String, Object>> shareCityList = shareMapper.getShareCityList(sharepathVO);
        List<Map<String, Object>> shareCityAddList = shareMapper.getShareCityaddList(sharepathVO);
        Map<String, Integer> userMap = shareCityList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));
        Map<String, Integer> addUserMap = shareCityAddList.stream().collect(Collectors.toMap(x -> x.get("city").toString(), x -> Integer.valueOf(x.get("num").toString())));
        for(UserCityRepVO userCityRepVO:areaShareList){
            //回流量,回流量/shareGenderVO.getShareNum,分享新增（被分享的人且是新用户）
            String city = userCityRepVO.getCity();
            Integer userNum = userMap.get(city)==null?0:userMap.get(city);
            Integer addUNum= addUserMap.get(city)==null?0:addUserMap.get(city);;
            userCityRepVO.setTurnback(userNum);
            userCityRepVO.setUserPlusNum(addUNum);
            String percent = DecimalFormatUtils.getPercent(userNum, userCityRepVO.getShareNum());
            userCityRepVO.setTurnbackProp(percent);
        }
    }
}
