package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.dao.DataBroadMapper;
import com.etocrm.sdk.dao.PageMapper;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.page.*;
import com.etocrm.sdk.entity.user.UserDatailVO;
import com.etocrm.sdk.mysqldao.PageDataMapper;
import com.etocrm.sdk.service.DataService;
import com.etocrm.sdk.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PageServiceImpl implements PageService {

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private OvalUtils ovalUtils;

    @Autowired
    private DataService dataService;

    @Autowired
    private PageDataMapper pageDataMapper;

    @Autowired
    private DataBroadMapper dataBroadMapper;



    @Override
    public Result importExcel(MultipartFile file) {
        try {
            if(file==null||file.getOriginalFilename()==null||file.getSize()==0) return Result.error(ResponseCode.Fail);
            List<PageListDownloadVO> excelModelList = ExcelUtils.readExcelWithModel(file.getInputStream(), PageListDownloadVO.class);
            if (CollectionUtils.isNotEmpty(excelModelList)) {
                pageMapper.batchInsert(excelModelList);
            }
        } catch (Exception e) {
            log.error("Excel导入失败", e,e.getMessage());
            return Result.error(ResponseCode.Fail);
        }
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getPageList(PageListVO pageListVO) {
        if (ovalUtils.validatorRequestParam(pageListVO).size() > 0) {
            return Result.error(ResponseCode.PARAMETERS_NULL);
        }
        Integer pageCount = pageMapper.getPageCount(pageListVO);
        PageUtils<PageListResVO> pageUtil = new PageUtils<>(pageListVO.getPageSize(), pageListVO.getPageIndex(), pageCount);
        pageListVO.setPageIndex(pageUtil.getPageIndex());
        if (pageCount > 0) {
            List<PageListResVO> pageListPage = pageMapper.getPageListPage(pageListVO);
            pageUtil.setLists(pageListPage);
        } else {
            pageUtil.setLists(Collections.emptyList());
        }
        return Result.success(pageUtil);
    }

    @Override
    public Result getSinglePage(String id) {
        if (StringUtils.isEmpty(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        JSONObject j = JSON.parseObject(id);
        PageListResVO pageOne = pageMapper.getPageId(j.getString("id"));
        return Result.success(pageOne);
    }

    @Override
    public Result updatePage(PageListResVO pageListResVO) {
        if (ovalUtils.validatorRequestParam(pageListResVO).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        try {
            pageMapper.updatePage(pageListResVO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result deleteId(String id) {
        if (StringUtils.isBlank(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        try {
            JSONObject j = JSON.parseObject(id);
            if(!j.containsKey("id"))return Result.error(ResponseCode.PARAMETERS_NULL);
            pageMapper.deleteId(j.getString("id"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getPageParameterList(PageChannelVO pageChannelVO) {
        if (ovalUtils.validatorRequestParam(pageChannelVO).size() > 0)return Result.error(ResponseCode.PARAMETERS_NULL);
        try {
            Integer pageCount = pageMapper.getPageChannelCount(pageChannelVO);
            PageUtils<PageListResVO> pageUtil = new PageUtils<>(pageChannelVO.getPageSize(), pageChannelVO.getPageIndex(), pageCount);
            pageChannelVO.setPageIndex(pageUtil.getPageIndex());
            pageChannelVO.setPageSize(pageUtil.getPageSize());
            if (pageCount > 0) {
                List<PageChannelListVO> pageListPage = pageMapper.getPageChannelId(pageChannelVO);
                pageUtil.setLists(pageListPage);
            } else {
                pageUtil.setLists(Collections.emptyList());
            }
            return Result.success(pageUtil);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
    }

    @Override
    public Result addChannel(PageChannelAddVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.success(ResponseCode.PARAMETERS_NULL);
        try{
            PageChannelEditVO pageChannelEditVO=new PageChannelEditVO();
            BeanUtils.copyProperties(vo,pageChannelEditVO);
            PageChannelListVO pageChannelList = addEditChannel(pageChannelEditVO);
            pageMapper.insert(pageChannelList);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result editPageParameter(PageChannelEditVO pageChannelEditVO) {
        if(StringUtils.isEmpty(pageChannelEditVO.getId()) || ovalUtils.validatorRequestParam(pageChannelEditVO).size() > 0){
            return Result.success(ResponseCode.PARAMETERS_NULL);
        }
        try{
            PageChannelListVO pageChannelList = addEditChannel(pageChannelEditVO);
            pageMapper.updateChannel(pageChannelList);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getSinglePageParameter(String id) {
        if (StringUtils.isEmpty(id)) return Result.error(ResponseCode.PARAMETERS_NULL);
        JSONObject j = JSON.parseObject(id);
        PageChannelListVO pageChannelListVO = pageMapper.getPageChanelId(j.getString("id"));
        PageChannelEditVO pageChannelEditVo=new PageChannelEditVO();
        List<ChannelVO> list = new ArrayList<>();
        if (pageChannelListVO != null) {
            String params=pageChannelListVO.getParams();
            String[] split = params.split("&");
            ChannelVO c = null;
            for (String s : split) {
                c = new ChannelVO();
                String[] kv = s.split("=");
                String key = kv[0];
                String value = kv[1];
                c.setKey(key);
                c.setValue(value);
                list.add(c);
            }
            pageChannelEditVo.setId(pageChannelListVO.getId());
            pageChannelEditVo.setName(pageChannelListVO.getName());
            pageChannelEditVo.setPathId(pageChannelListVO.getPathId());
            pageChannelEditVo.setList(list);
        }
        return Result.success(pageChannelEditVo);
    }

    @Override
    public Result getVisitPage(PageAccessVo pageAccessVo) {
        if(ovalUtils.validatorRequestParam(pageAccessVo).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
       VisitTotalVO visitPage = pageMapper.getVisitPage(pageAccessVo);
       // VisitTotalVO visitPage=pageDataMapper.getVisitPage(pageAccessVo);
        DataBroadVO dataBroadVO=new DataBroadVO();
        BeanUtils.copyProperties(pageAccessVo,dataBroadVO);
        Integer userNum = dataBroadMapper.getVisUserNum(dataBroadVO);
        visitPage.setPersonNum(userNum);
        String percent = DecimalFormatUtils.getPercent(visitPage.getExitNum(), visitPage.getVisitNum());
        visitPage.setAvgExitRate(percent);
        /*String stopTime = dataService.getStopTime(dataBroadVO);
        visitPage.setAvgStopTime(stopTime);*/
        Long timeNum= pageMapper.getPageTime(pageAccessVo);
        String time="00:00:00";
        if(timeNum!=null&& timeNum>0l&&visitPage.getPersonNum()>0 ){
             time = TimeUtils.getGapTime(timeNum / visitPage.getPersonNum());
        }
        visitPage.setAvgStopTime(time);
        return Result.success(visitPage);
    }

    @Override
    public Result getVisitPageList(VisitPageVO visitPageVO) {
        if(ovalUtils.validatorRequestParam(visitPageVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        try {
            Integer pageAccVisCount = pageMapper.getPageVistListCount(visitPageVO);
            if (pageAccVisCount==null||pageAccVisCount == 0) {
                return Result.success();
            }
            PageUtils<VisitTotalPVO> pageUtils=new PageUtils<>(visitPageVO.getPageSize(),visitPageVO.getPageIndex(),pageAccVisCount);
            List<VisitTotalPVO> homeVOList = pageMapper.getPageVistListNew(visitPageVO);
            StopTimeVO stopTimeVO=null;
            for(VisitTotalPVO visitTotalPVO:homeVOList){
                String  visitPath=visitTotalPVO.getVisitPath();
                stopTimeVO=new StopTimeVO();
                stopTimeVO.setP(visitPath);
                List<Map<String, Object>> timeMapList = pageMapper.getPageshowTotal(stopTimeVO);
                String time="00:00:00";
                if ( CollectionUtils.isNotEmpty(timeMapList)) {
                    Long timeNum = (Long) timeMapList.get(0).get("stopTime");
                    time = TimeUtils.getGapTime(timeNum / visitTotalPVO.getPersonNum());
                }
                visitTotalPVO.setAvgStopTime(time);
                String avgExitRate = visitTotalPVO.getAvgExitRate();
                String percent = DecimalFormatUtils.getPercent(Integer.valueOf(avgExitRate), visitTotalPVO.getVisitNum());
                visitTotalPVO.setAvgExitRate(percent);
            }
            pageUtils.setLists(homeVOList);
            return Result.success(pageUtils);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.success(ResponseCode.CLICKHOUSE_EXCEPTION);
        }

    }

    @Override
    public Result getHomePage(PageAccessVo pageAccessVo) {
        if(ovalUtils.validatorRequestParam(pageAccessVo).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        HomeTotalVO homePage = new HomeTotalVO();
        try {
            pageAccessVo.setType(1);
            homePage = pageMapper.getHomePage(pageAccessVo);
            //入口页时间=时间、入口页
            String averageStopTime = getHomeStopTime(pageAccessVo,homePage.getEntryNum(),null);
            homePage.setAvgStopTime(averageStopTime);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        return Result.success(homePage);
    }



    @Override
    public Result getHomePageList(VisitPageVO visitPageVO) {
        if(ovalUtils.validatorRequestParam(visitPageVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        try {
            Integer pageAccVisCount = pageMapper.getPageAccVisCount(visitPageVO);
            if (pageAccVisCount==null||pageAccVisCount == 0) {
                return Result.success();
            }
            PageUtils<HomeVO> pageUtils=new PageUtils<>(visitPageVO.getPageSize(),visitPageVO.getPageIndex(),pageAccVisCount);
            List<HomeVO> homeVOList = pageMapper.getPageAccVisList(visitPageVO);
            PageAccessVo pageAccessVo=null;
            for(HomeVO homeVO:homeVOList){
                pageAccessVo=new PageAccessVo();
                BeanUtils.copyProperties(visitPageVO,pageAccessVo);
                String averageStopTime =getHomeStopTime(pageAccessVo,homeVO.getEntryNum(),homeVO.getVisitPath());
                homeVO.setAvgStopTime(averageStopTime);
            }
            pageUtils.setLists(homeVOList);
            return Result.success(pageUtils);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.success(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
    }

    @Override
    public List<HomePageExcel> downLoadHomePageList(VisitVO visitVO){
        if(ovalUtils.validatorRequestParam(visitVO).size()>0)return Collections.emptyList();
        List<HomePageExcel> pageListPage = pageMapper.getPageHomeExcelNew(visitVO);
        List<Map<String, Object>> entryTime = pageMapper.getEntryTime(visitVO);
        Map<String, Long> map = entryTime.stream().collect(Collectors.toMap(x -> x.get("path").toString(), x -> Long.valueOf(x.get("stopTime").toString())));
        for(HomePageExcel homePageExcel:pageListPage) {
            String visitPath = homePageExcel.getVisitPath();
            String  time="00:00:00";
            Long stopTime = map.get(visitPath);
            if(stopTime!=null &&stopTime>0)time=TimeUtils.getGapTime(stopTime / homePageExcel.getEntryNum());
            homePageExcel.setAvgStopTime(time);
        }
        return pageListPage;
    }
    @Override
    public   List<VisitPageExcel>  downLoadVisPageList(VisitVO visitVO) {
        if (ovalUtils.validatorRequestParam(visitVO).size() > 0) return Collections.emptyList();
        try {
            StopTimeVO stopTimeVO=new StopTimeVO();
            BeanUtils.copyProperties(visitVO,stopTimeVO);
            List<VisitPageExcel> pageVisExecl = pageMapper.getPageVisExecl(visitVO);
            for (VisitPageExcel visitPageExcel : pageVisExecl) {
                stopTimeVO.setP(visitPageExcel.getVisitPath());
                List<Map<String,Object>> timeMapList=pageMapper.getPageshowTotal( stopTimeVO);
                visitPageExcel.setAvgStopTime("00:00:00");
                if ( CollectionUtils.isNotEmpty(timeMapList)) {
                    Long timeNum = (Long) timeMapList.get(0).get("stopTime");
                    String time = TimeUtils.getGapTime(timeNum / visitPageExcel.getPersonNum());
                    visitPageExcel.setAvgStopTime(time);
                }
            }
            return pageVisExecl;
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public Result getUserList(PageUserVO userVO) {
        if(ovalUtils.validatorRequestParam(userVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
//        Integer pageCount = pageMapper.getUserCount(userVO);
//        PageUtils<UserDatailVO> pageUtil = new PageUtils<>(userVO.getPageSize(), userVO.getPageIndex(), pageCount);
//        userVO.setPageIndex(pageUtil.getPageIndex());
//        if (pageCount > 0) {
//            List<UserDatailVO> pageListPage =  pageMapper.getUserPage(userVO);
//            pageUtil.setLists(pageListPage);
//        } else {
//            pageUtil.setLists(Collections.emptyList());
//        }
        return Result.success();
    }

    @Override
    public List<UserDatailVO> downLoadUserList(PageUserVO userVO) {
        if(ovalUtils.validatorRequestParam(userVO).size()>0)return Collections.emptyList();
        List<UserDatailVO> userPageAll = pageMapper.getUserPageAll(userVO);
        return userPageAll;
    }

    @Override
    public Result getPageVisitHabitFrequency(PageVO pageVO) {
        if(ovalUtils.validatorRequestParam(pageVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        List<PageDepthVO> pageVisitHabitFrequencyList = getPageVisitHabitFrequencyList(pageVO);
        return Result.success(pageVisitHabitFrequencyList);
    }

    @Override
    public List<PageDepthVO> downLoadPageVisitHabitFrequency(PageVO pageVO) {
        if(ovalUtils.validatorRequestParam(pageVO).size()>0)return Collections.emptyList();
        List<PageDepthVO> pageVisitHabitFrequencyList = getPageVisitHabitFrequencyList(pageVO);
        return pageVisitHabitFrequencyList;
    }

    @Override
    public Result getPageVisitHabitDepth(PageVO pageVO) {
        if(ovalUtils.validatorRequestParam(pageVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        List<PageDepthVO> pageVisitHabitDepth = pageMapper.getPageVisitHabitDepth(pageVO);
        Integer total = pageMapper.getPageVisitHabitDepthTotal(pageVO);
        for(PageDepthVO pageDepthVO:pageVisitHabitDepth){
            Integer num = pageDepthVO.getNum();
            pageDepthVO.setRate(total==0?"0.00%":DecimalFormatUtils.getPercent(num,total));
        }
        return Result.success(pageVisitHabitDepth);
    }

    @Override
    public List<PageDepthVO> downLoadPageVisitHabitDepth(PageVO pageVO) {
        if(ovalUtils.validatorRequestParam(pageVO).size()>0)return Collections.emptyList();
        List<PageDepthVO> pageVisitHabitDepth = pageMapper.getPageVisitHabitDepth(pageVO);
        Integer total = pageMapper.getPageVisitHabitDepthTotal(pageVO);
        for(PageDepthVO pageDepthVO:pageVisitHabitDepth){
            Integer num = pageDepthVO.getNum();
            pageDepthVO.setRate(total==0?"0.00%":DecimalFormatUtils.getPercent(num,total));
        }
        return pageVisitHabitDepth;
    }

    @Override
    public Result getPageVisitHabitTime(PageVO pageVO) {
        if(ovalUtils.validatorRequestParam(pageVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        List<PageDepthVO> pageHabitTime = getPageHabitTime(pageVO);
        return Result.success(pageHabitTime);
    }
    @Override
    public List<PageDepthVO> downLoadPageVisitHabitTime(PageVO pageVO) {
        if(ovalUtils.validatorRequestParam(pageVO).size()>0)return Collections.emptyList();
        return getPageHabitTime(pageVO);
    }

    @Override
    public Result getPageNameList() {
        return Result.success(pageMapper.getPageNameList());
    }

    @Override
    public Result getPageParameterDataList(ParamDataSearchVO paramDataSearchVO) {
        if(ovalUtils.validatorRequestParam(paramDataSearchVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        try {
            Integer pageCount = pageMapper.getPageParameterDataCount(paramDataSearchVO);
            if(pageCount==null||pageCount==0){
               return Result.success();
            }
            PageUtils<ParamDataRepVO> pageUtil = new PageUtils<>(paramDataSearchVO.getPageSize(), paramDataSearchVO.getPageIndex(), pageCount);
            paramDataSearchVO.setPageIndex(pageUtil.getPageIndex());
            List<Map<String,Object>> pageListPage = pageMapper.getPageParameterDataList(paramDataSearchVO);
            List<ParamDataRepVO> paramDataRepVO1 = getParamDataRepVO(paramDataSearchVO,pageListPage);
            pageUtil.setLists(paramDataRepVO1);
            return Result.success(pageUtil);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.Fail);
        }
    }

    @Override
    public List<ParamDataRepVO> downloadPageParameterDataList(ParamDataSearchExcelVO paramDataSearchExcelVO) {
        if(ovalUtils.validatorRequestParam(paramDataSearchExcelVO).size()>0) return Collections.emptyList();
        try{
            ParamDataSearchVO paramDataSearchVO=new ParamDataSearchVO();
            BeanUtils.copyProperties(paramDataSearchExcelVO,paramDataSearchVO);
            List<Map<String,Object>> pageListPage = pageMapper.getPageParameterDataExcel(paramDataSearchVO);
            List<ParamDataRepVO> paramDataRepVO1 = getParamDataRepVO(paramDataSearchVO,pageListPage);
            return paramDataRepVO1;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Collections.emptyList();

        }
    }

    @Override
    public Result getPageParameterUserList(PageParameterPageSearchVO pageParameterPageSearchVO) {
        if(ovalUtils.validatorRequestParam(pageParameterPageSearchVO).size() > 0)return Result.success(ResponseCode.PARAMETERS_NULL);
        PageUtils<UserDatailVO> pageUtil = new PageUtils<>(pageParameterPageSearchVO.getPageSize(), pageParameterPageSearchVO.getPageIndex(), 0);
        try {
            Map<String,String> paramOne = pageMapper.getParamOne(pageParameterPageSearchVO);
            if(paramOne==null){
                pageUtil.setLists(Collections.emptyList());
            }else{
                String visitPath = paramOne.get("visitPath");
                pageParameterPageSearchVO.setVisitPath(visitPath);
                String params=paramOne.get("params");
                String[] split = params.split("&");
                StringBuffer stringBuffer=new StringBuffer();
                for (String s : split) {
                    String[] kv = s.split("=");
                    String key = kv[0];
                    String value = kv[1];
                    stringBuffer.append(" and q like  '%"+key+"%'");
                    stringBuffer.append(" and q like  '%"+value+"%'");
                }
                pageParameterPageSearchVO.setSql(stringBuffer.toString());
                Integer pageCount = pageMapper.getSdkParamCount(pageParameterPageSearchVO);
                if (pageCount ==0) {
                    pageUtil.setLists(Collections.emptyList());
                } else {
                    pageUtil = new PageUtils<>(pageParameterPageSearchVO.getPageSize(), pageParameterPageSearchVO.getPageIndex(), pageCount);
                    pageParameterPageSearchVO.setPageIndex(pageUtil.getPageIndex());
                    List<UserDatailVO> sdkParam = pageMapper.getSdkParam(pageParameterPageSearchVO);
                    pageUtil.setLists(sdkParam);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Result.error(ResponseCode.CLICKHOUSE_EXCEPTION);
        }
        return Result.success(pageUtil);
    }

    @Override
    public List<UserDatailVO> downloadPageParameterUserList(PageParameterUserSeaVO pageParameterUserSeaVO) {
        if(ovalUtils.validatorRequestParam(pageParameterUserSeaVO).size()>0) return Collections.emptyList();
        //子
        PageChannelListVO pageChanelId = pageMapper.getPageChanelId(pageParameterUserSeaVO.getId());
        try{
            if(pageChanelId!=null){
                String params = pageChanelId.getParams();
                StringBuffer stringBuffer=new StringBuffer();
                if(StringUtils.isNotBlank(params)){
                    String[] split = params.split("&");
                    for (String s : split) {
                        String[] kv = s.split("=");
                        String key = kv[0];
                        String value = kv[1];
                        stringBuffer.append(" and q like  '%"+key+"%'");
                        stringBuffer.append(" and q like  '%"+value+"%'");
                    }
                }
                String sql = stringBuffer.toString();
                PageListResVO pageId = pageMapper.getPageId(pageParameterUserSeaVO.getPathId());
                if(pageId!=null) {
                    pageParameterUserSeaVO.setVisitpath(pageId.getVisitPath());
                    pageParameterUserSeaVO.setSql(sql);
                    pageParameterUserSeaVO.setContent(pageParameterUserSeaVO.getContent());
                    List<UserDatailVO> pageParameterUserListExcel = pageMapper.getPageParamUserListExcel(pageParameterUserSeaVO);
                    return pageParameterUserListExcel;
                }
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        List<UserDatailVO> userDatailVOS=new ArrayList<>();
        return  userDatailVOS;

    }

    /**
     *
     * @Description
     * @author xing.liu 访问页
     * @date 2021/1/15
     **/
    private  String  getVistListStopTime(String p, VisitPageVO visitPageVO, Long userNum) {
        StopTimeVO stopTimeVO=new StopTimeVO();
        stopTimeVO.setP(p);
        BeanUtils.copyProperties(visitPageVO,stopTimeVO);
        //Integer  openNum=pageMapper.getOpenTime(stopTimeVO);
        String time="00:00:00";
        //if(openNum==null||openNum==0)return time;
        try{
            //Map<String, Object> stopTime = pageMapper.getStopTime(stopTimeVO);
            List<Map<String, Object>> stopTime = pageMapper.getPageshowTotal(stopTimeVO);
            if(stopTime!=null){
                    Long stopSecnds= (Long) stopTime.get(0).get("stopTime");
                    time =TimeUtils.getGapTime(stopSecnds / userNum);
            }
            }catch (Exception e){
                log.error(e.getMessage(),e);
        }
        return time;
    }


    private PageChannelListVO addEditChannel(PageChannelEditVO vo) {
        List<ChannelVO> list = vo.getList();
        String string = "";
        PageChannelListVO channel = new PageChannelListVO();
        BeanUtils.copyProperties(vo, channel);
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i).getKey();
            String value = list.get(i).getValue();
            String param = key + "=" + value;
            if (i == 0) {
                string = param;
            } else {
                string = string + "&" + param;
            }
        }
        channel.setParams(string);
        return channel;
    }

    private  Map<String, PageListResVO>  getPageName(){
        Map<String, PageListResVO> map=new HashMap();
        try {
            List<PageListResVO> pageListPage = pageMapper.getPageList();
            map =pageListPage.stream().collect(Collectors.toMap(PageListResVO::getVisitPath, Function.identity()));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return map;
    }

    private List<PageDepthVO> getPageVisitHabitFrequencyList(PageVO pageVO){
        Integer total = pageMapper.getTotal(pageVO);
        List<Map<String,Object>> pageVisitHabitFrequency = pageMapper.getPageVisitHabitFrequency(pageVO);
        List<PageDepthVO> pageDepthVOList=new ArrayList<>();
        PageDepthVO pageDepthVO=null;
        int lastNum = 0;
        for(Map<String,Object> map:pageVisitHabitFrequency){
            pageDepthVO =new PageDepthVO();
            BigInteger userNum= (BigInteger) map.get("userNum");
            BigInteger num = (BigInteger) map.get("num");
            //10次及10次以下的数据
            if (userNum.intValue() <= 10) {
                String keyName =map.get("userNum")+"次";
                pageDepthVO.setKeyName(keyName);
                pageDepthVO.setNum(num.intValue());
                pageDepthVO.setRate(total==0?"0.00%":DecimalFormatUtils.getPercent(num.intValue(),total));
                pageDepthVOList.add(pageDepthVO);
            } else {
                //汇总10次以上的用户数
                lastNum += num.intValue();
            }
        }
        //若10次以上有用户数，添加一条10次以上的数据
        if (lastNum > 0) {
            pageDepthVO.setKeyName("10次以上");
            pageDepthVO.setNum(lastNum);
            pageDepthVO.setRate(total==0?"0.00%":DecimalFormatUtils.getPercent(lastNum,total));
            pageDepthVOList.add(pageDepthVO);
        }
        return pageDepthVOList;
    }

    private  List<PageDepthVO>  getPageHabitTime(PageVO pageVO){
        List<PageDepthVO> list=new ArrayList<>();
        PageDepthVO pageDepthVO=null;
        try {
            Integer total = pageMapper.getOpen(pageVO);
            List<Map<String, Object>> duration = pageMapper.getDuration();
            for (Map<String, Object> map : duration) {
                pageDepthVO = new PageDepthVO();
                Integer dictValue = (Integer) map.get("dictValue");
                int num = dictValue.intValue();
                String name = (String) map.get("dictName");
                pageDepthVO.setKeyName(name);
                String sql = (String) map.get("sql");
                pageVO.setSql(sql);
                /*if (num == 1) {
                    pageVO.setSql("  where t1 <20");
                } else if (num == 2) {
                    pageVO.setSql("  where t1 >=20 and t1 <=60");
                } else if (num == 3) {
                    pageVO.setSql("  where t1 >60 and t1 <120");
                } else if (num == 4) {
                    pageVO.setSql("  where t1 >120 and t1 <300");
                } else if (num == 5) {
                    pageVO.setSql("  where t1 >600 and t1 <1200");
                } else if (num == 6) {
                    pageVO.setSql("  where t1 >1200 and t1 <1800");
                } else if (num == 7) {
                    pageVO.setSql("  where t1 >1800 and t1 <2400");
                } else if (num == 8) {
                    pageVO.setSql("  where t1 >2400 and t1 <3600");
                } else if (num == 9) {
                    pageVO.setSql("  where t1 >3600 and t1 <7200");
                } else if (num == 10) {
                    pageVO.setSql("  where t1 >7200");
                }*/
                Integer pageVisitHabitTime = pageMapper.getPageVisitHabitTime(pageVO);
                pageDepthVO.setNum(pageVisitHabitTime==null?0 :pageVisitHabitTime);
                pageDepthVO.setRate(total == 0 ? "0.00%" : DecimalFormatUtils.getPercent(pageVisitHabitTime, total));
                list.add(pageDepthVO);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return list;

    }

    /**
     *
     * @Description 数据组装q,变成k=t&
     * @author xing.liu
     * @date 2020/12/1
     **/
    private List<Map<String,Object>> getParamList(ParamDataSearchVO paramDataSearchVO){
        List<Map<String, Object>> paramSdk = pageMapper.getParamSdk(paramDataSearchVO);
        List<Map<String, Object>> paramSdkList=new ArrayList<>();
        for(Map<String, Object> qMap:paramSdk){
            String q= (String) qMap.get("q");
            String p= (String) qMap.get("p");
            String uu= (String) qMap.get("uu");
            String b= (String) qMap.get("b");
            String tv= (String) qMap.get("tv");
            String tl= (String) qMap.get("tl");


            if(StringUtils.isNotBlank(q)){
                JSONArray array = JSON.parseArray(q);
                StringBuffer stringBuffer=new StringBuffer();
                Map<String, Object> paramString=new HashMap<>();
                for(int i=0;i<array.size();i++) {
                    String k = array.getJSONObject(i).getString("k");
                    String v = array.getJSONObject(i).getString("v");
                    if(stringBuffer.toString().length()==0){
                        stringBuffer.append(k+"="+v);
                    }else{
                        stringBuffer.append("&"+k+"="+v);
                    }
                    paramString.put("params",stringBuffer.toString());
                    paramString.put("p",p);
                    paramString.put("uu",uu);
                    paramString.put("b",b);
                    paramString.put("tv",tv);
                    paramString.put("tl",tl);
                    paramSdkList.add(paramString);
                }
            }

        }
        return paramSdkList;
    }


   /**
    *
    * @Description 入口页总时间头部
    * @author xing.liu
    * @date 2021/1/15
    **/
    private String getHomeStopTime(PageAccessVo pageAccessVo, Integer visitNum, String path) {
        String time="00:00:00";
        if(visitNum==null||visitNum==0)return time;
        StopTimeVO stopTimeVO=new StopTimeVO();
        BeanUtils.copyProperties(pageAccessVo,stopTimeVO);
        if(StringUtils.isNotBlank(path)){
            stopTimeVO.setPath(path);
        }
        try{
           //Integer stopTime = pageMapper.getHomeStopTime(stopTimeVO);
            stopTimeVO.setPath(path);
            Long timeNum = pageMapper.getPageshowHomeTotal(stopTimeVO);
            if (timeNum!=null&&timeNum>0) {
                time = TimeUtils.getGapTime(timeNum / visitNum);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return time;

    }

    private List<ParamDataRepVO> getParamDataRepVO(ParamDataSearchVO paramDataSearchVO, List<Map<String,Object>> pageListPage){
        ParamDataRepVO paramDataRepVO=null;
        List<ParamDataRepVO> paramDataRepVOS=new ArrayList<>();
        for(Map<String,Object> map1:pageListPage) {
            //String visitPath = (String) map1.get("visitpath");
            paramDataRepVO = new ParamDataRepVO();
            String id = (String) map1.get("id");
            String name = (String) map1.get("name");
            paramDataRepVO.setId(id);
            paramDataRepVO.setName(name);
            BigInteger visitNum = (BigInteger) map1.get("visitNum");
            BigInteger userNum = (BigInteger) map1.get("userNum");
            BigInteger exiNum = (BigInteger) map1.get("exiNum");
            paramDataRepVO.setPersonNum(userNum.intValue());
            paramDataRepVO.setVisitNum(visitNum.intValue());
            paramDataRepVO.setAvgExitRate(DecimalFormatUtils.getPercent(exiNum.intValue(),paramDataRepVO.getVisitNum()));
            paramDataRepVOS.add(paramDataRepVO);
        }
        return paramDataRepVOS;

    }

    private  List<String>  getParams(String params){
        List<String> list=new ArrayList<>();
        if(StringUtils.isNotBlank(params)){
            String[] split = params.split("&");
            for(String s:split){
                String[] params1 = s.split("=");
                for(String param:params1){
                    list.add(param);
                }
            }
        }
        return  list;
    }



}
