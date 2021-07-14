package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.RegDataRepVO;
import com.etocrm.sdk.entity.databroad.TotalDataRepVO;
import com.etocrm.sdk.entity.user.*;
import com.etocrm.sdk.service.UserService;
import com.etocrm.sdk.util.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private EsUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result getUserStatisticsSummary(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
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
        return Result.success(totalDataRepVO);
    }

    @Override
    public Result getUserStatisticsDateDate(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Integer> map = groupDate(vo);
        List<UserDateRepVO> result = getDateBean(map, vo);
        return Result.success(result);
    }

    @Override
    public List<UserDateRepVO> getexcel(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Collections.emptyList();
        Map<String, Integer> map = groupDate(vo);
        List<UserDateRepVO> result = getDateBean(map, vo);
        return result;
    }

    @Override
    public Result getUserRegionData(UserVO vo, String field) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        List<Map<String, Object>> lists = esUtil.groupByMap(EsTable.INDEX, field + ".keyword", queryBuilder, Integer.MAX_VALUE);
        List<UserRegionRepVO> result = getUserstatics(lists, vo, field);
        return Result.success(result);
    }

    @Override
    public List<UserRegionRepVO> getUserExcelData(UserVO vo, String field) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Collections.emptyList();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").from(vo.getBeginDate().getTime()).to(vo.getEndDate().getTime()));
        List<Map<String, Object>> lists = esUtil.groupByMap(EsTable.INDEX, field + ".keyword", queryBuilder, Integer.MAX_VALUE);
        List<UserRegionRepVO> result = getUserstatics(lists, vo, field);
        return result;
    }

    @Override
    public Result userActiveStatisticsChart(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Integer> map = groupDate(vo);
        List<UserActiveRepVO> result = getActiveBean(map, vo);
        return Result.success(result);
    }

    @Override
    public List<UserActiveRepVO> getUserActiveexcel(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Collections.emptyList();
        Map<String, Integer> map = groupDate(vo);
        List<UserActiveRepVO> result = getActiveBean(map, vo);
        return result;
    }

    @Override
    public Result userActiveStatisticsTable(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Integer> map = groupDate(vo);
        List<UserActiveRepVO> result = getActiveBean(map, vo);
        return Result.success(result);
    }

    @Override
    public Result getUserNewAddRetentionTableAndChartData(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Integer> map = groupDate(vo, 1);
        List<UserNewAddRepVO> list = getUserNewAdd(map, vo, 1);
        return Result.success(list);
    }

    @Override
    public Result getUserActiveRetentionTableAndChartData(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Integer> map = groupDate(vo);
        List<UserNewAddRepVO> list = getUserNewAdd(map, vo);
        return Result.success(list);
    }

    @Override
    public List<UserNewAddRepVO> getUserNewAddExcel(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Collections.emptyList();
        Map<String, Integer> map = groupDate(vo, 1);
        List<UserNewAddRepVO> list = getUserNewAdd(map, vo, 1);
        return list;
    }

    @Override
    public List<UserNewAddRepVO> getUserNewActiveExcel(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Collections.emptyList();
        Map<String, Integer> map = groupDate(vo);
        List<UserNewAddRepVO> list = getUserNewAdd(map, vo);
        return list;
    }

    @Override
    public Result getUserBackflowRetentionTableAndChartData(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Integer> map = groupDate(vo);
        List<LostReturnRepVO> lostandReturn = getLostandReturn(map, vo);
        return Result.success(lostandReturn);
    }

    @Override
    public List<LostReturnRepVO> getUserBackflowRetentionExcel(UserVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Collections.emptyList();
        Map<String, Integer> map = groupDate(vo);
        List<LostReturnRepVO> lostandReturn = getLostandReturn(map, vo);
        return lostandReturn;
    }

    @Override
    public Result getUser(UserLostTypeVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        List<UserLostReturnRepVO> result=new ArrayList();
        String date = vo.getDate();
        UserVO userVO = new UserVO();
        userVO.setBeginDate(TimeUtils.getstrDate(date));
        userVO.setEndDate(TimeUtils.getstrDate(TimeUtils.addDayone(date, 1)));
        userVO.setAppKey(vo.getAppKey());
        Map<String, Object> userNumMap = getUser(userVO);
        Map<String, Object> lostuser = null;
        if (vo.getType() == 1) {
            lostuser = getLost(userVO, date, userNumMap);
        } else if (vo.getType() == 2) {
            lostuser = getuserReturn(userVO, date, userNumMap);
        }
        long totalNum = lostuser.size();
        if(totalNum==0)return  Result.success(result);
        long count = totalNum % vo.getPageSize() == 0 ? totalNum / vo.getPageSize() : totalNum / vo.getPageSize() + 1;
        if (vo.getPageIndex() != null) {
            if (vo.getPageIndex() < 1) {
                vo.setPageIndex(1);
            }
            if (vo.getPageIndex() >= count) {
                int i = (int) count;
                vo.setPageIndex(i);
            }
        } else {
            vo.setPageIndex(1);
        }
        Integer index = vo.getPageIndex() - 1;
        Integer i = index * vo.getPageSize();
        List<String> list = new ArrayList<>();
        lostuser.forEach((k, v) -> {
            list.add(k);
        });
        List<String> strings = list.subList(i, i + vo.getPageSize());
        strings.forEach(str->{
            String  s=str;
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(1);
            searchSourceBuilder.query(QueryBuilders.matchQuery("o.keyword",str));
            String [] string={"o","u","nickName","uu"};
            searchSourceBuilder.fetchSource(string,null);
            SearchRequest searchRequest = new SearchRequest(EsTable.INDEX);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = searchResponse.getHits();
                SearchHit[] hits1 = hits.getHits();
                for(SearchHit hit:hits1){
                    UserLostReturnRepVO u=JSONObject.parseObject(hit.getSourceAsString(),UserLostReturnRepVO.class);
                    result.add(u);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return  Result.success(result);
    }

    private Long getNewUser(UserVO vo, String... field) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("isfirstday", true));
        //queryBuilder.must(QueryBuilders.matchQuery("m_newpage", 1));
        queryBuilder.must(QueryBuilders.matchQuery("tv.keyword", "app"));
        queryBuilder.must(QueryBuilders.matchQuery("tl.keyword", "launch"));
        if (field.length > 0) {
            String key = field[0];
            String value = field[1];
            queryBuilder.must(QueryBuilders.matchQuery(key, value));
        }
        long num = esUtil.groupBy(EsTable.INDEX, "o.keyword", queryBuilder);
        return num;
    }

    /**
     * @Description 访问人数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getUserNum(UserVO vo, String... field) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("tv.keyword", "page"));
        queryBuilder.must(QueryBuilders.matchQuery("tl.keyword", "show"));
        if (field.length > 0) {
            String key = field[0];
            String value = field[1];
            queryBuilder.must(QueryBuilders.matchQuery(key, value));
        }
        long num = esUtil.groupBy(EsTable.INDEX, "o.keyword", queryBuilder);
        return num;
    }

    /**
     * @Description 访问次数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getVisitNum(UserVO vo, String... field) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        if (field.length > 0) {
            String key = field[0];
            String value = field[1];
            queryBuilder.must(QueryBuilders.matchQuery(key, value));
        }
        long num = esUtil.count(EsTable.INDEX, queryBuilder);
        return num;
    }

    /**
     * @Description 打开次数
     * @author xing.liu
     * @date 2020/9/24
     **/
    private Long getOpenNum(UserVO vo, String... field) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("m_openapp", 1));
        if (field.length > 0) {
            String key = field[0];
            String value = field[1];
            queryBuilder.must(QueryBuilders.matchQuery(key, value));
        }
        long num = esUtil.count(EsTable.INDEX, queryBuilder);
        return num;
    }

    /*
     *
     * @Description 均停留时长
     * @author xing.liu
     * @date 2020/9/24
     **/
    private String getStopRate(UserVO vo, String... field) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        long m_apptime = esUtil.sum(EsTable.INDEX, "m_apptime", queryBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("m_openapp", 1));
        if (field.length > 0) {
            String key = field[0];
            String value = field[1];
            queryBuilder.must(QueryBuilders.matchQuery(key, value));
        }
        Long open = esUtil.count(EsTable.INDEX, queryBuilder);
        String stopRate = "";
        if (open > 0 && m_apptime > 0) {
            stopRate = TimeUtils.getGapTime(m_apptime / open);
        } else {
            stopRate = "0:00:00";
        }
        return stopRate;
    }

    private Long getExitRate(UserVO vo, String... field) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.matchQuery("m_exitpage", 1));
        if (field.length > 0) {
            String key = field[0];
            String value = field[1];
            queryBuilder.must(QueryBuilders.matchQuery(key, value));
        }
        long exitpageNum = esUtil.count(EsTable.INDEX, queryBuilder);
        return exitpageNum;
    }

    private Map<String, Integer> groupDate(UserVO vo, Integer... type) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lte(vo.getEndDate().getTime());
        if (type.length > 0) {
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday", true));
            queryBuilder.must(QueryBuilders.matchQuery("tv.keyword", "app"));
            queryBuilder.must(QueryBuilders.matchQuery("tl.keyword", "launch"));
        }
        queryBuilder.must(timeBuilder);
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(EsTable.INDEX);
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("groupDate").field("t")
                .calendarInterval(DateHistogramInterval.DAY).offset("-8h").minDocCount(0);
        searchSourceBuilder.aggregation(dateHistogramAggregationBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        Map<String, Integer> map = new LinkedHashMap<>();
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            Map<String, Aggregation> serviceLineMap = response.getAggregations().asMap();
            Aggregation groupDate = serviceLineMap.get("groupDate");
            List<? extends Histogram.Bucket> buckets = ((Histogram) groupDate).getBuckets();

            for (Histogram.Bucket buck : buckets) {
                String key1 = buck.getKeyAsString();
                String yyyyMMdd = TimeUtils.getYyyyMMdd(Long.valueOf(key1));
                map.put(yyyyMMdd, 0);
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());

        }
        return map;
    }

    private List<UserDateRepVO> getDateBean(Map<String, Integer> map, UserVO vo) {
        List<UserDateRepVO> result = new ArrayList<>();
        map.forEach((k, v) -> {
            String date = k;
            UserVO userVO = new UserVO();
            userVO.setAppKey(vo.getAppKey());
            userVO.setBeginDate(TimeUtils.getstrDate(date));
            userVO.setEndDate(TimeUtils.getstrDate(TimeUtils.addDayone(date)));
            UserDateRepVO userDateRepVO = new UserDateRepVO();
            userDateRepVO.setNewUserNum(getNewUser(userVO));
            userDateRepVO.setUserNum(getUserNum(userVO));
            userDateRepVO.setVisitNum(getVisitNum(userVO));
            userDateRepVO.setAvgStopTime(getStopRate(userVO));
            Long openNum = getOpenNum(userVO);
            Long exitRate = getExitRate(userVO);
            userDateRepVO.setOpenNum(openNum);
            String percent = DecimalFormatUtils.getPercent(exitRate.intValue(), openNum.intValue());
            userDateRepVO.setExitRate(percent + "%");
            userDateRepVO.setDate(date);
            result.add(userDateRepVO);
        });
        return result;
    }

    private List<UserRegionRepVO> getUserstatics(List<Map<String, Object>> lists, UserVO vo, String field) {
        List<UserRegionRepVO> result = new ArrayList<>();
        UserRegionRepVO regDataRepVO = null;
        for (Map<String, Object> map : lists) {
            regDataRepVO = new UserRegionRepVO();
            String key = (String) map.get("key");
            Long value = (Long) map.get("value");
            regDataRepVO.setRegin(key);
            regDataRepVO.setVisitNum(value);
            String fieldKeyword = field + ".keyword";
            regDataRepVO.setNewUserNum(getNewUser(vo, fieldKeyword, key));
            regDataRepVO.setAvgStopTime(getStopRate(vo, fieldKeyword, key));
            regDataRepVO.setOpenNum(getOpenNum(vo, fieldKeyword, key));
            regDataRepVO.setUserNum(getUserNum(vo, fieldKeyword, key));
            String percent = DecimalFormatUtils.getPercent(getExitRate(vo, fieldKeyword).intValue(), regDataRepVO.getOpenNum().intValue());
            regDataRepVO.setExitRate(percent);
            result.add(regDataRepVO);
        }
        return result;
    }

    private List<UserActiveRepVO> getActiveBean(Map<String, Integer> map, UserVO vo) {
        List<UserActiveRepVO> result = new ArrayList<>();
        map.forEach((k, v) -> {
            UserActiveRepVO userActiveRepVO = new UserActiveRepVO();
            String date = k;
            Date begtime = TimeUtils.getstrDate(date);
            Date endtime = TimeUtils.getstrDate(TimeUtils.addDayone(date));
            UserVO userVO = new UserVO();
            userVO.setAppKey(vo.getAppKey());
            userVO.setBeginDate(begtime);
            userVO.setEndDate(endtime);
            Long dau = getActive(userVO);
            userActiveRepVO.setDau(dau);
            //周
            userVO.setBeginDate(TimeUtils.getstrDate(TimeUtils.reduceDayone(date, -6)));
            userVO.setEndDate(TimeUtils.getstrDate(date));
            Long wau = getActive(userVO);
            userActiveRepVO.setWau(wau);
            userVO.setBeginDate(TimeUtils.getstrDate(TimeUtils.reduceDayone(date, -29)));
            userVO.setEndDate(TimeUtils.getstrDate(date));
            Long mau = getActive(userVO);
            userActiveRepVO.setMau(mau);
            userActiveRepVO.setCurDate(date);
            String dmRate = DecimalFormatUtils.getPercent(dau.intValue(), mau.intValue());
            userActiveRepVO.setDmRate(dmRate);
            String dwRate = DecimalFormatUtils.getPercent(dau.intValue(), wau.intValue());
            userActiveRepVO.setDwRate(dwRate);
            result.add(userActiveRepVO);

        });
        return result;
    }

    private Long getActive(UserVO userVO) {
        //ri
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", userVO.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").gte(userVO.getBeginDate().getTime()).lte(userVO.getEndDate().getTime()));
        long num = esUtil.groupBy(EsTable.INDEX, "u.keyword", queryBuilder);
        return num;
    }

    private List<UserNewAddRepVO> getUserNewAdd(Map<String, Integer> map, UserVO vo, Integer... type) {
        List<UserNewAddRepVO> list = new ArrayList<>();
        map.forEach((k, v) -> {
            UserNewAddRepVO userNewAddRepVO = new UserNewAddRepVO();
            String date = k;
            Date begtime = TimeUtils.getstrDate(date);
            Date endtime = TimeUtils.getstrDate(TimeUtils.addDayone(date));
            //新用户数
            UserVO userVO = new UserVO();
            userVO.setAppKey(vo.getAppKey());
            userVO.setBeginDate(begtime);
            userVO.setEndDate(endtime);
            Long userNum = getNewUser(userVO);
            userNewAddRepVO.setCount(userNum);
            Map<String, Object> userNumMap = getUser(userVO, type);
            String oneLater = getUserRate(userVO, date, userNum, 1, userNumMap, type);
            String twoLater = getUserRate(userVO, date, userNum, 2, userNumMap, type);
            String threeLater = getUserRate(userVO, date, userNum, 3, userNumMap, type);
            String fourLater = getUserRate(userVO, date, userNum, 4, userNumMap, type);
            String fiveLater = getUserRate(userVO, date, userNum, 5, userNumMap, type);
            String sixLater = getUserRate(userVO, date, userNum, 6, userNumMap, type);
            String sevenLater = getUserRate(userVO, date, userNum, 7, userNumMap, type);
            String fourteenLater = getUserRate(userVO, date, userNum, 14, userNumMap, type);
            String thirtyLater = getUserRate(userVO, date, userNum, 30, userNumMap, type);
            userNewAddRepVO.setCurDate(date);
            userNewAddRepVO.setOneLater(oneLater);
            userNewAddRepVO.setTwoLater(twoLater);
            userNewAddRepVO.setThreeLater(threeLater);
            userNewAddRepVO.setFourLater(fourLater);
            userNewAddRepVO.setFiveLater(fiveLater);
            userNewAddRepVO.setSixLater(sixLater);
            userNewAddRepVO.setSevenLater(sevenLater);
            userNewAddRepVO.setFourteenLater(fourteenLater);
            userNewAddRepVO.setThirtyLater(thirtyLater);
            list.add(userNewAddRepVO);
        });
        return list;

    }

    private String getUserRate(UserVO userVO, String date, Long userNum, int day, Map<String, Object> userNumMap, Integer... type) {
        userVO.setBeginDate(TimeUtils.getstrDate(date));
        userVO.setEndDate(TimeUtils.getstrDate(TimeUtils.addDayone(date, day)));
        Map<String, Object> oneLaterMap = getUser(userVO, type);
        if (oneLaterMap.size() == 0 || userNum.intValue() == 0) return "0.00%";
        Map<String, Integer> accessMap = new HashMap();
        userNumMap.forEach((k1, v1) -> {
            Integer num = (Integer) oneLaterMap.get(k1);
            if (num != null) {
                accessMap.put(k1, 0);
            }
        });
        String percent = DecimalFormatUtils.getPercent(accessMap.size(), userNum.intValue());
        return percent + "%";
    }

    private Map<String, Object> getUser(UserVO vo, Integer... type) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").gte(vo.getBeginDate().getTime()).lt(vo.getEndDate().getTime()));
        if (type.length > 0) {
            queryBuilder.must(QueryBuilders.matchQuery("isfirstday", true));
            queryBuilder.must(QueryBuilders.matchQuery("tv.keyword", "app"));
            queryBuilder.must(QueryBuilders.matchQuery("tl.keyword", "launch"));
        }
        Map<String, Object> result = esUtil.groupByFiledMap(EsTable.INDEX, "o.keyword", queryBuilder, Integer.MAX_VALUE);
        return result;
    }

    private List<LostReturnRepVO> getLostandReturn(Map<String, Integer> map, UserVO vo) {
        List<LostReturnRepVO> list = new ArrayList<>();
        map.forEach((k, v) -> {
            LostReturnRepVO userNewAddRepVO = new LostReturnRepVO();
            String date = k;
            userNewAddRepVO.setCurDate(date);
            Date begtime = TimeUtils.getstrDate(date);
            Date endtime = TimeUtils.getstrDate(TimeUtils.addDayone(date));
            UserVO userVO = new UserVO();
            userVO.setAppKey(vo.getAppKey());
            userVO.setBeginDate(begtime);
            userVO.setEndDate(endtime);
            Map<String, Object> userNumMap = getUser(userVO);
            Map<String, Object> lostuser = getLost(userVO, date, userNumMap);
            Map<String, Object> returnUser = getuserReturn(userVO, date, userNumMap);
            long lostuserNum = lostuser.size();
            long returnUserNum = returnUser.size();
            userNewAddRepVO.setLostNum(lostuserNum);
            userNewAddRepVO.setReturnNum(returnUserNum);
            list.add(userNewAddRepVO);
        });
        return list;


    }

    private Map<String, Object> getUserNinety(UserVO vo) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("k.keyword", vo.getAppKey()));
        queryBuilder.must(QueryBuilders.rangeQuery("t").lt(vo.getBeginDate().getTime()));
        Map<String, Object> result = esUtil.groupByFiledMap(EsTable.INDEX, "o.keyword", queryBuilder, Integer.MAX_VALUE);
        return result;
    }

    private Map<String, Object> getLost(UserVO vo, String date, Map<String, Object> userNumMap) {
        LostReturnRepVO userNewAddRepVO = new LostReturnRepVO();
        userNewAddRepVO.setCurDate(date);
        Date begtime = TimeUtils.getstrDate(date);
        Date endtime = TimeUtils.getstrDate(TimeUtils.addDayone(date));
        UserVO userVO = new UserVO();
        userVO.setAppKey(vo.getAppKey());
        userVO.setBeginDate(begtime);
        userVO.setEndDate(endtime);
        userVO.setBeginDate(TimeUtils.getstrDate(TimeUtils.reduceDayone(date, -30)));
        userVO.setEndDate(TimeUtils.getstrDate(date));
        Map<String, Object> lostReturn = getUser(userVO);
        userVO.setBeginDate(TimeUtils.getstrDate(TimeUtils.reduceDayone(date, -30)));
        userVO.setEndDate(TimeUtils.getstrDate(TimeUtils.reduceDayone(date, -90)));
        Map<String, Object> lost = getUser(userVO);
        //沉默回流用户：在过去30天内无访问，90天内有访问（流失期在30~90天内），且当天访问的用户。
        Map<String, Object> threeMap = new LinkedHashMap<>();
        Map<String, Object> ninthMap = new LinkedHashMap<>();
        userNumMap.forEach((k1, v1) -> {
            if(lostReturn.get(k1)==null){
                threeMap.put(k1,0);
            }
            if (lost.get(k1) != null) {
                ninthMap.put(k1, 0);
            }
        });
        threeMap.putAll(ninthMap);
        return threeMap;
    }

    private Map<String, Object> getuserReturn(UserVO vo, String date, Map<String, Object> userNumMap) {
        vo.setBeginDate(TimeUtils.getstrDate(TimeUtils.reduceDayone(date, -90)));
        vo.setEndDate(TimeUtils.getstrDate(date));
        Map<String, Object> lost2 = getUser(vo);
        Map<String, Object> userNinety = getUserNinety(vo);
        Map<String, Object> ninthMap = new LinkedHashMap<>();
        Map<String, Object> ninthlaterMap = new LinkedHashMap<>();
        //流失回流用户
        userNumMap.forEach((k1, v1) -> {
            if(lost2.get(k1)==null){
                ninthMap.put(k1,0);
            }
            if (userNinety.get(k1) != null) {
                ninthlaterMap.put(k1, 0);
            }
        });
        ninthMap.putAll(ninthlaterMap);
        return ninthMap;
    }


}
