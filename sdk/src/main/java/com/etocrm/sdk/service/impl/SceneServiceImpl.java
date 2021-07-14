package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.ScenDataRepVO;
import com.etocrm.sdk.entity.databroad.TotalDataRepVO;
import com.etocrm.sdk.entity.scene.*;
import com.etocrm.sdk.service.SceneService;
import com.etocrm.sdk.util.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@Slf4j
public class SceneServiceImpl implements SceneService {

    @Autowired
    private EsUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result dataGet(SceneVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        SceneRepVO sceneRepVO=new SceneRepVO();
        sceneRepVO.setTotalDataRepVO(getTotalDataRepVO(vo));
        sceneRepVO.setSceneTotalDetailDomain(getSceneTotalDetailDomain(vo));
        sceneRepVO.setChartDetailDomain(getChartDetailDomainRepVO(vo));
        sceneRepVO.setSceneDetailData(getSceneDetailData(vo));
        sceneRepVO.setDateDetailData(getDateDetailData(vo));
        return Result.success(sceneRepVO);
    }

    @Override
    public List<SceneTotalDetailDomainRepVO> getexcel(SceneVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Collections.emptyList();
        List<SceneTotalDetailDomainRepVO> result = getSceneTotalDetailDomain(vo);
        return result;
    }

    private TotalDataRepVO getTotalDataRepVO(SceneVO vo) {
        TotalDataRepVO totalDataRepVO = new TotalDataRepVO();
        totalDataRepVO.setNewUserNum(getNewUser(vo));
        totalDataRepVO.setUserNum(getUserNum(vo));
        totalDataRepVO.setVisitNum(getVisitNum(vo));
        totalDataRepVO.setAvgStopTime(getStopRate(vo));
        Long openNum = getOpenNum(vo);
        Long exitRate = getExitRate(vo);
        totalDataRepVO.setOpenNum(openNum);
        String percent = DecimalFormatUtils.getPercent(exitRate.intValue(), openNum.intValue());
        totalDataRepVO.setExitRate(percent + "%");
        return totalDataRepVO;
    }

