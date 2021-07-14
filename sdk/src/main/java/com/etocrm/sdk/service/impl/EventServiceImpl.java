package com.etocrm.sdk.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.eventVO.*;
import com.etocrm.sdk.service.EventService;
import com.etocrm.sdk.util.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.oval.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @author: xing.liu
 * @ 2020/9/2
 */
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private static String index = "sdk-20200826";

    private static String url="http://localhost:9521/api/event/download";

    @Autowired
    private EsUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private OvalUtils ovalUtils;

    @Override
    public Result downLoadEventList() {
        return  Result.success(url);
    }

    @Override
    public Result getEventList(EventVO vo) {
        List<ConstraintViolation> constraintViolations = ovalUtils.validatorRequestParam(vo);
        if (constraintViolations.size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder appKeyBuilder = QueryBuilders.matchQuery("appKey", vo.getAppKey());
        mustQuery.must(appKeyBuilder);
        if (StringUtils.isNotBlank(vo.getEventKey())) {
            MatchQueryBuilder tvBuilder = QueryBuilders.matchQuery("tv", vo.getEventKey());
            mustQuery.must(tvBuilder);
        }
        if (StringUtils.isNotBlank(vo.getEventName())) {
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl", vo.getEventName());
            mustQuery.mustNot(eventNameBuilder);
        }
        Map<String, Object> eventList = getEventList(EsTable.EVENT, vo.getPageSize(), vo.getPageIndex(), mustQuery);
        return  Result.success(eventList);
    }

    @Override
    public List<EventListVO> getEvent(DownLoadExcel  vo) {
        String appKey= vo.getAppkey();
        String  tv=vo.getTv();
        String  tl=vo.getTl();
        if (StringUtils.isBlank(appKey)) return Collections.emptyList();
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder appKeyBuilder = QueryBuilders.matchQuery("appKey", appKey);
        mustQuery.must(appKeyBuilder);
        if (StringUtils.isNotBlank(tv)) {
            MatchQueryBuilder tvBuilder = QueryBuilders.matchQuery("tv", tv);
            mustQuery.must(tvBuilder);
        }
        if (StringUtils.isNotBlank(tl)) {
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl", tl);
            mustQuery.must(eventNameBuilder);
        }
        List<EventListVO> event = getEvent(EsTable.EVENT, mustQuery);
        return event;
    }

    @Override
    public Result getEventNameList(String appKey, String tv, String tl) {
        if (StringUtils.isBlank(appKey)) return Result.error(ResponseCode.PARAMETERS_NULL);
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder appKeyBuilder = QueryBuilders.matchQuery("appKey.keyword", appKey);
        //mustQuery.must(appKeyBuilder);
        if (StringUtils.isNotBlank(tv)) {
            MatchQueryBuilder tvBuilder = QueryBuilders.matchQuery("tv.keyword", tv);
            mustQuery.must(tvBuilder);
        }
        if (StringUtils.isNotBlank(tl)) {
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl.keyword", tl);
            mustQuery.must(eventNameBuilder);
        }
        List<EventListVO> event =getEvent(index, mustQuery);
        return Result.success(event);
    }

    @Override
    public Result getEventAnalysisList(EventAnalysisVO vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
       // BoolQueryBuilder query = getQuery(vo.getAppKey(), vo.getEventKey(), vo.getEventName(), vo.getBeginDate(), vo.getEndDate());
        PageUtils pageUtils = new PageUtils(vo.getPageSize(), vo.getPageIndex(), null);
        Map map = getEventAnalysisList(index, pageUtils, vo);
        return Result.success(map);
    }

    @Override
    public Result getSingleEvent(String appKey, String id) {
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(id))
            return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Object> map = esUtil.selectOne(EsTable.EVENT, id);
        return  Result.success(map);
    }

    @Override
    public Result editEvent(EventEditVO vo) {
        if (StringUtils.isBlank(vo.getId()) || ovalUtils.validatorRequestParam(vo).size()>0)
            return Result.error(ResponseCode.PARAMETERS_NULL);
        boolean flag = esUtil.updateColum(EsTable.EVENT, vo.getId(), JSON.toJSONString(vo));
        if (!flag) return  Result.error(ResponseCode.ES_EXCEPTION);
        return  Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result addEvent(EventEditVO vo) {
        String appKey = vo.getAppKey();
        String tv = vo.getTv();
        String tl = vo.getTl();
        if (ovalUtils.validatorRequestParam(vo).size()>0)
            return Result.error(ResponseCode.PARAMETERS_NULL);
        EventEsVO  esVO=new EventEsVO();
        BeanUtils.copyProperties(vo,esVO);
        esVO.setCreateTime(new Date());
        String id = esUtil.insert(EsTable.EVENT,JSON.toJSONString(esVO));
        if (StringUtils.isBlank(id)) return  Result.error(ResponseCode.ES_EXCEPTION);
        return Result.success(ResponseCode.SUCCESS);
    }

    @Override
    public Result getEventAnalysisDetails(EventAnalysisDeVo vo) {
        if (ovalUtils.validatorRequestParam(vo).size() > 0) return Result.error(ResponseCode.PARAMETERS_NULL);
        Map<String, Object> map = esUtil.selectOne(EsTable.EVENT, vo.getId());
        if (map == null) return  Result.success(Collections.emptyList());
        if (map != null) {
            String tv = (String) map.get("tv");
            String tl = (String) map.get("tl");
            BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl.keyword", tl);
            mustQuery.must(eventNameBuilder);
            MatchQueryBuilder tvNameBuilder = QueryBuilders.matchQuery("tv.keyword", tv);
            mustQuery.must(tvNameBuilder);
            //用户-日期-人数-次数
            List<String> datas = TimeUtils.findDates(vo.getBeginDate(), vo.getEndDate());
            if (datas != null && !datas.isEmpty()) {
                List<Map<String, Object>> list = new ArrayList<>();
                Map event = null;
                for (String date : datas) {
                    //date
                    long begTime = TimeUtils.getstrDate(date).getTime();
                    String s = TimeUtils.addDayone(date);
                    long endTime = TimeUtils.getstrDate(s).getTime();
                    event = new HashMap();
                    QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gte(begTime).lte(endTime);
                    mustQuery.must(queryBuilder);
                    //次数
                    long eventNum = esUtil.count(index, mustQuery);
                    long userNum = esUtil.groupBy(index, "uu.keyword", mustQuery);
                    if (eventNum > 0) {
                        event.put("eventNum", eventNum);
                        event.put("userNum", userNum);
                        event.put("source", userNum / eventNum);
                    } else {
                        //event.put("frequency",0);
                        //event.put("userNum",0);
                        event.put("source", 0);
                    }
                    event.put("date", date);
                    list.add(event);
                }
                return Result.success(list);
            }
        }
        return Result.success(Collections.emptyList());
    }

    @Override
    public Result getEventParamDetails(EventParamDetails vo) {
        String appKey = vo.getAppKey();
        if(ovalUtils.validatorRequestParam(vo).size()>0)return Result.error(ResponseCode.PARAMETERS_NULL);
        Integer type = vo.getType();
        List<Map<String, Object>> list = new ArrayList<>();
        Map map = null;
        if (type == 1) {
            map = new HashMap();
            map.put("date", "");
            map.put("sum", "");
            map.put("average", "");
            map.put("max", "");
            map.put("min", "");
            map.put("perCapita", "");
            map.put("distinctNum", "");

        } else if (type == 2) {
            map.put("date", "");
            map.put("distinctNum", "");
        }
        return Result.success(list);
    }

    @Override
    public Result delEvent( String id) {
        if (StringUtils.isBlank(id))
            return Result.error(ResponseCode.PARAMETERS_NULL);
        esUtil.deleteId(EsTable.EVENT, id);
        return  Result.success(ResponseCode.SUCCESS);
    }

    public Map<String,Object>  getEventList(String index, Integer pageSize, Integer pageIndex ,QueryBuilder queryBuilder ){
        Map  map=new HashMap();
        JSONArray jsonArray=new JSONArray();
        try{
            //条数
            long totalNum= esUtil.count(index, null);
            if(pageSize<1){
                pageSize=0;
            }
            //总页
            long  count=totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            if (pageIndex != null) {
                if (pageIndex < 1) {
                    pageIndex = 1;
                }
                if (pageIndex >= count) {
                    int i=(int) count;
                    pageIndex = i;
                }
            } else {
                pageIndex = 1;
            }
            searchSourceBuilder.size(pageSize);
            searchSourceBuilder.from((pageIndex-1)*pageSize);
            searchSourceBuilder.query(queryBuilder);
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();


            map.put("totalNum",totalNum);

            map.put("count",count);
            Map  event=null;
            List<Map<String,Object>> list=new ArrayList<>();
            for (SearchHit hit : hits.getHits()) {
                JSONObject obj = JSONObject.parseObject(hit.getSourceAsString());
                String tv=obj.getString("tv");
                String tl=obj.getString("tl");
                String createTime=obj.getString("createTime");
                String eventName=obj.getString("eventName");
                event=new HashMap();
                event.put("eventKey",tv+"."+tl);
                event.put("eventName",eventName);
                event.put("createTime",createTime);
                event.put("id",hit.getId());
                list.add(event);
            }
            map.put("data",list);
        }catch (Exception  e){
            log.error(e.getMessage(), e);
            map.put("totalNum",0);
            map.put("count",0);
            map.put("data",Collections.emptyList());
        }
        return   map;
    }

    private List<EventListVO>  getEvent(String  index,QueryBuilder queryBuilder){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        //searchRequest.types("doc");
        List<EventListVO> list=new ArrayList<>();
        try{
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            EventListVO  event=null;
            for (SearchHit hit : hits.getHits()) {
                JSONObject obj = JSONObject.parseObject(hit.getSourceAsString());
                String tv=obj.getString("tv");
                String tl=obj.getString("tl");
                Long t=obj.getLong("t");
                event=new EventListVO();
                event.setEventKey(tv);
                String createTime = TimeUtils.getString(t);
                event.setCreateTime(createTime);
                event.setEventName(tl);
                list.add(event);
            }
            return  list;
        }catch (Exception  e){
            log.error(e.getMessage(), e);
        }
        return   list;
    }

    /**
     * @Description: 新版小程序te对象里获取小程序标志，前端埋点？？
     * @author xing.liu
     * @date ${DATE} ${TIME}
     */
      private   Map   getEventAnalysisList(String index,PageUtils pageUtils,EventAnalysisVO vo){
        Map  map=new HashMap();
        JSONArray jsonArray=new JSONArray();
        try{
            BoolQueryBuilder queryBuilder = get(vo);
            //条数
            long totalNum= esUtil.count(EsTable.EVENT, queryBuilder);
            pageUtils.setTotalNum((int)totalNum);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            searchSourceBuilder.size(pageUtils.getPageSize().intValue());
            searchSourceBuilder.from(pageUtils.getLimit().intValue());
            searchSourceBuilder.query(queryBuilder);
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();


            map.put("totalNum",pageUtils.getTotalNum());

            map.put("count",pageUtils.getCount());
            Map  event=null;
            List<Map<String,Object>> list=new ArrayList<>();
            for (SearchHit hit : hits.getHits()) {
                JSONObject obj = JSONObject.parseObject(hit.getSourceAsString());
                String tv=obj.getString("tv");
                String tl=obj.getString("tl");
                Long t=obj.getLong("t");
                String eventName=obj.getString("eventName");
                event=new HashMap();
                event.put("tv",tv);//前端
                event.put("tl",tl);
                event.put("createTime",TimeUtils.getString(t));
                event.put("id",hit.getId());
                //用户数，次数、人均
                //根据事件名称来查询（tl）
                //
                RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("t").gte(vo.getEndDate()).lte(vo.getEndDate());
                queryBuilder.must(timeBuilder);
                //次数
                long eventNum=esUtil.count(index,queryBuilder);
                //用户数
                queryBuilder.must(QueryBuilders.existsQuery("uu.keyword"));
                long userNum=esUtil.groupBy(index,"uu.keyword",queryBuilder);
                if(eventNum>0){
                    event.put("eventNum",eventNum);
                    event.put("userNum",userNum);
                    event.put("source",userNum/eventNum);
                }else{
                    //event.put("frequency",0);
                    //event.put("userNum",0);
                    event.put("source",0);
                }
                list.add(event);
            }
            map.put("data",list);
        }catch (Exception  e){
            log.error(e.getMessage(), e);
            map.put("eventNum",0);
            map.put("userNum",0);
            map.put("source",0);
            map.put("data",Collections.emptyList());
        }
        return   map;

    }

    private  BoolQueryBuilder  get(EventAnalysisVO vo  ){
          String  appKey=vo.getEventKey();
          String  tv=vo.getEventKey();
          String  tl=vo.getEventName();
          BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
          MatchQueryBuilder appKeyBuilder = QueryBuilders.matchQuery("appKey.keyword", appKey);
          if (StringUtils.isNotBlank(tv)) {
                MatchQueryBuilder tvBuilder = QueryBuilders.matchQuery("tv.keyword", tv);
                mustQuery.must(tvBuilder);
          }
          if (StringUtils.isNotBlank(tl)) {
                MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl.keyword", tl);
                mustQuery.must(eventNameBuilder);
          }
            return mustQuery;
    }

    /**
     * @Description: 查询条件
     * @author xing.liu
     * @date ${DATE}${TIME}
     */
    private BoolQueryBuilder getQuery(String appKey, String tv, String tl, Date beginDate, Date endDate) {
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        MatchQueryBuilder appKeyBuilder = QueryBuilders.matchQuery("appKey.keyword", appKey);
        /*QueryBuilder queryBuilder = QueryBuilders.rangeQuery("t").gte(beginDate).lte(beginDate);
        mustQuery.must(queryBuilder);*/
        // mustQuery.must(appKeyBuilder);
        if (StringUtils.isNotBlank(tv)) {
            MatchQueryBuilder tvBuilder = QueryBuilders.matchQuery("tv.keyword", tv);
            mustQuery.must(tvBuilder);
        }
        if (StringUtils.isNotBlank(tl)) {
            MatchQueryBuilder eventNameBuilder = QueryBuilders.matchQuery("tl.keyword", tl);
            mustQuery.must(eventNameBuilder);
        }
        return mustQuery;
    }



}
