package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.DataBroadMapper;
import com.etocrm.sdk.dao.PageMapper;
import com.etocrm.sdk.dao.ScenceMapper;
import com.etocrm.sdk.entity.databroad.*;
import com.etocrm.sdk.entity.page.StopTimeVO;
import com.etocrm.sdk.entity.total.ToTalPO;
import com.etocrm.sdk.mysqldao.MysqlMapper;
import com.etocrm.sdk.service.DataBroadService;
import com.etocrm.sdk.service.DataService;
import com.etocrm.sdk.service.TotalService;
import com.etocrm.sdk.util.DecimalFormatUtils;
import com.etocrm.sdk.util.OvalUtils;
import com.etocrm.sdk.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataBroadServiceImpl implements DataBroadService {

    @Autowired
    private OvalUtils ovalUtils;

    @Autowired
    private DataBroadMapper dataBroadMapper;

    @Autowired
    private DataService dataService;

    @Autowired
    private ScenceMapper sceneMapper;

    @Autowired
    private MysqlMapper mysqlMapper;

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private TotalService totalService;


    @Override
    public Result portalData(DataBroadVO dataBroadVO) {
        if (ovalUtils.validatorRequestParam(dataBroadVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        DataBroadRepVO dataBroadRepVO = new DataBroadRepVO();
        dataBroadRepVO.setTotalData(getTotalDataRepVO(dataBroadVO));
        //页面前5
        dataBroadRepVO.setPathDatas(getPathDataLsit(dataBroadVO));
        List<PortalDetailDatasRepVO> portalDetailDatas = getPortalDetailDatas(dataBroadVO);
        dataBroadRepVO.setPortalDetailDatas(portalDetailDatas);
        //区域
        dataBroadRepVO.setRegDatas(getRegData(dataBroadVO));
        //场景前五
        dataBroadRepVO.setSceneDatas(getSence(dataBroadVO));
        dataBroadRepVO.setQrDatas(getQrData(dataBroadVO));
        return Result.success(dataBroadRepVO);
    }

    @Override
    public TotalDataRepVO getTotalDataRepVO(DataBroadVO dataBroadVO) {
        TotalDataRepVO totalPo = getTotal(dataBroadVO);
        return totalPo;
        //访问次数、打开次数、新用户数
//        TotalDataRepVO totalDataRepVO = dataBroadMapper.getTotalDataRepVO(dataBroadVO);
//        Integer newUser=dataBroadMapper.getTotalnewUser(dataBroadVO);
//        newUser=newUser==null?0:newUser;
//        totalDataRepVO.setNewUserNum(newUser);
//       // List<Map<String, Object>> visNum = dataBroadMapper.getVisNum(dataBroadVO);
//        Integer  boundPage=dataBroadMapper.getBounce(dataBroadVO);//跳出
////        if(CollectionUtils.isNotEmpty(visNum)){
////            BigInteger accUserNum = (BigInteger) visNum.get(0).get("accUserNum");
////            totalDataRepVO.setUserNum(accUserNum.intValue());//访问人数
////        }
//        boundPage=boundPage==null?0 :boundPage;//跳出率
//        String percent = DecimalFormatUtils.getPercent(boundPage, totalDataRepVO.getOpenNum().intValue());
//        totalDataRepVO.setBounceRate(percent + "%");
//        totalDataRepVO.setAvgStopTime(dataService.getStopTime(dataBroadVO));
 //       return totalDataRepVO;
    }

    private  List<PathDataRepVO>  getPathDataLsit(DataBroadVO dataBroadVO){
        List<PathDataRepVO> pathDataRepVO = dataBroadMapper.getTopVisNum(dataBroadVO);
        DataBroadLIstVO dataBroadLIstVO=new DataBroadLIstVO();
        BeanUtils.copyProperties(dataBroadVO,dataBroadLIstVO);
        try {
            for (PathDataRepVO p : pathDataRepVO) {
                String visitPath = p.getVisitPath();
                dataBroadLIstVO.setVisitpath(visitPath);
                //跳出率
                Integer topExitVisNum = dataBroadMapper.getTopExitVisNum(dataBroadLIstVO);
                topExitVisNum=topExitVisNum==null?0:topExitVisNum;
                Long topOpen=p.getOpenNum();
               // Integer topOpen = dataBroadMapper.getTopOpen(dataBroadLIstVO);
                //topOpen=topOpen==null?0:topOpen;
                String percent = DecimalFormatUtils.getPercent(topExitVisNum, topOpen.intValue());
                p.setBounceRate(percent);
                StopTimeVO stopTimeVO=new StopTimeVO();
                BeanUtils.copyProperties(dataBroadLIstVO,stopTimeVO);
                stopTimeVO.setP(visitPath);
                //Integer stoptime=dataBroadMapper.getTime(dataBroadLIstVO);
                //页时长
                List<Map<String, Object>> pageshowTotal = pageMapper.getPageshowTotal(stopTimeVO);
                Long stoptime = pageshowTotal.size() > 0 ?(Long) pageshowTotal.get(0).get("stopTime") : 0L;
                String time = topOpen == 0 ? "00:00:00" : TimeUtils.getGapTime(stoptime/p.getUserNum());
                p.setAvgDate(time);

            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return  pathDataRepVO;
    }
    
    private List<PortalDetailDatasRepVO> getPortalDetailDatas(DataBroadVO dataBroadVO) {
        List<PortalDetailDatasRepVO> portalDetailDatas = dataBroadMapper.getPortalDetailDatas(dataBroadVO);
        List<PortalDetailDatasRepVO> newUserList = dataBroadMapper.getTotalnewUserDate(dataBroadVO);
        if(!CollectionUtils.isEmpty(portalDetailDatas)){
            Map<String, Integer> collect = newUserList.stream().collect(Collectors.toMap(PortalDetailDatasRepVO::getDate, PortalDetailDatasRepVO::getNewUserNum));
            for(PortalDetailDatasRepVO portalDetailDatasRepVO:portalDetailDatas){
                String date=portalDetailDatasRepVO.getDate();
                Integer num=collect.get(date)==null?0:collect.get(date);
                portalDetailDatasRepVO.setNewUserNum(num);
            }
        }


 //       if(CollectionUtils.isEmpty(portalDetailDatas))return portalDetailDatas;
//        //访问人数
//        List<Map<String, Object>> userDate = dataBroadMapper.getUserDate(dataBroadVO);
//        Map<String, Integer> userDateMap = userDate.stream().collect(
//                Collectors.toMap(x -> x.get("dt").toString(),
//                        x -> Integer.valueOf(x.get("num").toString())));
//        List<UserDateRepVO> result = new ArrayList<>();
//        for(PortalDetailDatasRepVO portalDetailDatasRepVO:portalDetailDatas){
//            String dt = portalDetailDatasRepVO.getDate();
//            Integer userDateNum = userDateMap.get(dt);
//            int user1 = userDateNum == null ? 0 : userDateNum;
//            portalDetailDatasRepVO.setUserNum(user1);
//        }
        return portalDetailDatas;
    }

    private List<RegDataRepVO> getRegData(DataBroadVO dataBroadVO) {
        List<RegDataRepVO> result = dataBroadMapper.getTopProvince(dataBroadVO);
        return result;
    }

    private List<ScenDataRepVO> getSence(DataBroadVO dataBroadVO) {
        List<ScenDataRepVO> topExite = dataBroadMapper.getTopSence(dataBroadVO);
        if(topExite.size()==0)return topExite;
        //List<Map<String, Object>> sceneDetailInfo = sceneMapper.getSceneDetailInfo();
        //Map<String, String> collect = sceneDetailInfo.stream().collect(Collectors.toMap(x -> x.get("sceneId").toString(), x->x.get("sceneName")==null?"": x.get("sceneName").toString()));
        DataBroadLIstVO dataBroadLIstVO=new DataBroadLIstVO();
        BeanUtils.copyProperties(dataBroadVO,dataBroadLIstVO);
        List<Map<String, Object>> sceneStopTime = sceneMapper.getSceneStopTime(dataBroadVO);
        Map<Integer, Long> sceneStopTimeMap = sceneStopTime.stream().collect(
                Collectors.toMap(x -> Integer.valueOf(x.get("scene").toString()),
                        x -> Long.valueOf(x.get("stopTime").toString())));
        for(ScenDataRepVO p:topExite){
            Integer scene = p.getScene();
            //String sceneName = collect.get(scene);
            //if(StringUtils.isNotBlank(sceneName)){
            //    p.setSceneName(sceneName);
            //}
            dataBroadLIstVO.setScene(scene);
            //Integer topOpen = dataBroadMapper.getTopOpen(dataBroadLIstVO);
            Integer topOpen =p.getOpenCount();
            Long stoptime=sceneStopTimeMap.get(scene)==null?0L:sceneStopTimeMap.get(scene);
            String time = topOpen == 0 ? "00:00:00" : TimeUtils.getGapTime(stoptime/topOpen);
            p.setAvgDate(time);
            p.setOpenCount(topOpen);
            Integer topExitVisNum = dataBroadMapper.getTopExitVisNum(dataBroadLIstVO);
            topExitVisNum=topExitVisNum==null?0:topExitVisNum;
            String percent = DecimalFormatUtils.getPercent(topExitVisNum, topOpen);
            p.setBounceRate(percent);
        }
        return topExite;
    }

    private  List<QRReqVO> getQrData(DataBroadVO dataBroadVO){
        List<QRReqVO>  result=dataBroadMapper.getQrData(dataBroadVO);
        DataBroadLIstVO dataBroadVO1=new DataBroadLIstVO();
        BeanUtils.copyProperties(dataBroadVO,dataBroadVO1);
        Map<String, Long> map=new HashMap<>();
        if(CollectionUtils.isNotEmpty(result)) {
            List<String> list = result.stream().map(QRReqVO::getPage).collect(Collectors.toList());
            dataBroadVO1.setPage(list);
            List<QRReqVO> qrDataNewUser = dataBroadMapper.getQrDataNewUser(dataBroadVO1);
            map = qrDataNewUser.stream().collect(Collectors.toMap(QRReqVO::getPage, QRReqVO::getNewUseridCount));
        }
        for(QRReqVO qrReqVO:result){
            String page = qrReqVO.getPage();
            Long  newUseridCount= map.get(page);
            if(newUseridCount!=null){
                qrReqVO.setNewUseridCount(newUseridCount);
            }else{
                qrReqVO.setNewUseridCount(0l);
            }
        }
        return result;

    }
    
    private TotalDataRepVO getTotal (DataBroadVO dataBroadVO){
        //查询mysql
        TotalDataRepVO totalBean = getTotalBean(dataBroadVO);
        if(totalBean!=null) {
            //访问人数从ck,去重
            List<Map<String, Object>> visNum = dataBroadMapper.getVisNum(dataBroadVO);
            if (CollectionUtils.isNotEmpty(visNum)) {
                BigInteger accUserNum = (BigInteger) visNum.get(0).get("accUserNum");
                totalBean.setUserNum(accUserNum.intValue());//访问人数
            }
        }else{
            //ck
            totalBean = dataBroadMapper.getTotalDataRepVO(dataBroadVO);
            Integer newUser=dataBroadMapper.getTotalnewUser(dataBroadVO);
            newUser=newUser==null?0:newUser;
            totalBean.setNewUserNum(newUser);
        // List<Map<String, Object>> visNum = dataBroadMapper.getVisNum(dataBroadVO);
            Integer  boundPage=dataBroadMapper.getBounce(dataBroadVO);//跳出
//        if(CollectionUtils.isNotEmpty(visNum)){
//            BigInteger accUserNum = (BigInteger) visNum.get(0).get("accUserNum");
//            totalDataRepVO.setUserNum(accUserNum.intValue());//访问人数
//        }
            boundPage=boundPage==null?0 :boundPage;//跳出率
            String percent = DecimalFormatUtils.getPercent(boundPage, totalBean.getOpenNum().intValue());
            totalBean.setBounceRate(percent + "%");
            totalBean.setAvgStopTime(dataService.getStopTime(dataBroadVO));
            //写入mysql
            totalService.insertApiTotal(dataBroadVO.getBeginDate(),dataBroadVO.getEndDate());
        }
        return totalBean;


    }

    private TotalDataRepVO getTotalBean(DataBroadVO dataBroadVO){
        TotalDataRepVO totalDataRepVO=null;
        ToTalPO toTalPO = mysqlMapper.getYesList(dataBroadVO);
        if(toTalPO!=null){
            totalDataRepVO=new TotalDataRepVO();
            totalDataRepVO.setNewUserNum(toTalPO.getNewUser());
            totalDataRepVO.setVisitNum(toTalPO.getPv());
            totalDataRepVO.setUserNum(toTalPO.getUv());
            totalDataRepVO.setOpenNum(toTalPO.getOpenNum());
            Integer boundPage = toTalPO.getBounce();
            boundPage=boundPage==null?0 :boundPage;//跳出率
            String percent = DecimalFormatUtils.getPercent(boundPage, totalDataRepVO.getOpenNum().intValue());
            totalDataRepVO.setBounceRate(percent + "%");
            Integer openNum = toTalPO.getOpenNum();
            String time = openNum == 0 ? "00:00:00" : TimeUtils.getGapTime(toTalPO.getAvgStopTime()/openNum);
            totalDataRepVO.setAvgStopTime(time);
        }
        return  totalDataRepVO;
    }



}
