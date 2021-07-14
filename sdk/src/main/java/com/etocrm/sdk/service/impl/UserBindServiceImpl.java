package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.*;
import com.etocrm.sdk.service.UserBindService;
import com.etocrm.sdk.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserBindServiceImpl  implements UserBindService {

    @Autowired
    private EsUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result getUserBindDataList(UserDataVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        List<UserDataRepVO> result=new ArrayList<>();
        //新用户page.show
        UserDataRepVO  userDataRepVO=new UserDataRepVO();
        userDataRepVO.setItem1(getNewUser(vo,1));
        userDataRepVO.setItem2(getNewUser(vo,0));
        userDataRepVO.setItem3(getUser(vo,0));
        userDataRepVO.setItem4(getUser(vo,1));
        //新增会员数
        userDataRepVO.setItem5(getNewUser(vo,2));
        //净增会员数 = 新增会员数 - 解绑会员数(未完成)张锋哪里不知道
        //解绑会员数（未完成）张锋哪里不知道

        return Result.success(userDataRepVO);
    }

    @Override
    public Result getUserBindDataTotal(UserDataVO vo) {
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        List<UserDataRepVO> result=new ArrayList<>();
        //新用户page.show
        TotalVO accUserNum = getNewUserNum(vo, 0);
        TotalVO newUserNum = getNewUserNum(vo, 1);
        TotalVO newRegister = getNewUserNum(vo, 2);
        TotalVO oldRegister = getNewUserNum(vo, 3);
        List<TotalVO> list=new ArrayList<>();
        list.add(accUserNum);
        list.add(newUserNum);
        list.add(oldRegister);
        list.add(newRegister);
        list.add(oldRegister);
        list.add(getAccOrOpen(vo,0));
        list.add(getAccOrOpen(vo,1));
        list.add(getAccOrOpen(vo,2));
        TotalVO bind = getBind(newRegister, newUserNum);
        list.add(bind);
        return Result.success(list);
    }

    @Override
    public Result getUserVisitData(List<YestDayVO> vo) {
      List<YestDayRepVO> result=new ArrayList<>();
      YestDayRepVO yestDayRepVO=null;
      for(YestDayVO yestDay:vo){
          yestDayRepVO=new YestDayRepVO();
          String appName = yestDay.getAppName();
          yestDayRepVO.setName(appName);
          yestDayRepVO.setUserNum(getYesrdayUser(1,yestDay));
          yestDayRepVO.setNewUserNum(getYesrdayUser(0,yestDay));
          yestDayRepVO.setVisitNum(getYestDayTimes(1,yestDay));
          Long open= getYestDayTimes(0,yestDay);
          yestDayRepVO.setOpenNum(open);
          Long yestDayExitTimes = getYestDayTimes(2, yestDay);
          String percent = DecimalFormatUtils.getPercent(yestDayExitTimes.intValue(), open.intValue());
          yestDayRepVO.setExitRate(percent+"%");
          yestDayRepVO.setAvgStopTime(getYesDayRate(yestDay));
          result.add(yestDayRepVO);
      }
      return Result.success(result);
    }

    /**
     *
     * @Description 新增会员数、访问人数
     * @author xing.liu
     * @date 2020/9/23
     **/
    private SubItemsRepVO getNewUser(UserDataVO vo, Integer  type){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword",vo.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        //访问人数type=0
        if(type==2){
            //新增会员
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday",true));
            queryBuilder.must(QueryBuilders.matchQuery("m_newpage",1));
            queryBuilder.must(QueryBuilders.matchQuery("tv.keyword","page"));
            queryBuilder.must(QueryBuilders.matchQuery("tl.keyword","show"));
        }else if(type==1){
           //新用户数-首次访问小程序页面的用户数，同一用户多次访问不重复计,isfirstday=1，app.launch
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday",true));
            queryBuilder.must(QueryBuilders.matchQuery("tv.keyword","app"));
            queryBuilder.must(QueryBuilders.matchQuery("tl.keyword","launch"));
        }
        SubItemsRepVO subItemsRepVO=new SubItemsRepVO();
        List<DataRepVO> subItems=new ArrayList<>();
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.query(queryBuilder);
            //o去重
            CardinalityAggregationBuilder groupO = AggregationBuilders.cardinality("groupO").field("o.keyword");
            //dateHistogramInterval过时
            DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("groupDate").field("t")
                    .calendarInterval(DateHistogramInterval.DAY).offset("-8h").minDocCount(0);
            SearchRequest searchRequest = new SearchRequest(EsTable.INDEX);
            dateHistogramAggregationBuilder.subAggregation(groupO);
            searchSourceBuilder.aggregation(dateHistogramAggregationBuilder);
            //返回指定字段
            String []source={"t","tv","tl","o","m_newpage","k","isfirstday"};
            searchSourceBuilder.fetchSource(source,null);
            log.info(searchSourceBuilder.toString());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregation agg = searchResponse.getAggregations().get("groupDate");
            List<? extends Histogram.Bucket> buckets = ((Histogram) agg).getBuckets();
            DataRepVO  dataRepVO=null;
            for(Histogram.Bucket buck : buckets) {
                dataRepVO=new DataRepVO();
                String key = buck.getKeyAsString();
                ParsedCardinality count = buck.getAggregations().get("groupO");
                Long  value=count.getValue();
                dataRepVO.setDate(TimeUtils.getYyyyMMdd(Long.valueOf(key)));
                dataRepVO.setValue(value);
                subItems.add(dataRepVO);
            }
            subItemsRepVO.setSubItems(subItems);
        }catch (Exception e){
            log.error("{}",e.getMessage());
            subItemsRepVO.setSubItems(new ArrayList<DataRepVO>());
        }
        return  subItemsRepVO;
    }

    /**
     *
     * @Description 访问次数、打开次数
     * @author xing.liu
     * @date 2020/9/23
     **/
    private  SubItemsRepVO  getUser(UserDataVO vo,Integer type){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword",vo.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));

        if(type==1){
            //打开次数
            queryBuilder.must(QueryBuilders.matchQuery("m_openapp",1));
        }
        SubItemsRepVO subItemsRepVO=new SubItemsRepVO();
        List<DataRepVO> subItems=new ArrayList<>();
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.query(queryBuilder);
            //dateHistogramInterval过时
            DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("groupDate").field("t")
                    .calendarInterval(DateHistogramInterval.DAY).offset("-8h").minDocCount(0);
            SearchRequest searchRequest = new SearchRequest(EsTable.INDEX);
            searchSourceBuilder.aggregation(dateHistogramAggregationBuilder);
            //返回指定字段
            String []source={"t"};
            searchSourceBuilder.fetchSource(source,null);
            log.info(searchSourceBuilder.toString());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregation agg = searchResponse.getAggregations().get("groupDate");
            List<? extends Histogram.Bucket> buckets = ((Histogram) agg).getBuckets();
            DataRepVO  dataRepVO=null;
            for(Histogram.Bucket buck : buckets) {
                dataRepVO=new DataRepVO();
                String key = buck.getKeyAsString();
                Long  value=buck.getDocCount();
                dataRepVO.setDate(TimeUtils.getYyyyMMdd(Long.valueOf(key)));
                dataRepVO.setValue(value);
                subItems.add(dataRepVO);
            }
            subItemsRepVO.setSubItems(subItems);
        }catch (Exception e){
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            log.error("异常抛出异常行号{},信息{}",stackTraceElement.getLineNumber(),e.getMessage());
        }
        return  subItemsRepVO;
    }

    private TotalVO getNewUserNum(UserDataVO  userDataVO,Integer type){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword",userDataVO.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").from(userDataVO.getBeginDate().getTime()).to(userDataVO.getEndDate().getTime()));
        TotalVO vo=new TotalVO();
        //访问人数type=0
        if(type==2){
            //新增会员
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday",true));
            queryBuilder.must(QueryBuilders.matchQuery("m_newpage",1));
            queryBuilder.must(QueryBuilders.matchQuery("tv.keyword","page"));
            queryBuilder.must(QueryBuilders.matchQuery("tl.keyword","show"));
            vo.setName("新增会员");
        }else if(type==1){
            //新用户数-首次访问小程序页面的用户数，同一用户多次访问不重复计,isfirstday=1，app.launch
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday",true));
            queryBuilder.must(QueryBuilders.matchQuery("tv.keyword","app"));
            queryBuilder.must(QueryBuilders.matchQuery("tl.keyword","launch"));
            vo.setName("新用户数");
        }else if (type==0){
            vo.setName("访问人数");
        }else if(type==3){
            vo.setName("累计会员数");
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday",false));
        }
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.query(queryBuilder);
            //o去重
            CardinalityAggregationBuilder groupO = AggregationBuilders.cardinality("groupO")
                    .field("o.keyword");


            SearchRequest searchRequest = new SearchRequest(EsTable.INDEX);
            searchSourceBuilder.aggregation(groupO);
            //返回指定字段
            String []source={"t","tv","tl","o","m_newpage"};
            searchSourceBuilder.fetchSource(source,null);
            log.info(searchSourceBuilder.toString());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Aggregation countName = aggregations.get("groupO");
            long i = ((ParsedCardinality) countName).getValue();
            vo.setValue(i+"");

        }catch (Exception e){
            log.error("{}",e.getMessage());
        }

        return  vo;
    }
    private  TotalVO  getAccOrOpen(UserDataVO userDataVO,Integer type){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword",userDataVO.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").from(userDataVO.getBeginDate().getTime()).to(userDataVO.getEndDate().getTime()));
        TotalVO vo=new TotalVO();
        long count=0l;
        if(type==0){
            //打开次数
            queryBuilder.must(QueryBuilders.matchQuery("m_openapp",1));
            vo.setName("打开次数");
            count = esUtil.count(EsTable.INDEX, queryBuilder);
            vo.setValue(count+"");
        }else if(type==1){
            vo.setName("访问次数");
            count = esUtil.count(EsTable.INDEX, queryBuilder);
            vo.setValue(count+"");
        }else if(type==2){
            vo.setName("次均停留时长");
            count = esUtil.sum(EsTable.INDEX, "m_apptime", queryBuilder);
            queryBuilder.must(QueryBuilders.matchQuery("m_openapp",1));
            Long open=esUtil.count(EsTable.INDEX, queryBuilder);
            String stopRate="";
            if(open>0&& count>0){
                Long num=count/open;
                stopRate = TimeUtils.getGapTime(num);
            }else{
                stopRate="0:00:00";
            }
            vo.setValue(stopRate+"");
        }
        return  vo;
    }

    /*
     *
     * @Description 绑定率= 新增会员数 / 新用户数
     * @author xing.liu
     * @date 2020/9/23
     **/
    private  TotalVO  getBind(TotalVO newRegister,TotalVO newUserNum){
        TotalVO totalVO=new TotalVO();
        totalVO.setName("绑定率");
        if(StringUtils.isNotBlank(newRegister.getValue())&& StringUtils.isNotBlank(newUserNum.getValue())){
            Integer newReg = Integer.valueOf(newRegister.getValue());
            Integer newUser=Integer.valueOf(newUserNum.getValue());
            String percent = DecimalFormatUtils.getPercent(newReg, newUser);
            totalVO.setValue(percent+"%");
        }else{
            totalVO.setValue("0.00%");
        }
        return  totalVO;
    }

    /**
     * 
     * @Description :昨日访问人数、昨日新用户
     * @author xing.liu
     * @date 2020/9/24
     **/
    private  Long  getYesrdayUser(Integer type,YestDayVO yestDayVO){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword",yestDayVO.getAppKey()));
        if(type==0){
            //新用户
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday",true));
            //queryBuilder.must(QueryBuilders.matchQuery("m_newpage",1));
            queryBuilder.must(QueryBuilders.matchQuery("tv.keyword","app"));
            queryBuilder.must(QueryBuilders.matchQuery("tl.keyword","lanuc"));
        }else  if(type==1){
            //昨日访问人数
        }
        Date beginTime = TimeUtils.getstrDate(TimeUtils.subDay());
        Date endTime = TimeUtils.getstrDate(TimeUtils.getDate());
        queryBuilder.must(QueryBuilders.rangeQuery("t").gte(beginTime.getTime()).lt(endTime.getTime()));
        try{
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.query(queryBuilder);
            //o去重
            CardinalityAggregationBuilder groupO = AggregationBuilders.cardinality("groupO")
                    .field("o.keyword");
            SearchRequest searchRequest = new SearchRequest(EsTable.INDEX);
            searchSourceBuilder.aggregation(groupO);
            //返回指定字段
            String []source={"t","tv","tl","o","m_newpage"};
            searchSourceBuilder.fetchSource(source,null);
            log.info(searchSourceBuilder.toString());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Aggregation countName = aggregations.get("groupO");
            long i = ((ParsedCardinality) countName).getValue();
            return i;
        }catch (Exception e){
            log.error("{}",e.getMessage());
            return 0L;
        }
    }

    /**
     *
     * @Description 打开次数、访问次数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getYestDayTimes(Integer type,YestDayVO yestDayVO){
        Date beginTime = TimeUtils.getstrDate(TimeUtils.subDay());
        Date endTime = TimeUtils.getstrDate(TimeUtils.getDate());
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword",yestDayVO.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").gte(beginTime.getTime()).lt(endTime.getTime()));
        if(type==0){
            //打开次数
            queryBuilder.must(QueryBuilders.matchQuery("m_openapp",1));
        }else if(type==1){
            //访问次数
        }else  if(type==2){
            queryBuilder.must(QueryBuilders.matchQuery("m_exitpage",1));
        }
        long count = esUtil.count(EsTable.INDEX, queryBuilder);
        if(count>=0){
            return count;
        }
        log.error("es查询异常");
        return 0l ;

    }

   /**
    * @Description :昨日均停留时长
    * @author xing.liu
    * @date 2020/9/24
    **/
    private  String   getYesDayRate(YestDayVO yestDayVO){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword",yestDayVO.getAppKey()));
        Date beginTime = TimeUtils.getstrDate(TimeUtils.subDay());
        Date endTime = TimeUtils.getstrDate(TimeUtils.getDate());
        queryBuilder.must(QueryBuilders.rangeQuery("t").gte(beginTime.getTime()).lt(endTime.getTime()));
        long m_apptime = esUtil.sum(EsTable.INDEX, "m_apptime", queryBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("m_openapp",1));
        Long open=esUtil.count(EsTable.INDEX, queryBuilder);
        String stopRate="";
        if(open>0&& m_apptime>0){
            Long num=m_apptime/open;
            stopRate = TimeUtils.getGapTime(num);
        }else{
            stopRate="0:00:00";
        }
        return stopRate;
    }






}