    private Long getNewUser(SceneVO vo,String ...scence) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("isfirstday", true));
        queryBuilder.must(QueryBuilders.matchQuery("m_newpage", 1));
        queryBuilder.must(QueryBuilders.matchQuery("tv.keyword", "app"));
        queryBuilder.must(QueryBuilders.matchQuery("tl.keyword", "launch"));
        if(scence.length>0)queryBuilder.must(QueryBuilders.matchQuery("ao.scene",scence[0] ));
        long num = esUtil.groupBy(EsTable.INDEX, "o.keyword", queryBuilder);
        return num;
    }

    /**
     * @Description 访问人数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getUserNum(SceneVO vo,String ...scence) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.boolQuery().must((QueryBuilders.existsQuery("ao.scene"))));
        if(scence.length>0)queryBuilder.must(QueryBuilders.matchQuery("ao.scene",scence[0] ));
        long num = esUtil.groupBy(EsTable.INDEX, "o.keyword", queryBuilder);
        return num;
    }

    /**
     * @Description 访问次数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getVisitNum(SceneVO vo,String ...scence) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        if(scence.length>0)queryBuilder.must(QueryBuilders.matchQuery("ao.scene",scence[0] ));
        long num = esUtil.count(EsTable.INDEX, queryBuilder);
        return num;
    }

    /**
     * @Description 打开次数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getOpenNum(SceneVO vo,String ...scence) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("m_openapp", 1));
        if(scence.length>0)queryBuilder.must(QueryBuilders.matchQuery("ao.scene",scence[0] ));
        long num = esUtil.count(EsTable.INDEX, queryBuilder);
        return num;
    }

    /*
     *
     * @Description 均停留时长
     * @author xing.liu
     * @date 2020/9/24
     **/
    private String getStopRate(SceneVO vo,String ...scence) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        if(scence.length>0)queryBuilder.must(QueryBuilders.matchQuery("ao.scene",scence[0] ));
        long m_apptime = esUtil.sum(EsTable.INDEX, "m_apptime", queryBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("m_openapp", 1));
        Long open = esUtil.count(EsTable.INDEX, queryBuilder);
        String stopRate = "";
        if (open > 0 && m_apptime > 0) {
            stopRate = TimeUtils.getGapTime(m_apptime / open);
        } else {
            stopRate = "0:00:00";
        }
        return stopRate;
    }

    private Long getExitRate(SceneVO vo,String ...scence) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("m_exitpage", 1));
        if(scence.length>0)queryBuilder.must(QueryBuilders.matchQuery("ao.scene",scence[0] ));
        long exitpageNum = esUtil.count(EsTable.INDEX, queryBuilder);
        return exitpageNum;
    }

    private List<SceneTotalDetailDomainRepVO> getSceneTotalDetailDomain(SceneVO vo){
        //grouby scence
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        List<Map<String, Object>> lists = esUtil.groupByMap(EsTable.INDEX, "ao.scene", queryBuilder, Integer.MAX_VALUE);
        List<SceneTotalDetailDomainRepVO> result=new ArrayList<>();
        SceneTotalDetailDomainRepVO sceneTotalDetailDomain=null;
        for (Map<String, Object> map : lists) {
            sceneTotalDetailDomain=new SceneTotalDetailDomainRepVO();
            String  key = (Long)map.get("key")+"";
            sceneTotalDetailDomain.setScene(key);
            sceneTotalDetailDomain.setNewUserNum(getNewUser(vo,key));
            sceneTotalDetailDomain.setUserNum(getUserNum(vo,key));
            sceneTotalDetailDomain.setVisitNum(getVisitNum(vo,key));
            sceneTotalDetailDomain.setAvgStopTime(getStopRate(vo,key));
            Long openNum = getOpenNum(vo,key);
            Long exitRate = getExitRate(vo,key);
            sceneTotalDetailDomain.setOpenNum(openNum);
            String percent = DecimalFormatUtils.getPercent(exitRate.intValue(), openNum.intValue());
            sceneTotalDetailDomain.setExitRate(percent + "%");
            result.add(sceneTotalDetailDomain);
        }
      return result;
    }

    private List<ChartDetailDomainRepVO> getChartDetailDomainRepVO(SceneVO vo){
        //grouby scence
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        List<Map<String, Object>> lists = esUtil.groupByMap(EsTable.INDEX, "ao.scene", queryBuilder, 5);
        List<ChartDetailDomainRepVO> result=new ArrayList<>();
        ChartDetailDomainRepVO chartDetailDomainRepVO=null;
        for (Map<String, Object> map : lists) {
            chartDetailDomainRepVO=new ChartDetailDomainRepVO();
            String  key = (Long)map.get("key")+"";
            chartDetailDomainRepVO.setScene(key);
            chartDetailDomainRepVO.setNewUserNum(getNewUser(vo,key));
            chartDetailDomainRepVO.setUserNum(getUserNum(vo,key));
            chartDetailDomainRepVO.setVisitNum(getVisitNum(vo,key));
            chartDetailDomainRepVO.setAvgStopTime(getStopRate(vo,key));
            Long openNum = getOpenNum(vo,key);
            Long exitRate = getExitRate(vo,key);
            chartDetailDomainRepVO.setOpenNum(openNum);
            String percent = DecimalFormatUtils.getPercent(exitRate.intValue(), openNum.intValue());
            chartDetailDomainRepVO.setExitRate(percent + "%");
            chartDetailDomainRepVO.setDistanceValue(getDistancValue(vo,key));
            result.add(chartDetailDomainRepVO);
        }
        if(result.size()>0) {
            long totalNewUseridCount = result.stream().mapToLong(ChartDetailDomainRepVO::getNewUserNum).sum();
            long totalUseridCount = result.stream().mapToLong(ChartDetailDomainRepVO::getUserNum).sum();
            long totalUseridVisitCount = result.stream().mapToLong(ChartDetailDomainRepVO::getVisitNum).sum();
            long totalTotalSimpleBakCount = result.stream().mapToLong(ChartDetailDomainRepVO::getOpenNum).sum();
            result.forEach(ChartDetailDomainRepVO->{
                ChartDetailDomainRepVO.setTotalNewUseridCount(totalNewUseridCount);
                ChartDetailDomainRepVO.setTotalUseridCount(totalUseridCount);
                ChartDetailDomainRepVO.setTotalUseridVisitCount(totalUseridVisitCount);
                ChartDetailDomainRepVO.setTotalTotalSimpleBakCount(totalTotalSimpleBakCount);
            });
        }
        return result;
    }

    private Long getDistancValue(SceneVO vo,String ...scence) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        if(scence.length>0)queryBuilder.must(QueryBuilders.matchQuery("ao.scene",scence[0] ));
        Long m_apptime = esUtil.sum(EsTable.INDEX, "m_apptime", queryBuilder);
        return m_apptime;
    }

    private List<DateDetailData> getDateDetailData(SceneVO sceneVO){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(sceneVO.getBeginDate().getTime()).lte(sceneVO.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", sceneVO.getAppKey()));
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(EsTable.INDEX);
        TermsAggregationBuilder terms = AggregationBuilders.terms("getP").field("ao.scene").size(Integer.MAX_VALUE);
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("groupDate").field("t")
                .calendarInterval(DateHistogramInterval.DAY).offset("-8h").minDocCount(0);
        terms.subAggregation(dateHistogramAggregationBuilder);
        searchSourceBuilder.aggregation(terms);
        searchSourceBuilder.size(0);
        String []source={"t","ao.scene"};
        searchSourceBuilder.fetchSource(source, null);
        searchRequest.source(searchSourceBuilder);
        List<DateDetailData> result = new ArrayList<>();
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            Map<String, Aggregation> serviceLineMap = response.getAggregations().asMap();
            ParsedLongTerms serviceLineTerms = (ParsedLongTerms) serviceLineMap.get("getP");
            List serviceLists = serviceLineTerms.getBuckets();
            Map<String,Integer> map=new LinkedHashMap<>();
            //groupby scence-date
            for (Object serviceList : serviceLists) {
                Map<String,Integer>  mapManey = getSenceMap(serviceList);
                map.putAll(mapManey);
            }
            map.forEach((k,v)->{
                DateDetailData   dateDetailData=new DateDetailData();
                String scene = k.split("#")[0];
                String date = k.split("#")[1];
                SceneVO  vo=new  SceneVO();
                vo.setAppKey(sceneVO.getAppKey());
                vo.setBeginDate(TimeUtils.getstrDate(date));
                vo.setEndDate(TimeUtils.getstrDate(TimeUtils.addDayone(date)));
                dateDetailData.setScene(scene);
                dateDetailData.setNewUserNum(getNewUser(vo,scene));
                dateDetailData.setUserNum(getUserNum(vo,scene));
                dateDetailData.setVisitNum(getVisitNum(vo,scene));
                dateDetailData.setAvgStopTime(getStopRate(vo,scene));
                dateDetailData.setDate(date);
                dateDetailData.setDistanceValue(getDistancValue(vo,scene));
                Long openNum = getOpenNum(vo, scene);
                dateDetailData.setOpenNum(getOpenNum(vo,scene));
                Long exitRate = getExitRate(vo, scene);
                //dateDetailData.setSceneName();
                String percent = DecimalFormatUtils.getPercent(exitRate.intValue(), openNum.intValue());
                dateDetailData.setExitRate(percent + "%");
                result.add(dateDetailData);

            });


        } catch (Exception e) {
            log.error("Count dis field failed!{}", e.getMessage());
        }
        return  result;
    }

    private Map<String,Integer> getSenceMap(Object serviceList) {
        DateDetailData dateDetailData=new DateDetailData();
        ParsedLongTerms.ParsedBucket serviceListObj = (ParsedLongTerms.ParsedBucket) serviceList;
        String key = serviceListObj.getKeyAsString();//sence
        dateDetailData.setScene(key);
        Map<String, Aggregation> appNameMap = serviceListObj.getAggregations().asMap();
        //groupDate
        Aggregation groupDate = appNameMap.get("groupDate");
        Map<String,Integer> map=new LinkedHashMap<>();
        if(appNameMap.size()==0){
            return map;
        }
        List<? extends Histogram.Bucket> buckets = ((Histogram) groupDate).getBuckets();
        for(Histogram.Bucket buck : buckets){
            String key1 =buck.getKeyAsString();
            String yyyyMMdd = TimeUtils.getYyyyMMdd(Long.valueOf(key1));
            map.put(key+"#"+yyyyMMdd,0);
        }
        return  map;
    }

    private  List<SceneDetailData> getSceneDetailData(SceneVO vo){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        List<Map<String, Object>> lists = esUtil.groupByMap(EsTable.INDEX, "ao.scene", queryBuilder, 5);
        List<SceneDetailData> result=new ArrayList<>();
        SceneDetailData sceneDetailData=null;
        for (Map<String, Object> map : lists) {
            sceneDetailData = new SceneDetailData();
            String key = (Long) map.get("key") + "";
            sceneDetailData.setScene(key);
            result.add(sceneDetailData);
        }
        return result;
    }

}
