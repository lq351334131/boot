package com.etocrm.sdk.service.impl;

import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.*;
import com.etocrm.sdk.service.DataBroadService;
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
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DataBroadServiceImpl  implements DataBroadService {

    @Autowired
    private EsUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result portalData(DataBroadVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        DataBroadRepVO dataBroadRepVO = new DataBroadRepVO();
        dataBroadRepVO.setTotalData(getTotalDataRepVO(vo));
        List<PathDataRepVO> pathDataRepVO = countDistinctField(vo);
        dataBroadRepVO.setPathDatas(pathDataRepVO);
        List<PortalDetailDatasRepVO> portalDetailDatas = getPortalDetailDatas(vo);
        dataBroadRepVO.setPortalDetailDatas(portalDetailDatas);
        dataBroadRepVO.setRegDatas(getRegData(vo));
        dataBroadRepVO.setSceneDatas(getSence(vo));
        //qrDatas二维码未完成
        return Result.success(dataBroadRepVO);
    }

    private TotalDataRepVO getTotalDataRepVO(DataBroadVO vo) {
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

    /**
     * @Description 新用户
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getNewUser(DataBroadVO vo) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("isfirstday", true));
        //queryBuilder.must(QueryBuilders.matchQuery("m_newpage", 1));
        queryBuilder.must(QueryBuilders.matchQuery("tv.keyword", "app"));
        queryBuilder.must(QueryBuilders.matchQuery("tl.keyword", "launch"));
        long num = esUtil.groupBy(EsTable.INDEX, "o.keyword", queryBuilder);
        return num;
    }

    /**
     * @Description 访问人数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getUserNum(DataBroadVO vo) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        long num = esUtil.groupBy(EsTable.INDEX, "o.keyword", queryBuilder);
        return num;
    }

    /**
     * @Description 访问次数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getVisitNum(DataBroadVO vo) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        long num = esUtil.count(EsTable.INDEX, queryBuilder);
        return num;
    }

    /**
     * @Description 打开次数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getOpenNum(DataBroadVO vo) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("m_openapp", 1));
        long num = esUtil.count(EsTable.INDEX, queryBuilder);
        return num;
    }

    /*
     *
     * @Description 均停留时长
     * @author xing.liu
     * @date 2020/9/24
     **/
    private String getStopRate(DataBroadVO vo) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
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

    private Long getExitRate(DataBroadVO vo) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("m_exitpage", 1));
        long exitpageNum = esUtil.count(EsTable.INDEX, queryBuilder);
        return exitpageNum;
    }

    /*
     *
     * @Description :count(distinct(*)) 效果
     * @author xing.liu
     * @date 2020/9/24
     **/
    private List<PathDataRepVO> countDistinctField(DataBroadVO dataBroadVO) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(dataBroadVO.getBeginDate().getTime()).lte(dataBroadVO.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", dataBroadVO.getAppKey()));
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(EsTable.INDEX);
        TermsAggregationBuilder terms = AggregationBuilders.terms("getP").field("p.keyword").size(5);
        AggregationBuilder aggregationBuilder = AggregationBuilders.cardinality("disNum").field("o.keyword");
        terms.subAggregation(aggregationBuilder);
        searchSourceBuilder.aggregation(terms);
        searchSourceBuilder.size(0);
        searchSourceBuilder.fetchSource("p", null);
        searchRequest.source(searchSourceBuilder);
        List<PathDataRepVO> result = new ArrayList<>();
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            Map<String, Aggregation> serviceLineMap = response.getAggregations().asMap();
            ParsedStringTerms serviceLineTerms = (ParsedStringTerms) serviceLineMap.get("getP");
            List serviceLists = serviceLineTerms.getBuckets();
            for (Object serviceList : serviceLists) {
                PathDataRepVO pathDataBean = getPathDataBean(serviceList, queryBuilder);
                result.add(pathDataBean);
            }
        } catch (Exception e) {
            log.error("Count dis field failed!{}", e.getMessage());
        }
        return result;
    }

    private PathDataRepVO getPathDataBean(Object serviceList, BoolQueryBuilder queryBuilder) {
        PathDataRepVO pathDataRepVO = new PathDataRepVO();
        ParsedStringTerms.ParsedBucket serviceListObj = (ParsedStringTerms.ParsedBucket) serviceList;
        String path = serviceListObj.getKeyAsString();
        long docCount = serviceListObj.getDocCount();
        pathDataRepVO.setVisitNum(docCount);
        pathDataRepVO.setVisitPath(path);
        Map<String, Aggregation> appNameMap = serviceListObj.getAggregations().asMap();
        Cardinality appNameTerms = (Cardinality) appNameMap.get("disNum");
        long value = appNameTerms.getValue();
        pathDataRepVO.setUserNum(value);
        BoolQueryBuilder res = QueryBuilders.boolQuery();
        BeanUtils.copyProperties(queryBuilder, res);
        res.must(QueryBuilders.matchQuery("p.keyword", path));
        long m_apptime = esUtil.sum(EsTable.INDEX, "m_apptime", res);
        BoolQueryBuilder exitBuild = QueryBuilders.boolQuery();
        BeanUtils.copyProperties(res, exitBuild);
        exitBuild.must(QueryBuilders.matchQuery("m_exitpage", 1));
        Long exitpageNum = esUtil.count(EsTable.INDEX, exitBuild);
        BoolQueryBuilder openBuild = QueryBuilders.boolQuery();
        BeanUtils.copyProperties(res, openBuild);
        openBuild.must(QueryBuilders.matchQuery("m_openapp", 1));
        Long openNum = esUtil.count(EsTable.INDEX, openBuild);
        String percent = DecimalFormatUtils.getPercent(exitpageNum.intValue(), openNum.intValue());
        pathDataRepVO.setExitRate(percent + "%");
        String stopRate = "";
        if (openNum > 0 && m_apptime > 0) {
            Long num = m_apptime / openNum;
            stopRate = TimeUtils.getGapTime(num);
        } else {
            stopRate = "0:00:00";
        }
        pathDataRepVO.setAvgDate(stopRate);
        return pathDataRepVO;
    }

    private List<PortalDetailDatasRepVO> getPortalDetailDatas(DataBroadVO vo) {
        List<PortalDetailDatasRepVO> result = new ArrayList<>();
        Map<String, Long> userMap = getPortalDetailDatasType(vo, 0);
        PortalDetailDatasRepVO portalDetailDatasRepVO = new PortalDetailDatasRepVO();
        userMap.forEach((k, v) -> {
            Map<String, Long> newUserMap = getPortalDetailDatasType(vo, 1);
            Map<String, Long> openNumMap = getPortalDetailDatasType(vo, 2);
            if (newUserMap.size() > 0) {
                Long newUser = newUserMap.get(k);
                portalDetailDatasRepVO.setNewUserNum(newUser);
            }
            if (openNumMap.size() > 0) {
                Long openNum = openNumMap.get(k);
                portalDetailDatasRepVO.setOpenNum(openNum);
            }
            portalDetailDatasRepVO.setDate(k);
            portalDetailDatasRepVO.setUserNum(v);
            result.add(portalDetailDatasRepVO);
        });
        return result;
    }

    /**
     * @Description 新用户-根据日期分组type=1,type=0用户数，type=2打开次数
     * @author xing.liu
     * @date 2020/9/25
     **/
    private Map<String, Long> getPortalDetailDatasType(DataBroadVO vo, Integer type) {
        Map<String, Long> map = new LinkedHashMap<>();
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
            queryBuilder.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
            if (type == 1) {
                queryBuilder.must(QueryBuilders.matchQuery("isfirstday", true));
                queryBuilder.must(QueryBuilders.matchQuery("tv.keyword", "app"));
                queryBuilder.must(QueryBuilders.matchQuery("tl.keyword", "launch"));
            } else if (type == 2) {
                queryBuilder.must(QueryBuilders.matchQuery("m_openapp", 1));
            }
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
            String[] source = {"t", "tv", "tl", "o", "m_newpage", "k", "isfirstday"};
            searchSourceBuilder.fetchSource(source, null);
            log.info(searchSourceBuilder.toString());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Aggregation agg = searchResponse.getAggregations().get("groupDate");
            List<? extends Histogram.Bucket> buckets = ((Histogram) agg).getBuckets();
            for (Histogram.Bucket buck : buckets) {
                String key = buck.getKeyAsString();
                ParsedCardinality count = buck.getAggregations().get("groupO");
                Long value = count.getValue();
                map.put(TimeUtils.getYyyyMMdd(Long.valueOf(key)), value);
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
        return map;
    }

    private List<RegDataRepVO> getRegData(DataBroadVO vo) {
        List<RegDataRepVO> result = new ArrayList<>();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        List<Map<String, Object>> lists = esUtil.groupByMap(EsTable.INDEX, "province.keyword", queryBuilder, 5);
        RegDataRepVO regDataRepVO = null;
        for (Map<String, Object> map : lists) {
            regDataRepVO = new RegDataRepVO();
            String key = (String) map.get("key");
            Long value = (Long) map.get("value");
            regDataRepVO.setRegion(key);
            regDataRepVO.setReginCount(value);
            result.add(regDataRepVO);
        }
        return result;
    }

    private List<ScenDataRepVO> getSence(DataBroadVO dataBroadVO) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(dataBroadVO.getBeginDate().getTime()).lte(dataBroadVO.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", dataBroadVO.getAppKey()));
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(EsTable.INDEX);
        TermsAggregationBuilder terms = AggregationBuilders.terms("getP").field("ao.scene").size(5);
        AggregationBuilder aggregationBuilder = AggregationBuilders.cardinality("disNum").field("o.keyword");
        terms.subAggregation(aggregationBuilder);
        searchSourceBuilder.aggregation(terms);
        searchSourceBuilder.size(0);
        searchSourceBuilder.fetchSource("ao.scene", null);
        searchRequest.source(searchSourceBuilder);
        List<ScenDataRepVO> result = new ArrayList<>();
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            Map<String, Aggregation> serviceLineMap = response.getAggregations().asMap();
            ParsedLongTerms serviceLineTerms = (ParsedLongTerms) serviceLineMap.get("getP");
            List serviceLists = serviceLineTerms.getBuckets();
            for (Object serviceList : serviceLists) {
                ScenDataRepVO pathDataBean = getSenceBean(serviceList, queryBuilder);
                result.add(pathDataBean);
            }
        } catch (Exception e) {
            log.error("Count dis field failed!{}", e.getMessage());
        }
        return result;
    }

    private ScenDataRepVO getSenceBean(Object serviceList,BoolQueryBuilder queryBuilder) {
        ScenDataRepVO  pathDataRepVO=new ScenDataRepVO();
        ParsedLongTerms.ParsedBucket serviceListObj = (ParsedLongTerms.ParsedBucket) serviceList;
        String scene = serviceListObj.getKeyAsString();
        long docCount = serviceListObj.getDocCount();
        pathDataRepVO.setUseridVisitCount(docCount);
        pathDataRepVO.setScene(scene);
        Map<String, Aggregation> appNameMap = serviceListObj.getAggregations().asMap();
        Cardinality appNameTerms = (Cardinality) appNameMap.get("disNum");
        long value = appNameTerms.getValue();
        pathDataRepVO.setUseridCount(value);
        BoolQueryBuilder res= QueryBuilders.boolQuery();
        BeanUtils.copyProperties(queryBuilder,res);
        res.must(QueryBuilders.matchQuery("scene.keyword", scene));
        long m_apptime = esUtil.sum(EsTable.INDEX, "m_apptime", res);
        BoolQueryBuilder exitBuild = QueryBuilders.boolQuery();
        BeanUtils.copyProperties(res, exitBuild);
        exitBuild.must(QueryBuilders.matchQuery("m_exitpage", 1));
        Long exitpageNum = esUtil.count(EsTable.INDEX, exitBuild);
        BoolQueryBuilder openBuild = QueryBuilders.boolQuery();
        BeanUtils.copyProperties(res, openBuild);
        openBuild.must(QueryBuilders.matchQuery("m_openapp", 1));
        Long openNum = esUtil.count(EsTable.INDEX, openBuild);
        pathDataRepVO.setOpenCount(openNum);
        String percent = DecimalFormatUtils.getPercent(exitpageNum.intValue(), openNum.intValue());
        pathDataRepVO.setBounceRate(percent + "%");
        String stopRate="";
        if(openNum>0&& m_apptime>0){
            Long num=m_apptime/openNum;
            stopRate = TimeUtils.getGapTime(num);
        }else{
            stopRate="0:00:00";
        }
        pathDataRepVO.setAvgDate(stopRate);
        return  pathDataRepVO;
    }










}
